package mx.nic.rdap.store.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.catalog.Role;
import mx.nic.rdap.core.db.Entity;
import mx.nic.rdap.sql.QueryGroup;

/**
 * Model for the {@link Role} objects of nested entities of main objects.
 * 
 */
public class RoleStoreModel {

	private final static Logger logger = Logger.getLogger(RoleStoreModel.class.getName());

	private final static String QUERY_GROUP = "RoleStore";

	private static final String DOMAIN_STORE_QUERY = "storeDomainsEntityRol";
	private static final String ENTITY_STORE_QUERY = "storeEntitiesEntityRol";
	private static final String NAMESERVER_STORE_QUERY = "storeNSEntityRol";
	private static final String AUTNUM_STORE_QUERY = "storeAutnumEntityRol";
	private static final String IP_NETWORK_STORE_ROLES = "storeIpNetworkEntityRol";
	private static final String MAIN_ENTITY_STORE_QUERY = "storeMainEntityRole";

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

	public static void storeEntityEntityRoles(List<Entity> entities, Long entityId, Connection connection)
			throws SQLException {
		storeEntitiesRoles(entities, entityId, connection, ENTITY_STORE_QUERY);
	}

	public static void storeNameserverEntityRoles(List<Entity> entities, Long nsId, Connection connection)
			throws SQLException {
		storeEntitiesRoles(entities, nsId, connection, NAMESERVER_STORE_QUERY);
	}

	public static void storeDomainEntityRoles(List<Entity> entities, Long domainId, Connection connection)
			throws SQLException {
		storeEntitiesRoles(entities, domainId, connection, DOMAIN_STORE_QUERY);
	}

	public static void storeAutnumEntityRoles(List<Entity> entities, Long autnumId, Connection connection)
			throws SQLException {
		storeEntitiesRoles(entities, autnumId, connection, AUTNUM_STORE_QUERY);
	}

	public static void storeIpNetworkEntityRoles(List<Entity> entities, Long ipNetworkId, Connection connection)
			throws SQLException {
		storeEntitiesRoles(entities, ipNetworkId, connection, IP_NETWORK_STORE_ROLES);
	}

	private static void storeEntitiesRoles(List<Entity> entities, Long ownerId, Connection connection,
			String storeQuery) throws SQLException {
		if (entities.isEmpty())
			return;

		String query = getQueryGroup().getQuery(storeQuery);

		try (PreparedStatement statement = connection.prepareStatement(query);) {
			statement.setLong(1, ownerId);
			for (Entity entity : entities) {
				statement.setLong(2, entity.getId());
				for (Role role : entity.getRoles()) {
					statement.setLong(3, role.getId());
					logger.log(Level.INFO, "Executing QUERY" + statement.toString());
					statement.execute();
				}
			}
		}
	}

	public static void storeMainEntityRol(Entity mainEntity, Connection connection) throws SQLException {
		if (mainEntity.getRoles().isEmpty()) {
			return;
		}

		String query = getQueryGroup().getQuery(MAIN_ENTITY_STORE_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query);) {
			statement.setLong(1, mainEntity.getId());
			for (Role role : mainEntity.getRoles()) {
				statement.setInt(2, role.getId());
				logger.log(Level.INFO, "Executing QUERY" + statement.toString());
				statement.execute();
			}
		}
	}

}
