package mx.nic.rdap.store.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.catalog.Status;
import mx.nic.rdap.sql.QueryGroup;

/**
 * Model for the {@link Status} Object
 * 
 */
public class StatusStoreModel {

	private final static Logger logger = Logger.getLogger(StatusStoreModel.class.getName());

	private final static String QUERY_GROUP = "StatusStore";

	private static QueryGroup queryGroup = null;

	private static final String NAMESERVER_STORE_QUERY = "storeNameserverStatusToDatabase";
	private static final String DOMAIN_STORE_QUERY = "storeDomainStatusToDatabase";
	private static final String ENTITY_STORE_QUERY = "storeEntityStatusToDatabase";
	private static final String AUTNUM_STORE_QUERY = "storeAutnumStatusToDatabase";
	private static final String IP_NETWORK_STORE_QUERY = "storeIpNetworkStatusToDatabase";

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

	public static void storeNameserverStatusToDatabase(List<Status> statusList, Long nameserverId,
			Connection connection) throws SQLException {
		storeRelationStatusToDatabase(statusList, nameserverId, connection, NAMESERVER_STORE_QUERY);
	}

	public static void storeDomainStatusToDatabase(List<Status> statusList, Long domainId, Connection connection)
			throws SQLException {
		storeRelationStatusToDatabase(statusList, domainId, connection, DOMAIN_STORE_QUERY);
	}

	public static void storeEntityStatusToDatabase(List<Status> statusList, Long entityId, Connection connection)
			throws SQLException {
		storeRelationStatusToDatabase(statusList, entityId, connection, ENTITY_STORE_QUERY);
	}

	public static void storeAutnumStatusToDatabase(List<Status> statusList, Long autnumId, Connection connection)
			throws SQLException {
		storeRelationStatusToDatabase(statusList, autnumId, connection, AUTNUM_STORE_QUERY);
	}

	public static void storeIpNetworkStatusToDatabase(List<Status> statusList, Long ipNetworkId, Connection connection)
			throws SQLException {
		storeRelationStatusToDatabase(statusList, ipNetworkId, connection, IP_NETWORK_STORE_QUERY);
	}

	private static void storeRelationStatusToDatabase(List<Status> statusList, Long id, Connection connection,
			String storeQueryId) throws SQLException {
		if (statusList.isEmpty())
			return;

		String query = getQueryGroup().getQuery(storeQueryId);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			for (Status status : statusList) {
				statement.setLong(1, id);
				statement.setLong(2, status.getId());
				logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
				statement.executeUpdate();
			}
		}
	}

}
