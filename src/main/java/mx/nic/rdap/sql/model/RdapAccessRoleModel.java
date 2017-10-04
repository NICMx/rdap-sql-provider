package mx.nic.rdap.sql.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.SQLProviderConfiguration;
import mx.nic.rdap.sql.objects.RdapAccessRoleDbObj;

/**
 * Model for RdapAccessRoleModel Data
 */
public class RdapAccessRoleModel {

	private final static Logger logger = Logger.getLogger(RdapAccessRoleModel.class.getName());

	private final static String QUERY_GROUP = "RdapAccessRole";
	private final static String GET_BY_USERNAME_QUERY = "getByUserName";
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

	public static Set<String> getByUserName(String userName, Connection connection) throws SQLException {
		String query = getQueryGroup().getQuery(GET_BY_USERNAME_QUERY);
		if (SQLProviderConfiguration.isUserSQL() && query == null) {
			return null;
		}
		Set<String> accessRoles = new HashSet<String>();
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, userName);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					RdapAccessRoleDbObj accessRole = new RdapAccessRoleDbObj(resultSet);
					accessRoles.add(accessRole.getName());
				}
			}
		}
		return accessRoles;
	}

}
