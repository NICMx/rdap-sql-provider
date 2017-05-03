package mx.nic.rdap.sql.model;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.db.IpNetwork;
import mx.nic.rdap.core.ip.AddressBlock;
import mx.nic.rdap.sql.IpUtils;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.objects.IpNetworkDbObj;

/**
 * Model for the {@link IpNetworkModel} Object
 * 
 */
public class IpNetworkModel {

	private final static Logger logger = Logger.getLogger(EntityModel.class.getName());

	private final static String QUERY_GROUP = "IpNetwork";

	private static QueryGroup queryGroup = null;

	private static final String GET_BY_IPV4 = "getByIPv4";
	private static final String GET_BY_IPV6 = "getByIPv6";
	private static final String GET_BY_ENTITY_ID = "getByEntityId";
	private static final String GET_BY_DOMAIN_ID = "getByDomainId";
	private static final String GET_BY_HANDLE = "getByHandle";

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

	private static void loadNestedObjects(IpNetwork ipNetwork, Connection connection) throws SQLException {
		Long ipNetworkId = ipNetwork.getId();

		loadSimpleNestedObjects(ipNetwork, connection);

		// Retrieve the entities
		ipNetwork.getEntities().addAll(EntityModel.getEntitiesByIpNetworkId(ipNetworkId, connection));

	}

	private static void loadSimpleNestedObjects(IpNetwork ipNetwork, Connection connection) throws SQLException {
		Long ipNetworkId = ipNetwork.getId();

		// Retrieve the events
		ipNetwork.getEvents().addAll(EventModel.getByIpNetworkId(ipNetworkId, connection));

		// Retrieve the links
		ipNetwork.getLinks().addAll(LinkModel.getByIpNetworkId(ipNetworkId, connection));

		// Retrieve the status
		ipNetwork.getStatus().addAll(StatusModel.getByIpNetworkId(ipNetworkId, connection));

		// Retrieve the remarks
		ipNetwork.getRemarks().addAll(RemarkModel.getByIpNetworkId(ipNetworkId, connection));
	}

	private static IpNetwork getByIpv4Block(AddressBlock block, Connection connection) throws SQLException {
		InetAddress lastAddressFromNetwork = block.getLastAddress();

		BigInteger start = IpUtils.addressToNumber((Inet4Address) block.getAddress());
		BigInteger end = IpUtils.addressToNumber((Inet4Address) lastAddressFromNetwork);

		String query = getQueryGroup().getQuery(GET_BY_IPV4);
		IpNetworkDbObj ipDao = null;
		try (PreparedStatement statement = connection.prepareStatement(query);) {
			statement.setInt(1, block.getPrefix());
			statement.setString(2, start.toString());
			statement.setString(3, end.toString());
			ResultSet rs = statement.executeQuery();
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			if (!rs.next()) {
				return null;
			}

			ipDao = new IpNetworkDbObj(rs);
		}

		return ipDao;
	}

	private static IpNetwork getByIpv6Block(AddressBlock block, Connection connection) throws SQLException {
		InetAddress lastAddressFromNetwork = block.getLastAddress();

		BigInteger startUpperPart = IpUtils.inet6AddressToUpperPart((Inet6Address) block.getAddress());
		BigInteger startLowerPart = IpUtils.inet6AddressToLowerPart((Inet6Address) block.getAddress());

		BigInteger endUpperPart = IpUtils.inet6AddressToUpperPart((Inet6Address) lastAddressFromNetwork);
		BigInteger endLowerPart = IpUtils.inet6AddressToLowerPart((Inet6Address) lastAddressFromNetwork);

		String query = getQueryGroup().getQuery(GET_BY_IPV6);
		IpNetworkDbObj ipDao = null;
		try (PreparedStatement statement = connection.prepareStatement(query);) {
			statement.setInt(1, block.getPrefix());
			statement.setString(2, startUpperPart.toString());
			statement.setString(3, startLowerPart.toString());

			statement.setString(4, endUpperPart.toString());
			statement.setString(5, endLowerPart.toString());

			ResultSet rs = statement.executeQuery();
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());

			if (!rs.next()) {
				return null;
			}

			ipDao = new IpNetworkDbObj(rs);
		}

		return ipDao;
	}

	public static IpNetwork getByAddressBlock(AddressBlock block, Connection connection) throws SQLException {
		IpNetwork result = null;
		if (block.getAddress() instanceof Inet4Address) {
			result = getByIpv4Block(block, connection);
		} else if (block.getAddress() instanceof Inet6Address) {
			result = getByIpv6Block(block, connection);
		} else {
			throw new UnsupportedOperationException("Unsupported class:" + block.getAddress().getClass().getName());
		}

		if (result == null) {
			return result;
		}
		loadNestedObjects(result, connection);
		return result;
	}

	public static IpNetwork getByDomainId(long domainId, Connection connection) throws SQLException {
		String query = getQueryGroup().getQuery(GET_BY_DOMAIN_ID);
		IpNetworkDbObj result = null;
		try (PreparedStatement statement = connection.prepareStatement(query);) {
			statement.setLong(1, domainId);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			ResultSet rs = statement.executeQuery();
			if (!rs.next()) {
				return null;
			}

			result = new IpNetworkDbObj(rs);
			loadSimpleNestedObjects(result, connection);
		}

		return result;
	}

	public static List<IpNetwork> getByEntityId(long entityId, Connection connection) throws SQLException {
		return getByRdapObjectId(entityId, connection, GET_BY_ENTITY_ID);
	}

	private static List<IpNetwork> getByRdapObjectId(long id, Connection connection, String getQueryId)
			throws SQLException {
		String query = getQueryGroup().getQuery(getQueryId);
		List<IpNetwork> results = null;
		try (PreparedStatement statement = connection.prepareStatement(query);) {
			statement.setLong(1, id);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			ResultSet rs = statement.executeQuery();
			if (!rs.next()) {
				return Collections.emptyList();
			}

			results = new ArrayList<IpNetwork>();

			do {
				results.add(new IpNetworkDbObj(rs));
			} while (rs.next());
		}

		for (IpNetwork ip : results) {
			loadSimpleNestedObjects(ip, connection);
		}

		return results;
	}

	public static IpNetworkDbObj getByHandle(String handle, Connection connection) throws SQLException {
		String query = getQueryGroup().getQuery(GET_BY_HANDLE);
		IpNetworkDbObj result = null;
		try (PreparedStatement statement = connection.prepareStatement(query);) {
			statement.setString(1, handle);
			logger.log(Level.INFO, "Executing QUERY : " + statement.toString());
			ResultSet rs = statement.executeQuery();
			if (!rs.next()) {
				return null;
			}

			result = new IpNetworkDbObj(rs);
		}

		loadNestedObjects(result, connection);

		return result;
	}

}
