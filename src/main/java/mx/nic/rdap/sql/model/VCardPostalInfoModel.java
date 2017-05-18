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

import mx.nic.rdap.core.db.VCardPostalInfo;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.SQLProviderConfiguration;
import mx.nic.rdap.sql.exception.ObjectNotFoundException;
import mx.nic.rdap.sql.objects.VCardPostalInfoDbObj;

/**
 * Model for the {@link VCardPostalInfo}.
 * 
 */
public class VCardPostalInfoModel {
	private static final Logger logger = Logger.getLogger(VCardPostalInfoModel.class.getName());

	private static final String QUERY_GROUP = "VCardPostalInfo";

	private final static String GET_BY_VCARD_QUERY = "getByVCardId";
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
	 * Gets a {@link List} of {@link VCardPostalInfo} by a VCardId
	 */
	public static List<VCardPostalInfo> getByVCardId(Long vCardId, Connection connection)
			throws SQLException, ObjectNotFoundException {
		List<VCardPostalInfo> vCardPostalInfoResult = null;
		String query = getQueryGroup().getQuery(GET_BY_VCARD_QUERY);
		if (SQLProviderConfiguration.isUserSQL() && query == null) {
			return Collections.emptyList();
		}
		try (PreparedStatement statement = connection.prepareStatement(query);) {
			statement.setLong(1, vCardId);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			ResultSet resultSet = statement.executeQuery();
			vCardPostalInfoResult = processListResultSet(resultSet, connection);
		}
		return vCardPostalInfoResult;
	}

	/**
	 * Process a {@link ResultSet} and return a {@link List} of
	 * {@link VCardPostalInfo}.
	 */
	private static List<VCardPostalInfo> processListResultSet(ResultSet resultSet, Connection connection)
			throws SQLException, ObjectNotFoundException {
		List<VCardPostalInfo> result = new ArrayList<>();

		if (!resultSet.next()) {
			throw new ObjectNotFoundException("Object not found");
		}
		do {
			VCardPostalInfoDbObj vCardPostalInfo = new VCardPostalInfoDbObj();
			vCardPostalInfo.loadFromDatabase(resultSet);
			result.add(vCardPostalInfo);
		} while (resultSet.next());

		return result;
	}

}
