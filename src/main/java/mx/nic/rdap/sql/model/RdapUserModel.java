package mx.nic.rdap.sql.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.db.RdapUserRole;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.SQLProviderConfiguration;
import mx.nic.rdap.sql.objects.RdapUserDbObj;

/**
 * Model for RdapUserData
 * 
 */
public class RdapUserModel {

	private final static Logger logger = Logger.getLogger(RdapUserModel.class.getName());

	private final static String QUERY_GROUP = "RdapUser";
	private final static String GET_BY_NAME_QUERY = "getByName";

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

	public static RdapUserDbObj getByName(String name, Connection connection) throws SQLException {
		String query = getQueryGroup().getQuery(GET_BY_NAME_QUERY);
		if (SQLProviderConfiguration.isUserSQL() && query == null) {
			return null;
		}
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

}
