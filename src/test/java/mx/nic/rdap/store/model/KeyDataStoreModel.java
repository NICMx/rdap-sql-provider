package mx.nic.rdap.store.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.db.KeyData;
import mx.nic.rdap.sql.QueryGroup;

public class KeyDataStoreModel {

	private final static Logger logger = Logger.getLogger(KeyDataStoreModel.class.getName());

	private final static String QUERY_GROUP = "KeyDataStore";
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

	private static long storeToDatabase(KeyData keyData, Connection connection) throws SQLException {
		String query = getQueryGroup().getQuery(STORE_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			fillPreparedStatement(statement, keyData);
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			statement.executeUpdate();
			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.next();
			Long keyDataId = resultSet.getLong(1);

			keyData.setId(keyDataId);
		}

		EventStoreModel.storeKeyDataEventsToDatabase(keyData.getEvents(), keyData.getId(), connection);
		LinkStoreModel.storeKeyDataLinksToDatabase(keyData.getLinks(), keyData.getId(), connection);

		return keyData.getId();
	}

	public static void storeAllToDatabase(List<KeyData> keyDataList, Long secureDnsId, Connection connection)
			throws SQLException {
		if (keyDataList.isEmpty()) {
			return;
		}

		for (KeyData keyData : keyDataList) {
			keyData.setSecureDNSId(secureDnsId);
			storeToDatabase(keyData, connection);
		}

	}

	private static void fillPreparedStatement(PreparedStatement preparedStatement, KeyData keyData)
			throws SQLException {
		preparedStatement.setLong(1, keyData.getSecureDNSId());
		preparedStatement.setInt(2, keyData.getFlags());
		preparedStatement.setInt(3, keyData.getProtocol());
		preparedStatement.setString(4, keyData.getPublicKey());
		preparedStatement.setInt(5, keyData.getAlgorithm());
	}
}
