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

import mx.nic.rdap.core.db.Remark;
import mx.nic.rdap.sql.QueryGroup;

/**
 * Model for the {@link Remark} Object
 * 
 */
public class RemarkStoreModel {

	private static final Logger logger = Logger.getLogger(RemarkStoreModel.class.getName());

	private static final String QUERY_GROUP = "RemarkStore";

	private static final String NAMESERVER_STORE_QUERY = "storeNameserverRemarksToDatabase";
	private static final String DOMAIN_STORE_QUERY = "storeDomainRemarksToDatabase";
	private static final String ENTITY_STORE_QUERY = "storeEntityRemarksToDatabase";
	private static final String AUTNUM_STORE_QUERY = "storeAutnumRemarksToDatabase";
	private static final String IP_NETWORK_STORE_QUERY = "storeIpNetworkRemarksToDatabase";

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

	private static long storeToDatabase(Remark remark, Connection connection) throws SQLException {

		// The Remark's id is autoincremental, Statement.RETURN_GENERATED_KEYS
		// give us the id generated for the object stored
		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery("storeToDatabase"),
				Statement.RETURN_GENERATED_KEYS)) {
			fillPreparedStatement(statement, remark);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			result.next();
			// The id of the remark inserted
			Long remarkInsertedId = result.getLong(1);
			remark.setId(remarkInsertedId);
			RemarkDescriptionStoreModel.storeAllToDatabase(remark.getDescriptions(), remarkInsertedId, connection);
			LinkStoreModel.storeRemarkLinksToDatabase(remark.getLinks(), remarkInsertedId, connection);
			return remarkInsertedId;
		}
	}

	private static void storeRelationRemarksToDatabase(List<Remark> remarks, Long id, Connection connection,
			String queryId) throws SQLException {
		if (remarks.isEmpty())
			return;

		String query = getQueryGroup().getQuery(queryId);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			for (Remark remark : remarks) {
				Long remarkId = RemarkStoreModel.storeToDatabase(remark, connection);
				statement.setLong(1, id);
				statement.setLong(2, remarkId);
				logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
				statement.executeUpdate();
			}
		}
	}

	public static void storeNameserverRemarksToDatabase(List<Remark> remarks, Long nameserverId, Connection connection)
			throws SQLException {
		storeRelationRemarksToDatabase(remarks, nameserverId, connection, NAMESERVER_STORE_QUERY);
	}

	public static void storeDomainRemarksToDatabase(List<Remark> remarks, Long domainId, Connection connection)
			throws SQLException {
		storeRelationRemarksToDatabase(remarks, domainId, connection, DOMAIN_STORE_QUERY);
	}

	public static void storeAutnumRemarksToDatabase(List<Remark> remarks, Long autnumId, Connection connection)
			throws SQLException {
		storeRelationRemarksToDatabase(remarks, autnumId, connection, AUTNUM_STORE_QUERY);
	}

	public static void storeEntityRemarksToDatabase(List<Remark> remarks, Long entityId, Connection connection)
			throws SQLException {
		storeRelationRemarksToDatabase(remarks, entityId, connection, ENTITY_STORE_QUERY);
	}

	public static void storeIpNetworkRemarksToDatabase(List<Remark> remarks, Long ipNetworkId, Connection connection)
			throws SQLException {
		storeRelationRemarksToDatabase(remarks, ipNetworkId, connection, IP_NETWORK_STORE_QUERY);
	}

	private static void fillPreparedStatement(PreparedStatement preparedStatement, Remark remark) throws SQLException {
		preparedStatement.setString(1, remark.getTitle());
		preparedStatement.setString(2, remark.getType());
		preparedStatement.setString(3, remark.getLanguage());
	}
}
