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
import mx.nic.rdap.db.objects.VCardPostalInfoDAO;

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

	static {
		try {
			queryGroup = new QueryGroup(QUERY_GROUP);
		} catch (IOException e) {
			throw new RuntimeException("Error while loading query group on " + VCardPostalInfoModel.class.getName(), e);
		}
	}

	/**
	 * Store a VCardPostalInfo
	 * 
	 */
	public static long storeToDatabase(VCardPostalInfo vCardPostalInfo, Connection connection) throws SQLException {
		long insertedId;

		try (PreparedStatement statement = connection.prepareStatement(queryGroup.getQuery(STORE_QUERY),
				Statement.RETURN_GENERATED_KEYS);) {
			((VCardPostalInfoDAO) vCardPostalInfo).storeToDatabase(statement);
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
	 * Get a {@link VCardPostalInfo} by its id.
	 * 
	 * @throws ObjectNotFoundException
	 * 
	 */
	public static VCardPostalInfo getById(Long vCardPostalInfoId, Connection connection)
			throws SQLException, ObjectNotFoundException {
		VCardPostalInfo vCardPostalInfoResult = null;
		try (PreparedStatement statement = connection.prepareStatement(queryGroup.getQuery(GET_QUERY));) {
			statement.setLong(1, vCardPostalInfoId);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			ResultSet resultSet = statement.executeQuery();
			vCardPostalInfoResult = processResultSet(resultSet, connection);
		}
		return vCardPostalInfoResult;
	}

	/**
	 * Gets a {@link List} of {@link VCardPostalInfo} by a VCardId
	 * 
	 * @throws ObjectNotFoundException
	 * 
	 */
	public static List<VCardPostalInfo> getByVCardId(Long vCardId, Connection connection)
			throws SQLException, ObjectNotFoundException {
		List<VCardPostalInfo> vCardPostalInfoResult = null;
		try (PreparedStatement statement = connection.prepareStatement(queryGroup.getQuery(GET_BY_VCARD_QUERY));) {
			statement.setLong(1, vCardId);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			ResultSet resultSet = statement.executeQuery();
			vCardPostalInfoResult = processListResultSet(resultSet, connection);
		}
		return vCardPostalInfoResult;
	}

	/**
	 * Process a {@link ResultSet} and return one {@link VCardPostalInfo}.
	 * 
	 * @throws ObjectNotFoundException
	 * 
	 */
	private static VCardPostalInfo processResultSet(ResultSet resultSet, Connection connection)
			throws SQLException, ObjectNotFoundException {
		if (!resultSet.next()) {
			throw new ObjectNotFoundException("Object not found");
		}
		VCardPostalInfoDAO vCardPostalInfo = new VCardPostalInfoDAO();
		vCardPostalInfo.loadFromDatabase(resultSet);

		return vCardPostalInfo;
	}

	/**
	 * Process a {@link ResultSet} and return a {@link List} of
	 * {@link VCardPostalInfo}.
	 * 
	 * @throws ObjectNotFoundException
	 * 
	 */
	private static List<VCardPostalInfo> processListResultSet(ResultSet resultSet, Connection connection)
			throws SQLException, ObjectNotFoundException {
		List<VCardPostalInfo> result = new ArrayList<>();

		if (!resultSet.next()) {
			throw new ObjectNotFoundException("Object not found");
		}
		do {
			VCardPostalInfoDAO vCardPostalInfo = new VCardPostalInfoDAO();
			vCardPostalInfo.loadFromDatabase(resultSet);
			result.add(vCardPostalInfo);
		} while (resultSet.next());

		return result;
	}

}
