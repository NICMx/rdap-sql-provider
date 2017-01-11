package mx.nic.rdap.db.model;

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

import mx.nic.rdap.core.catalog.Status;
import mx.nic.rdap.db.QueryGroup;

/**
 * Model for the {@link Status} Object
 * 
 */
public class StatusModel {

	private final static Logger logger = Logger.getLogger(StatusModel.class.getName());

	private final static String QUERY_GROUP = "Status";

	protected static QueryGroup queryGroup = null;

	private static final String NAMESERVER_STORE_QUERY = "storeNameserverStatusToDatabase";
	private static final String DOMAIN_STORE_QUERY = "storeDomainStatusToDatabase";
	private static final String ENTITY_STORE_QUERY = "storeEntityStatusToDatabase";
	private static final String AUTNUM_STORE_QUERY = "storeAutnumStatusToDatabase";
	private static final String IP_NETWORK_STORE_QUERY = "storeIpNetworkStatusToDatabase";

	private static final String NAMESERVER_GET_QUERY = "getByNameServerId";
	private static final String DOMAIN_GET_QUERY = "getByDomainId";
	private static final String ENTITY_GET_QUERY = "getByEntityId";
	private static final String AUTNUM_GET_QUERY = "getByAutnumid";
	private static final String IP_NETWORK_GET_QUERY = "getByIpNetworkId";

	static {
		try {
			StatusModel.queryGroup = new QueryGroup(QUERY_GROUP);
		} catch (IOException e) {
			throw new RuntimeException("Error loading query group");
		}
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

		String query = queryGroup.getQuery(storeQueryId);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			for (Status status : statusList) {
				statement.setLong(1, id);
				statement.setLong(2, status.getId());
				logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
				statement.executeUpdate();
			}
		}
	}

	public static List<Status> getByNameServerId(Long nameserverId, Connection connection) throws SQLException {
		return getByRelationsId(nameserverId, connection, NAMESERVER_GET_QUERY);
	}

	public static List<Status> getByDomainId(Long domainId, Connection connection) throws SQLException {
		return getByRelationsId(domainId, connection, DOMAIN_GET_QUERY);
	}

	public static List<Status> getByEntityId(Long entityId, Connection connection) throws SQLException {
		return getByRelationsId(entityId, connection, ENTITY_GET_QUERY);
	}

	public static List<Status> getByAutnumId(Long autnumId, Connection connection) throws SQLException {
		return getByRelationsId(autnumId, connection, AUTNUM_GET_QUERY);
	}

	public static List<Status> getByIpNetworkId(Long ipNetworkId, Connection connection) throws SQLException {
		return getByRelationsId(ipNetworkId, connection, IP_NETWORK_GET_QUERY);
	}

	public static List<Status> getByRelationsId(Long id, Connection connection, String getQueryId) throws SQLException {
		List<Status> result = null;
		String query = queryGroup.getQuery(getQueryId);

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, id);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					return Collections.emptyList();
				}
				List<Status> status = new ArrayList<Status>();
				do {
					status.add(Status.getById(resultSet.getInt("sta_id")));
				} while (resultSet.next());
				result = status;
			}
		}

		return result;
	}

}
