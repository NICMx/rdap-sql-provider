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

import mx.nic.rdap.core.catalog.Role;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.SQLProviderConfiguration;

/**
 * Model for the {@link Role} objects of nested entities of main objects.
 * 
 */
public class RoleModel {

	private final static Logger logger = Logger.getLogger(RoleModel.class.getName());

	private final static String QUERY_GROUP = "Role";

	private static final String DOMAIN_GET_QUERY = "getDomainRole";
	private static final String ENTITY_GET_QUERY = "getEntityRole";
	private static final String NAMESERVER_GET_QUERY = "getNSRole";
	private static final String AUTNUM_GET_QUERY = "getAutnumRole";
	private static final String IP_NETWORK_GET_QUERY = "getIpNetworkRole";
	private static final String MAIN_ENTITY_GET_QUERY = "getMainEntityRole";

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

	public static List<Role> getDomainEntityRole(Long domainId, Long entityId, Connection connection)
			throws SQLException {
		return getNestedEntityRole(domainId, entityId, connection, DOMAIN_GET_QUERY);
	}

	public static List<Role> getNameserverEntityRole(Long nameserverId, Long entityId, Connection connection)
			throws SQLException {
		return getNestedEntityRole(nameserverId, entityId, connection, NAMESERVER_GET_QUERY);
	}

	public static List<Role> getEntityEntityRole(Long mainEntityId, Long nestedEntityId, Connection connection)
			throws SQLException {
		return getNestedEntityRole(mainEntityId, nestedEntityId, connection, ENTITY_GET_QUERY);
	}

	public static List<Role> getAutnumEntityRole(Long autnumId, Long asnId, Connection connection) throws SQLException {
		return getNestedEntityRole(autnumId, asnId, connection, AUTNUM_GET_QUERY);
	}

	public static List<Role> getIpNetworkEntityRole(Long ipNetworkId, Long entityId, Connection connection)
			throws SQLException {
		return getNestedEntityRole(ipNetworkId, entityId, connection, IP_NETWORK_GET_QUERY);
	}

	private static List<Role> getNestedEntityRole(Long ownerId, Long nestedEntityId, Connection connection,
			String getQuery) throws SQLException {
		String query = getQueryGroup().getQuery(getQuery);
		if (SQLProviderConfiguration.isUserSQL() && query == null) {
			return Collections.emptyList();
		}
		List<Role> roles = null;
		try (PreparedStatement statement = connection.prepareStatement(query);) {
			statement.setLong(1, ownerId);
			statement.setLong(2, nestedEntityId);
			ResultSet rs = statement.executeQuery();

			if (!rs.next()) {
				return Collections.emptyList();
			}

			roles = new ArrayList<>();
			do {
				int roleId = rs.getInt(1);
				if (rs.wasNull()) {
					throw new NullPointerException("Role id was null");
				}
				roles.add(Role.getById(roleId));
			} while (rs.next());
		}

		return roles;
	}

	public static List<Role> getMainEntityRole(Long entId, Connection connection) throws SQLException {
		List<Role> result = null;
		String query = getQueryGroup().getQuery(MAIN_ENTITY_GET_QUERY);
		if (SQLProviderConfiguration.isUserSQL() && query == null) {
			return Collections.emptyList();
		}
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, entId);
			logger.log(Level.FINE, "Executing QUERY: " + statement.toString());
			ResultSet rs = statement.executeQuery();
			if (!rs.next()) {
				return Collections.emptyList();
			}

			result = new ArrayList<>();
			do {
				int roleId = rs.getInt(1);
				if (rs.wasNull()) {
					throw new NullPointerException("Role id was null");
				}
				result.add(Role.getById(roleId));
			} while (rs.next());
		}
		return result;
	}

}
