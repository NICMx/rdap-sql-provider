package mx.nic.rdap.sql.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.db.SecureDNS;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.objects.SecureDNSDbObj;

/**
 * Model for the {@link SecureDNS} object
 * 
 */
public class SecureDNSModel {

	private final static Logger logger = Logger.getLogger(SecureDNSModel.class.getName());

	private final static String QUERY_GROUP = "SecureDNS";
	private final static String GET_QUERY = "getByDomain";

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

	public static SecureDNS getByDomain(Long domainId, Connection connection) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery(GET_QUERY));) {
			statement.setLong(1, domainId);
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next()) {
				return null;
			}

			SecureDNSDbObj secureDns = new SecureDNSDbObj(resultSet);
			secureDns.setDsData(DsDataModel.getBySecureDnsId(secureDns.getId(), connection));
			secureDns.setKeyData(KeyDataModel.getBySecureDnsId(secureDns.getId(), connection));
			return secureDns;
		}
	}
}
