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

import mx.nic.rdap.core.db.DsData;
import mx.nic.rdap.db.QueryGroup;
import mx.nic.rdap.db.exception.RequiredValueNotFoundException;
import mx.nic.rdap.db.objects.DsDataDAO;

/**
 * Model for the {@link DsData} Object
 * 
 */
public class DsDataModel {

	private final static Logger logger = Logger.getLogger(DsDataModel.class.getName());

	private final static String QUERY_GROUP = "DsData";
	private final static String STORE_QUERY = "storeToDatabase";
	private final static String GET_QUERY = "getBySecureDns";

	protected static QueryGroup queryGroup = null;

	static {
		try {
			queryGroup = new QueryGroup(QUERY_GROUP);
		} catch (IOException e) {
			throw new RuntimeException("Error loading query group.");
		}
	}

	public static long storeToDatabase(DsData dsData, Connection connection)
			throws SQLException, RequiredValueNotFoundException {
		String query = queryGroup.getQuery(STORE_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			((DsDataDAO) dsData).storeToDatabase(statement);
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			statement.executeUpdate();
			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.next();
			Long dsDataId = resultSet.getLong(1);

			dsData.setId(dsDataId);
		}

		EventModel.storeDsDataEventsToDatabase(dsData.getEvents(), dsData.getId(), connection);
		LinkModel.storeDsDataLinksToDatabase(dsData.getLinks(), dsData.getId(), connection);

		return dsData.getId();
	}

	public static void storeAllToDatabase(List<DsData> dsDataList, Long secureDnsId, Connection connection)
			throws SQLException, RequiredValueNotFoundException {
		if (dsDataList.isEmpty()) {
			return;
		}

		for (DsData dsData : dsDataList) {
			dsData.setSecureDNSId(secureDnsId);
			storeToDatabase(dsData, connection);
		}

	}

	/**
	 * Finds a SercureDns´s DsData
	 * 
	 */
	public static List<DsData> getBySecureDnsId(Long secureDnsId, Connection connection) throws SQLException {
		String query = queryGroup.getQuery(GET_QUERY);
		List<DsData> resultList = null;

		try (PreparedStatement statement = connection.prepareStatement(query);) { // QUERY
			statement.setLong(1, secureDnsId);
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next()) {
				return Collections.emptyList();
			}

			resultList = new ArrayList<>();

			do {
				DsDataDAO dsData = new DsDataDAO(resultSet);
				dsData.setEvents(EventModel.getByDsDataId(dsData.getId(), connection));
				dsData.getLinks().addAll(LinkModel.getByDsDataId(dsData.getId(), connection));
				resultList.add(dsData);
			} while (resultSet.next());
		}

		return resultList;
	}

}
