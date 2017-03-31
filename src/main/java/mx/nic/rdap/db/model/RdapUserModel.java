package mx.nic.rdap.db.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.db.QueryGroup;
import mx.nic.rdap.db.RdapUserRole;
import mx.nic.rdap.db.exception.IncompleteObjectException;
import mx.nic.rdap.db.exception.InvalidObjectException;
import mx.nic.rdap.db.objects.RdapUserDbObj;
import mx.nic.rdap.db.objects.RdapUserRoleDbObj;

/**
 * Model for RdapUserData
 * 
 */
public class RdapUserModel {

	private final static Logger logger = Logger.getLogger(RdapUserModel.class.getName());

	private final static String QUERY_GROUP = "RdapUser";
	private final static String STORE_QUERY = "storeToDatabase";
	private final static String GET_BY_NAME_QUERY = "getByName";
	private final static String DELETE_ROLES_QUERY = "deleteAllRdapUserRoles";
	private final static String DELETE_QUERY = "deleteAllRdapUsers";

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
	 * Validate the required attributes for the rdapuser
	 * 
	 */
	private static void isValidForStore(RdapUserDbObj user) throws InvalidObjectException {
		if (user.getName() == null || user.getName().isEmpty())
			throw new IncompleteObjectException("name", "RdapUser");
		if (user.getPass() == null || user.getPass().isEmpty())
			throw new IncompleteObjectException("password", "RdapUser");
		if (user.getUserRole().getRoleName() == null || user.getUserRole().getRoleName().isEmpty())
			throw new IncompleteObjectException("role", "RdapUser");
	}

	public static void storeToDatabase(RdapUserDbObj user, Connection connection)
			throws SQLException {
		isValidForStore(user);
		String query = getQueryGroup().getQuery(STORE_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			user.storeToDatabase(statement);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();
		}
		RdapUserRoleModel.storeRdapUserRoleToDatabase((RdapUserRoleDbObj) user.getUserRole(), connection);
	}

	public static RdapUserDbObj getByName(String name, Connection connection)
			throws SQLException {
		String query = getQueryGroup().getQuery(GET_BY_NAME_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, name);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					return null;
				}
				RdapUserDbObj user = new RdapUserDbObj(resultSet);

				RdapUserRole role = RdapUserRoleModel.getByUserName(user.getName(), connection);
				if (role == null) {
					logger.log(Level.WARNING, "User '" + user.getName() + "' has no roles.");
					return null;
				}

				user.setUserRole(role);
				return user;
			}
		}
	}

	/**
	 * Clean the rdapuser and rdapUserRole tables in the migration
	 * 
	 */
	public static void cleanRdapUserDatabase(Connection connection) throws SQLException {
		String query = getQueryGroup().getQuery(DELETE_ROLES_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();

		}
		query = getQueryGroup().getQuery(DELETE_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();

		}
	}

}
