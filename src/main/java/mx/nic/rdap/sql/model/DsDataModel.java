package mx.nic.rdap.sql.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.db.DsData;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.objects.DsDataDbObj;

/**
 * Model for the {@link DsData} Object
 * 
 */
public class DsDataModel {

	private final static Logger logger = Logger.getLogger(DsDataModel.class.getName());

	private final static String QUERY_GROUP = "DsData";
	private final static String GET_QUERY = "getBySecureDns";

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
	 * Finds a SercureDns´s DsData
	 * 
	 */
	public static List<DsData> getBySecureDnsId(Long secureDnsId, Connection connection) throws SQLException {
		String query = getQueryGroup().getQuery(GET_QUERY);
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
				DsDataDbObj dsData = new DsDataDbObj(resultSet);
				dsData.setEvents(EventModel.getByDsDataId(dsData.getId(), connection));
				dsData.getLinks().addAll(LinkModel.getByDsDataId(dsData.getId(), connection));
				resultList.add(dsData);
			} while (resultSet.next());
		}

		return resultList;
	}

}
