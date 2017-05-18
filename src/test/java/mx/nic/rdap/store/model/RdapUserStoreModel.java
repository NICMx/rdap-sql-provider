package mx.nic.rdap.store.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.db.RdapUser;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.exception.IncompleteObjectException;
import mx.nic.rdap.sql.exception.InvalidObjectException;
import mx.nic.rdap.sql.objects.RdapUserDbObj;
import mx.nic.rdap.sql.objects.RdapUserRoleDbObj;

/**
 * Model for RdapUserData
 * 
 */
public class RdapUserStoreModel {

	private final static Logger logger = Logger.getLogger(RdapUserStoreModel.class.getName());

	private final static String QUERY_GROUP = "RdapUserStore";
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

	public static void storeToDatabase(RdapUserDbObj user, Connection connection) throws SQLException {
		isValidForStore(user);
		String query = getQueryGroup().getQuery(STORE_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			fillPreparedStatement(statement, user);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();
		}
		RdapUserRoleStoreModel.storeRdapUserRoleToDatabase((RdapUserRoleDbObj) user.getUserRole(), connection);
	}

	private static void fillPreparedStatement(PreparedStatement preparedStatement, RdapUser rdapUser)
			throws SQLException {
		preparedStatement.setString(1, rdapUser.getName());
		preparedStatement.setString(2, rdapUser.getPass());
		if (rdapUser.getMaxSearchResults() != null)
			preparedStatement.setInt(3, rdapUser.getMaxSearchResults());
		else
			preparedStatement.setNull(3, java.sql.Types.INTEGER);
	}
}
