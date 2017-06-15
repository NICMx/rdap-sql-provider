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

import mx.nic.rdap.core.db.PublicId;
import mx.nic.rdap.sql.QueryGroup;

/**
 * Model for the {@link PublicId} Object
 * 
 */
public class PublicIdStoreModel {

	private final static Logger logger = Logger.getLogger(PublicIdStoreModel.class.getName());

	private final static String QUERY_GROUP = "PublicIdStore";

	private static QueryGroup queryGroup = null;

	private static final String STORE_QUERY = "storeToDatabase";
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
			fillPreparedStatement(statement, publicId);
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
				Long resultId = PublicIdStoreModel.storeToDatabase(publicId, connection);
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

	private static void fillPreparedStatement(PreparedStatement preparedStatement, PublicId publicId)
			throws SQLException {
		preparedStatement.setString(1, publicId.getType());
		preparedStatement.setString(2, publicId.getPublicId());
	}
}
