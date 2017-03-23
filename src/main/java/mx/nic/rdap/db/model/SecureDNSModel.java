package mx.nic.rdap.db.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.db.SecureDNS;
import mx.nic.rdap.db.QueryGroup;
import mx.nic.rdap.db.exception.RequiredValueNotFoundException;
import mx.nic.rdap.db.objects.SecureDNSDbObj;

/**
 * Model for the {@link SecureDNS} object
 * 
 */
public class SecureDNSModel {

	private final static Logger logger = Logger.getLogger(SecureDNSModel.class.getName());

	private final static String QUERY_GROUP = "SecureDNS";
	private final static String STORE_QUERY = "storeToDatabase";
	private final static String GET_QUERY = "getByDomain";

	protected static QueryGroup queryGroup = null;

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

	public static Long storeToDatabase(SecureDNS secureDns, Connection connection)
			throws SQLException, RequiredValueNotFoundException {
		String query = getQueryGroup().getQuery(STORE_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			((SecureDNSDbObj) secureDns).storeToDatabase(statement);
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			statement.executeUpdate();
			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.next();
			Long secureDnsId = resultSet.getLong(1);

			secureDns.setId(secureDnsId);

		}

		DsDataModel.storeAllToDatabase(secureDns.getDsData(), secureDns.getId(), connection);
		KeyDataModel.storeAllToDatabase(secureDns.getKeyData(), secureDns.getId(), connection);

		return secureDns.getId();
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
