package mx.nic.rdap.store.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.db.VCardPostalInfo;
import mx.nic.rdap.sql.QueryGroup;

/**
 * Model for the {@link VCardPostalInfo}.
 * 
 */
public class VCardPostalInfoStoreModel {
	private static final Logger logger = Logger.getLogger(VCardPostalInfoStoreModel.class.getName());

	private static final String QUERY_GROUP = "VCardPostalInfoStore";

	private final static String STORE_QUERY = "storeToDatabase";
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

	/**
	 * Store a VCardPostalInfo
	 * 
	 */
	public static long storeToDatabase(VCardPostalInfo vCardPostalInfo, Connection connection) throws SQLException {
		long insertedId;

		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery(STORE_QUERY),
				Statement.RETURN_GENERATED_KEYS);) {
			fillPreparedStatement(statement, vCardPostalInfo);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();

			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.next();
			insertedId = resultSet.getLong(1);
			vCardPostalInfo.setId(insertedId);
		}

		return insertedId;
	}

	private static void fillPreparedStatement(PreparedStatement preparedStatement, VCardPostalInfo postalInfo)
			throws SQLException {
		preparedStatement.setLong(1, postalInfo.getVCardId());
		preparedStatement.setString(2, postalInfo.getType());
		preparedStatement.setString(3, postalInfo.getCountry());
		preparedStatement.setString(4, postalInfo.getCity());
		preparedStatement.setString(5, postalInfo.getStreet1());
		preparedStatement.setString(6, postalInfo.getStreet2());
		preparedStatement.setString(7, postalInfo.getStreet3());
		preparedStatement.setString(8, postalInfo.getState());
		preparedStatement.setString(9, postalInfo.getPostalCode());
	}

}
