package mx.nic.rdap.store.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.db.RdapAccessRole;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.exception.IncompleteObjectException;
import mx.nic.rdap.sql.exception.InvalidObjectException;
import mx.nic.rdap.sql.objects.RdapAccessRoleDbObj;

/**
 * Model for RdapAccessRole Data
 */
public class RdapAccessRoleStoreModel {

	private final static Logger logger = Logger.getLogger(RdapAccessRoleStoreModel.class.getName());

	private final static String QUERY_GROUP = "RdapAccessRoleStore";
	private final static String STORE_QUERY = "storeToDatabase";
	private final static String STORE_USER_ACCES_ROLE_QUERY = "storeUserAccessRoleToDatabase";
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

	/**
	 * Validate the required attributes for the rdapAccessRole
	 */
	private static void isValidForStore(RdapAccessRoleDbObj accessRole) throws InvalidObjectException {
		if (accessRole.getName() == null || accessRole.getName().isEmpty())
			throw new IncompleteObjectException("name", "RdapAccessRole");
		if (accessRole.getDescription() == null || accessRole.getDescription().isEmpty())
			throw new IncompleteObjectException("description", "RdapAccessRole");
	}

	public static void storeRdapAccessRoleToDatabase(RdapAccessRoleDbObj accessRole, Connection connection)
			throws SQLException {
		isValidForStore(accessRole);
		String query = getQueryGroup().getQuery(STORE_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			fillPreparedStatement(statement, accessRole);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();
		}
	}

	private static void fillPreparedStatement(PreparedStatement preparedStatement, RdapAccessRole rdapAccessRole)
			throws SQLException {
		preparedStatement.setString(1, rdapAccessRole.getName());
		preparedStatement.setString(2, rdapAccessRole.getDescription());
	}

	public static void storeUserAccessRolesToDatabase(String userName, Set<String> accessRoles, Connection connection)
		throws SQLException {
		if (accessRoles != null && !accessRoles.isEmpty()) {
			String query = getQueryGroup().getQuery(STORE_USER_ACCES_ROLE_QUERY);
			try (PreparedStatement statement = connection.prepareStatement(query);) {
				statement.setString(1, userName);
				for (String accessRole : accessRoles) {
					statement.setString(2, accessRole);
					logger.log(Level.INFO, "Executing QUERY" + statement.toString());
					statement.execute();
				}
			}
		}
	}

}
