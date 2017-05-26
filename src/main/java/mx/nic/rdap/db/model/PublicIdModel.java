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

import mx.nic.rdap.core.db.PublicId;
import mx.nic.rdap.db.QueryGroup;
import mx.nic.rdap.db.objects.PublicIdDAO;

/**
 * Model for the {@link PublicId} Object
 * 
 */
public class PublicIdModel {

	private final static Logger logger = Logger.getLogger(PublicIdModel.class.getName());

	private final static String QUERY_GROUP = "PublicId";

	private static QueryGroup queryGroup = null;

	private static final String STORE_QUERY = "storeToDatabase";
	private static final String GET_ALL_QUERY = "getAll";
	private static final String ENTITY_GET_QUERY = "getByEntity";
	private static final String DOMAIN_GET_QUERY = "getByDomain";
	private static final String ENTITY_STORE_QUERY = "storeEntityPublicIdsToDatabase";
	private static final String DOMAIN_STORE_QUERY = "storeDomainPublicIdsToDatabase";

	static {
		try {
			queryGroup = new QueryGroup(QUERY_GROUP);
		} catch (IOException e) {
			throw new RuntimeException("Error loading query group");
		}
	}

	public static void storeAllToDatabase(List<PublicId> publicIds, Connection connection) throws SQLException {
		for (PublicId publicId : publicIds) {
			PublicIdModel.storeToDatabase(publicId, connection);
		}
	}

	public static Long storeToDatabase(PublicId publicId, Connection connection) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(queryGroup.getQuery(STORE_QUERY),
				Statement.RETURN_GENERATED_KEYS);) {
			((PublicIdDAO) publicId).storeToDatabase(statement);
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			result.next();
			// The id of the link inserted
			Long resultId = result.getLong(1);
			publicId.setId(resultId);

			return publicId.getId();
		}
	}

	private static void storeBy(List<PublicId> publicIds, Long id, Connection connection, String query)
			throws SQLException {
		if (publicIds.isEmpty())
			return;

		try (PreparedStatement statement = connection.prepareStatement(queryGroup.getQuery(query))) {
			for (PublicId publicId : publicIds) {
				Long resultId = PublicIdModel.storeToDatabase(publicId, connection);
				statement.setLong(1, id);
				statement.setLong(2, resultId);
				logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
				statement.executeUpdate();
			}
		}
	}

	public static void storePublicIdByDomain(List<PublicId> publicIds, Long domainId, Connection connection)
			throws SQLException {
		storeBy(publicIds, domainId, connection, DOMAIN_STORE_QUERY);
	}

	public static void storePublicIdByEntity(List<PublicId> publicIds, Long entityId, Connection connection)
			throws SQLException {
		storeBy(publicIds, entityId, connection, ENTITY_STORE_QUERY);
	}

	private static List<PublicId> getBy(Long entityId, Connection connection, String query) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(queryGroup.getQuery(query))) {
			statement.setLong(1, entityId);
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			try (ResultSet resultSet = statement.executeQuery()) {
				return processResultSet(resultSet);
			}
		}
	}

	public static List<PublicId> getAll(Connection connection) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY)) {
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			ResultSet resultSet = statement.executeQuery();
			return processResultSet(resultSet);
		}
	}

	public static List<PublicId> getByDomain(Long domainId, Connection connection) throws SQLException {
		return getBy(domainId, connection, DOMAIN_GET_QUERY);
	}

	public static List<PublicId> getByEntity(Long entityId, Connection connection) throws SQLException {
		return getBy(entityId, connection, ENTITY_GET_QUERY);
	}

	private static List<PublicId> processResultSet(ResultSet resultSet) throws SQLException {
		if (!resultSet.next()) {
			// Did not retrieve any public Ids
			return Collections.emptyList();
		}
		List<PublicId> publicIds = new ArrayList<PublicId>();
		do {
			PublicIdDAO publicId = new PublicIdDAO(resultSet);
			publicIds.add(publicId);
		} while (resultSet.next());
		return publicIds;
	}

}