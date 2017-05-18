/**
 * 
 */
package mx.nic.rdap.store.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.model.ZoneModel;

/**
 * Model for the Zone table, read all zones in the zone_table and keeps it in
 * memory for quickly access.
 */
public class ZoneStoreModel {

	private final static Logger logger = Logger.getLogger(ZoneStoreModel.class.getName());

	private final static String QUERY_GROUP = "ZoneStore";
	private final static String STORE_QUERY = "storeToDatabase";
	private final static String GET_BY_ZONE_NAME = "getByZoneName";

	private static QueryGroup queryGroup = null;

	public static void loadQueryGroup(String schema) {
		try {
			QueryGroup qG = new QueryGroup(QUERY_GROUP, schema);
			setQueryGroup(qG);
		} catch (IOException e) {
			throw new RuntimeException("Error loading query group");
		}
	}

	private static void setQueryGroup(QueryGroup qG) {
		queryGroup = qG;
	}

	private static QueryGroup getQueryGroup() {
		return queryGroup;
	}

	public static Integer storeToDatabase(String zoneName, Connection connection) throws SQLException {
		Integer idByZoneName = getIdByZoneName(zoneName, connection);

		if (idByZoneName != null) {
			return idByZoneName;
		}

		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery(STORE_QUERY),
				Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, zoneName);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();
			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.next();
			// Inserted Zone's Id
			idByZoneName = resultSet.getInt(1);

		}

		ZoneModel.loadAllFromDatabase(connection);
		return idByZoneName;
	}

	private static Integer getIdByZoneName(String zoneName, Connection connection) throws SQLException {
		String query = getQueryGroup().getQuery(GET_BY_ZONE_NAME);
		Integer result = null;

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, zoneName);
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				int int1 = rs.getInt(1);
				if (!rs.wasNull()) {
					result = int1;
				}
			}

		}

		return result;
	}

}
