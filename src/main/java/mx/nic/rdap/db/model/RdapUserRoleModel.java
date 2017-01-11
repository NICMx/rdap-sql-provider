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
import mx.nic.rdap.db.objects.RdapUserRoleDAO;

/**
 * Model for RdapUserRole Data
 */
public class RdapUserRoleModel {

	private final static Logger logger = Logger.getLogger(RdapUserRoleModel.class.getName());

	private final static String QUERY_GROUP = "RdapUserRole";
	private final static String STORE_QUERY = "storeToDatabase";
	private final static String GET_QUERY = "getByUserName";
	private static QueryGroup queryGroup = null;

	static {
		try {
			queryGroup = new QueryGroup(QUERY_GROUP);
		} catch (IOException e) {
			throw new RuntimeException("Error loading query group");
		}
	}

	/**
	 * Validate the required attributes for the rdapUserRole
	 */
	private static void isValidForStore(RdapUserRoleDAO userRole) throws RequiredValueNotFoundException {
		if (userRole.getUserName() == null || userRole.getUserName().isEmpty())
			throw new RequiredValueNotFoundException("userName", "RdapUserRole");
		if (userRole.getRoleName() == null || userRole.getRoleName().isEmpty())
			throw new RequiredValueNotFoundException("roleName", "RdapUserRole");
	}

	public static void storeRdapUserRoleToDatabase(RdapUserRoleDAO userRole, Connection connection)
			throws RequiredValueNotFoundException, SQLException {
		isValidForStore(userRole);
		String query = queryGroup.getQuery(STORE_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			userRole.storeToDatabase(statement);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();
		}
	}

	public static RdapUserRoleDAO getByUserName(String userName, Connection connection)
			throws SQLException, ObjectNotFoundException {
		String query = queryGroup.getQuery(GET_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, userName);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					throw new ObjectNotFoundException("Object not found.");
				}
				RdapUserRoleDAO userRole = new RdapUserRoleDAO(resultSet);
				return userRole;
			}
		}
	}

}
