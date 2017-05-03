package mx.nic.rdap.sql.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.db.IpAddress;
import mx.nic.rdap.core.db.struct.NameserverIpAddressesStruct;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.objects.IpAddressDbObj;

/**
 * Model for the {@link IpAddress} Object
 * 
 */
public class IpAddressModel {

	private final static Logger logger = Logger.getLogger(IpAddressModel.class.getName());

	private final static String QUERY_GROUP = "IpAddress";
	private static final String GET_QUERY = "getByNameserverId";

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

	public static NameserverIpAddressesStruct getIpAddressStructByNameserverId(Long nameserverId, Connection connection)
			throws SQLException {
		String query = getQueryGroup().getQuery(GET_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, nameserverId);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next()) {
				return null;
			}

			// Process the resulset to construct the struct
			NameserverIpAddressesStruct struct = new NameserverIpAddressesStruct();
			do {
				IpAddressDbObj ipAddressDAO = new IpAddressDbObj(resultSet);
				if (ipAddressDAO.getType() == 4) {
					struct.getIpv4Adresses().add(ipAddressDAO);
				} else if (ipAddressDAO.getType() == 6) {
					struct.getIpv6Adresses().add(ipAddressDAO);
				}
			} while (resultSet.next());
			return struct;
		}
	}

}
