package mx.nic.rdap.db.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.db.QueryGroup;
import mx.nic.rdap.db.exception.IncompleteObjectException;
import mx.nic.rdap.db.exception.InvalidObjectException;
import mx.nic.rdap.db.objects.RdapUserRoleDbObj;

/**
 * Model for RdapUserRole Data
 */
public class RdapUserRoleModel {

	private final static Logger logger = Logger.getLogger(RdapUserRoleModel.class.getName());

	private final static String QUERY_GROUP = "RdapUserRole";
	private final static String STORE_QUERY = "storeToDatabase";
	private final static String GET_QUERY = "getByUserName";
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
	 * Validate the required attributes for the rdapUserRole
	 */
	private static void isValidForStore(RdapUserRoleDbObj userRole) throws InvalidObjectException {
		if (userRole.getUserName() == null || userRole.getUserName().isEmpty())
			throw new IncompleteObjectException("userName", "RdapUserRole");
		if (userRole.getRoleName() == null || userRole.getRoleName().isEmpty())
			throw new IncompleteObjectException("roleName", "RdapUserRole");
	}

	public static void storeRdapUserRoleToDatabase(RdapUserRoleDbObj userRole, Connection connection)
			throws SQLException {
		isValidForStore(userRole);
		String query = getQueryGroup().getQuery(STORE_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			userRole.storeToDatabase(statement);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();
		}
	}

	public static RdapUserRoleDbObj getByUserName(String userName, Connection connection) throws SQLException {
		String query = getQueryGroup().getQuery(GET_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, userName);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					return null;
				}
				RdapUserRoleDbObj userRole = new RdapUserRoleDbObj(resultSet);
				return userRole;
			}
		}
	}

}
