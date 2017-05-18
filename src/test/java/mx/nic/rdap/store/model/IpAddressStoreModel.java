package mx.nic.rdap.store.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.db.IpAddress;
import mx.nic.rdap.core.db.struct.NameserverIpAddressesStruct;
import mx.nic.rdap.sql.QueryGroup;

/**
 * Model for the {@link IpAddress} Object
 * 
 */
public class IpAddressStoreModel {

	private final static Logger logger = Logger.getLogger(IpAddressStoreModel.class.getName());

	private final static String QUERY_GROUP = "IpAddressStore";
	private static final String STORE_QUERY = "storeToDatabase";

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

	public static void storeToDatabase(NameserverIpAddressesStruct struct, long nameserverId, Connection connection)
			throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery(STORE_QUERY),
				Statement.RETURN_GENERATED_KEYS)) {
			for (IpAddress addressV4 : struct.getIpv4Adresses()) {
				addressV4.setNameserverId(nameserverId);
				fillPreparedStatement(statement, addressV4);
				logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
				statement.executeUpdate();
				ResultSet generatedKeys = statement.getGeneratedKeys();
				generatedKeys.next();
				long id = generatedKeys.getLong(1);
				addressV4.setId(id);
			}
			for (IpAddress addressV6 : struct.getIpv6Adresses()) {
				addressV6.setNameserverId(nameserverId);
				fillPreparedStatement(statement, addressV6);
				logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
				statement.executeUpdate();
				ResultSet generatedKeys = statement.getGeneratedKeys();
				generatedKeys.next();
				long id = generatedKeys.getLong(1);
				addressV6.setId(id);
			}
		}
	}

	private static void fillPreparedStatement(PreparedStatement preparedStatement, IpAddress ipAddress)
			throws SQLException {
		preparedStatement.setLong(1, ipAddress.getNameserverId());
		preparedStatement.setInt(2, ipAddress.getType());
		// To store the ipv6,use an if clause, the third parameter is the type
		// to compare if is a ipv4 or a ipv6
		preparedStatement.setInt(3, ipAddress.getType());
		preparedStatement.setString(4, ipAddress.getAddress().getHostAddress());
		preparedStatement.setString(5, ipAddress.getAddress().getHostAddress());

	}

}
