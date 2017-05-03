package mx.nic.rdap.store.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.db.RdapUserRole;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.exception.IncompleteObjectException;
import mx.nic.rdap.sql.exception.InvalidObjectException;
import mx.nic.rdap.sql.objects.RdapUserRoleDbObj;

/**
 * Model for RdapUserRole Data
 */
public class RdapUserRoleStoreModel {

	private final static Logger logger = Logger.getLogger(RdapUserRoleStoreModel.class.getName());

	private final static String QUERY_GROUP = "RdapUserRoleStore";
	private final static String STORE_QUERY = "storeToDatabase";
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
			fillPreparedStatement(statement, userRole);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();
		}
	}

	private static void fillPreparedStatement(PreparedStatement preparedStatement, RdapUserRole rdapUserRole)
			throws SQLException {
		preparedStatement.setString(1, rdapUserRole.getUserName());
		preparedStatement.setString(2, rdapUserRole.getRoleName());

	}

}
