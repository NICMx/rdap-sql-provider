package mx.nic.rdap.db.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.db.VCardPostalInfo;
import mx.nic.rdap.db.QueryGroup;
import mx.nic.rdap.db.exception.ObjectNotFoundException;
import mx.nic.rdap.db.objects.VCardPostalInfoDbObj;

/**
 * Model for the {@link VCardPostalInfo}.
 * 
 */
public class VCardPostalInfoModel {
	private static final Logger logger = Logger.getLogger(VCardPostalInfoModel.class.getName());

	private static final String QUERY_GROUP = "VCardPostalInfo";

	private final static String STORE_QUERY = "storeToDatabase";
	private final static String GET_QUERY = "getById";
	private final static String GET_BY_VCARD_QUERY = "getByVCardId";
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

	/**
	 * Store a VCardPostalInfo
	 * 
	 */
	public static long storeToDatabase(VCardPostalInfo vCardPostalInfo, Connection connection) throws SQLException {
		long insertedId;

		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery(STORE_QUERY),
				Statement.RETURN_GENERATED_KEYS);) {
			((VCardPostalInfoDbObj) vCardPostalInfo).storeToDatabase(statement);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();

			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.next();
			insertedId = resultSet.getLong(1);
			vCardPostalInfo.setId(insertedId);
		}

		return insertedId;
	}

	/**
	 * Gets a {@link List} of {@link VCardPostalInfo} by a VCardId
	 */
	public static List<VCardPostalInfo> getByVCardId(Long vCardId, Connection connection)
			throws SQLException, ObjectNotFoundException {
		List<VCardPostalInfo> vCardPostalInfoResult = null;
		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery(GET_BY_VCARD_QUERY));) {
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
