package mx.nic.rdap.db.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.db.QueryGroup;
import mx.nic.rdap.db.exception.ObjectNotFoundException;
import mx.nic.rdap.db.exception.RequiredValueNotFoundException;
import mx.nic.rdap.db.objects.RdapUserDAO;
import mx.nic.rdap.db.objects.RdapUserRoleDAO;

/**
 * Model for RdapUserData
 * 
 */
public class RdapUserModel {

	private final static Logger logger = Logger.getLogger(RdapUserModel.class.getName());

	private final static String QUERY_GROUP = "RdapUser";
	private final static String GET_MAX_RESULTS_QUERY = "getMaxSearchResults";
	private final static String STORE_QUERY = "storeToDatabase";
	private final static String GET_BY_NAME_QUERY = "getByName";
	private final static String DELETE_ROLES_QUERY = "deleteAllRdapUserRoles";
	private final static String DELETE_QUERY = "deleteAllRdapUsers";

	private static QueryGroup queryGroup = null;

	static {
		try {
			queryGroup = new QueryGroup(QUERY_GROUP);
		} catch (IOException e) {
			throw new RuntimeException("Error loading query group");
		}
	}

	/**
	 * Find the max search results for the autheticatedUser
	 * 
	 */
	public static Integer getMaxSearchResultsForAuthenticatedUser(String username, Connection connection)
			throws SQLException {
		String query = queryGroup.getQuery(GET_MAX_RESULTS_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, username);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					return null;
				}
				return resultSet.getInt(1);
			}
		}
	}

	/**
	 * Validate the required attributes for the rdapuser
	 * 
	 */
	private static void isValidForStore(RdapUserDAO user) throws RequiredValueNotFoundException {
		if (user.getName() == null || user.getName().isEmpty())
			throw new RequiredValueNotFoundException("name", "RdapUser");
		if (user.getPass() == null || user.getPass().isEmpty())
			throw new RequiredValueNotFoundException("password", "RdapUser");
		if (user.getUserRole().getRoleName() == null || user.getUserRole().getRoleName().isEmpty())
			throw new RequiredValueNotFoundException("role", "RdapUser");
	}

	public static void storeToDatabase(RdapUserDAO user, Connection connection)
			throws SQLException, RequiredValueNotFoundException {
		isValidForStore(user);
		String query = queryGroup.getQuery(STORE_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			user.storeToDatabase(statement);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();
		}
		RdapUserRoleModel.storeRdapUserRoleToDatabase((RdapUserRoleDAO) user.getUserRole(), connection);
	}

	public static RdapUserDAO getByName(String name, Connection connection)
			throws SQLException, ObjectNotFoundException {
		String query = queryGroup.getQuery(GET_BY_NAME_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, name);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					throw new ObjectNotFoundException("Object not found.");
				}
				RdapUserDAO user = new RdapUserDAO(resultSet);
				user.setUserRole(RdapUserRoleModel.getByUserName(user.getName(), connection));
				return user;
			}
		}
	}

	/**
	 * Clean the rdapuser and rdapUserRole tables in the migration
	 * 
	 */
	public static void cleanRdapUserDatabase(Connection connection) throws SQLException {
		String query = queryGroup.getQuery(DELETE_ROLES_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();

		}
		query = queryGroup.getQuery(DELETE_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();

		}
	}

}
