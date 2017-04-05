package mx.nic.rdap.sql.model;

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
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.objects.PublicIdDbObj;

/**
 * Model for the {@link PublicId} Object
 * 
 */
public class PublicIdModel {

	private final static Logger logger = Logger.getLogger(PublicIdModel.class.getName());

	private final static String QUERY_GROUP = "PublicId";

	private static QueryGroup queryGroup = null;

	private static final String STORE_QUERY = "storeToDatabase";
	private static final String ENTITY_GET_QUERY = "getByEntity";
	private static final String DOMAIN_GET_QUERY = "getByDomain";
	private static final String ENTITY_STORE_QUERY = "storeEntityPublicIdsToDatabase";
	private static final String DOMAIN_STORE_QUERY = "storeDomainPublicIdsToDatabase";

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

	private static Long storeToDatabase(PublicId publicId, Connection connection) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery(STORE_QUERY),
				Statement.RETURN_GENERATED_KEYS);) {
			((PublicIdDbObj) publicId).storeToDatabase(statement);
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

		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery(query))) {
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
		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery(query))) {
			statement.setLong(1, entityId);
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			try (ResultSet resultSet = statement.executeQuery()) {
				return processResultSet(resultSet);
			}
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
			PublicIdDbObj publicId = new PublicIdDbObj(resultSet);
			publicIds.add(publicId);
		} while (resultSet.next());
		return publicIds;
	}

}
