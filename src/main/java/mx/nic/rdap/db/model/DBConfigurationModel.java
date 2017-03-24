package mx.nic.rdap.db.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.db.QueryGroup;
import mx.nic.rdap.db.exception.ObjectNotFoundException;

public class DBConfigurationModel {

	private final static Logger logger = Logger.getLogger(DBConfigurationModel.class.getName());
	private final static String QUERY_GROUP = "DBConfiguration";

	private static final String GET_PROPERTY_QUERY = "getProperty";
	private static final String UPDATE_PROPERTY_QUERY = "updateProperty";

	private static final String SCHEMA_SETTING_KEY = "schema";

	private static QueryGroup queryGroup;

	private static void setQueryGroup(QueryGroup qG) {
		queryGroup = qG;
	}

	private static QueryGroup getQueryGroup() {
		return queryGroup;
	}

	public static void loadQueryGroup(String schema) {
		try {
			QueryGroup queryGroup2 = new QueryGroup(QUERY_GROUP, schema);
			setQueryGroup(queryGroup2);
		} catch (IOException e) {
			throw new RuntimeException("Error loading query group");
		}
	}

	public static String getSchemaSetting(Connection connection) throws ObjectNotFoundException, SQLException {
		return getPropertyFromDb(SCHEMA_SETTING_KEY, connection);
	}

	public static void updateSchemaSetting(String newSchemaValue, Connection connection)
			throws ObjectNotFoundException, SQLException {
		updatePropertyFromDb(newSchemaValue, SCHEMA_SETTING_KEY, connection);
	}

	private static String getPropertyFromDb(String propertyName, Connection connection)
			throws SQLException, ObjectNotFoundException {
		String query = getQueryGroup().getQuery(GET_PROPERTY_QUERY);
		String setting = null;
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, propertyName);
			logger.log(Level.INFO, "Executing query: " + statement.toString());
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next()) {
				throw new ObjectNotFoundException("Property '" + propertyName + "' not found");
			}
			setting = resultSet.getString("con_value");
		}

		return setting;
	}

	private static void updatePropertyFromDb(String newValue, String propertyName, Connection connection)
			throws ObjectNotFoundException, SQLException {
		String oldValue;
		try {
			oldValue = getPropertyFromDb(propertyName, connection);
		} catch (ObjectNotFoundException e) {
			oldValue = null;
		}

		String query = getQueryGroup().getQuery(UPDATE_PROPERTY_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query);) {
			statement.setString(1, newValue);
			if (oldValue != null) {
				statement.setString(2, oldValue);
			} else {
				statement.setNull(2, Types.VARCHAR);
			}
			statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			statement.setString(4, propertyName);

			logger.log(Level.INFO, "Executing query: " + statement.toString());
			int executeUpdate = statement.executeUpdate();
			logger.log(Level.INFO, "Rows updated: " + executeUpdate);
		}
	}
}
