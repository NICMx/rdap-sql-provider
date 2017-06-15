package mx.nic.rdap.store.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.db.SecureDNS;
import mx.nic.rdap.sql.QueryGroup;

/**
 * Model for the {@link SecureDNS} object
 * 
 */
public class SecureDNSStoreModel {

	private final static Logger logger = Logger.getLogger(SecureDNSStoreModel.class.getName());

	private final static String QUERY_GROUP = "SecureDNSStore";
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

	public static Long storeToDatabase(SecureDNS secureDns, Connection connection) throws SQLException {
		String query = getQueryGroup().getQuery(STORE_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			fillPreparedStatement(statement, secureDns);
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			statement.executeUpdate();
			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.next();
			Long secureDnsId = resultSet.getLong(1);

			secureDns.setId(secureDnsId);

		}

		DsDataStoreModel.storeAllToDatabase(secureDns.getDsData(), secureDns.getId(), connection);
		KeyDataStoreModel.storeAllToDatabase(secureDns.getKeyData(), secureDns.getId(), connection);

		return secureDns.getId();
	}

	private static void fillPreparedStatement(PreparedStatement preparedStatement, SecureDNS securedDNS)
			throws SQLException {
		preparedStatement.setBoolean(1, securedDNS.getZoneSigned());
		preparedStatement.setBoolean(2, securedDNS.getDelegationSigned());
		if (securedDNS.getMaxSigLife() == null) {
			preparedStatement.setNull(3, Types.INTEGER);
		} else {
			preparedStatement.setInt(3, securedDNS.getMaxSigLife());
		}
		preparedStatement.setLong(4, securedDNS.getDomainId());

	}

}
