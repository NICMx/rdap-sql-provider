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

import mx.nic.rdap.core.db.DsData;
import mx.nic.rdap.sql.QueryGroup;

/**
 * Model for the {@link DsData} Object
 * 
 */
public class DsDataStoreModel {

	private final static Logger logger = Logger.getLogger(DsDataStoreModel.class.getName());

	private final static String QUERY_GROUP = "DsDataStore";
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

	private static long storeToDatabase(DsData dsData, Connection connection) throws SQLException {
		String query = getQueryGroup().getQuery(STORE_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			fillPreparedStatement(statement, dsData);
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			statement.executeUpdate();
			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.next();
			Long dsDataId = resultSet.getLong(1);

			dsData.setId(dsDataId);
		}

		EventStoreModel.storeDsDataEventsToDatabase(dsData.getEvents(), dsData.getId(), connection);
		LinkStoreModel.storeDsDataLinksToDatabase(dsData.getLinks(), dsData.getId(), connection);

		return dsData.getId();
	}

	public static void storeAllToDatabase(List<DsData> dsDataList, Long secureDnsId, Connection connection)
			throws SQLException {
		if (dsDataList.isEmpty()) {
			return;
		}

		for (DsData dsData : dsDataList) {
			dsData.setSecureDNSId(secureDnsId);
			storeToDatabase(dsData, connection);
		}

	}

	private static void fillPreparedStatement(PreparedStatement preparedStatement, DsData dsData) throws SQLException {
		preparedStatement.setLong(1, dsData.getSecureDNSId());
		preparedStatement.setInt(2, dsData.getKeytag());
		preparedStatement.setInt(3, dsData.getAlgorithm());
		preparedStatement.setString(4, dsData.getDigest());
		preparedStatement.setInt(5, dsData.getDigestType());

	}

}
