package mx.nic.rdap.db.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.db.KeyData;
import mx.nic.rdap.db.QueryGroup;
import mx.nic.rdap.db.objects.KeyDataDbObj;

public class KeyDataModel {

	private final static Logger logger = Logger.getLogger(KeyDataModel.class.getName());

	private final static String QUERY_GROUP = "KeyData";
	private final static String STORE_QUERY = "storeToDatabase";
	private final static String GET_QUERY = "getBySecureDns";

	protected static QueryGroup queryGroup = null;

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

	public static long storeToDatabase(KeyData keyData, Connection connection) throws SQLException {
		String query = getQueryGroup().getQuery(STORE_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			((KeyDataDbObj) keyData).storeToDatabase(statement);
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			statement.executeUpdate();
			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.next();
			Long keyDataId = resultSet.getLong(1);

			keyData.setId(keyDataId);
		}

		EventModel.storeKeyDataEventsToDatabase(keyData.getEvents(), keyData.getId(), connection);
		LinkModel.storeKeyDataLinksToDatabase(keyData.getLinks(), keyData.getId(), connection);

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

	/**
	 * Find {@link KeyData} by a SecureDnsId
	 * 
	 */
	public static List<KeyData> getBySecureDnsId(Long secureDnsId, Connection connection) throws SQLException {
		String query = getQueryGroup().getQuery(GET_QUERY);
		List<KeyData> resultList = null;

		try (PreparedStatement statement = connection.prepareStatement(query);) { // QUERY
			statement.setLong(1, secureDnsId);
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next()) {
				return Collections.emptyList();
			}

			resultList = new ArrayList<>();

			do {
				KeyDataDbObj keyData = new KeyDataDbObj(resultSet);
				keyData.setEvents(EventModel.getByKeyDataId(keyData.getId(), connection));
				keyData.getLinks().addAll(LinkModel.getByKeyDataId(keyData.getId(), connection));
				resultList.add(keyData);
			} while (resultSet.next());
		}

		return resultList;
	}
}
