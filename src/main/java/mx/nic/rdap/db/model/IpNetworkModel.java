package mx.nic.rdap.db.model;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.IpUtils;
import mx.nic.rdap.core.catalog.IpVersion;
import mx.nic.rdap.core.db.Entity;
import mx.nic.rdap.core.db.IpNetwork;
import mx.nic.rdap.db.QueryGroup;
import mx.nic.rdap.db.exception.RdapDataAccessException;
import mx.nic.rdap.db.exception.RequiredValueNotFoundException;
import mx.nic.rdap.db.exception.http.BadRequestException;
import mx.nic.rdap.db.objects.IpNetworkDbObj;

/**
 * Model for the {@link IpNetworkModel} Object
 * 
 */
public class IpNetworkModel {

	private final static Logger logger = Logger.getLogger(EntityModel.class.getName());

	private final static String QUERY_GROUP = "IpNetwork";

	protected static QueryGroup queryGroup = null;

	private static final String GET_BY_IPV4 = "getByIPv4";
	private static final String GET_BY_IPV6 = "getByIPv6";
	private static final String STORE_TO_DATABASE = "storeToDatabase";
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

	private static void isValidForStore(IpNetwork ipNetwork) throws RdapDataAccessException {
		if (ipNetwork.getHandle() == null)
			throw new RequiredValueNotFoundException("handle", "IpNetwork");

		if (ipNetwork.getStartAddress() == null) {
			throw new RequiredValueNotFoundException("startAddress", "IpNetwork");
		}

		if (ipNetwork.getEndAddress() == null) {
			throw new RequiredValueNotFoundException("endAddress", "IpNetwork");
		}

		if (ipNetwork.getIpVersion() == null) {
			throw new RequiredValueNotFoundException("ipVersion", "IpNetwork");
		}

		if (ipNetwork.getIpVersion() == IpVersion.V4) {
			if (ipNetwork.getStartAddress() instanceof Inet6Address
					|| ipNetwork.getEndAddress() instanceof Inet6Address) {
				throw new RuntimeException("IP version doesn't match with the IpVersion set to the object");
			}
		} else {
			if (ipNetwork.getStartAddress() instanceof Inet4Address
					|| ipNetwork.getEndAddress() instanceof Inet4Address) {
				throw new RuntimeException("IP version doesn't match with the IpVersion set to the object");
			}

		}

		IpUtils.validateIpCidr(ipNetwork.getStartAddress(), ipNetwork.getCidr());
		IpUtils.validateLastIpCidr(ipNetwork.getEndAddress(), ipNetwork.getCidr());
	}

	public static Long storeToDatabase(IpNetwork ipNetwork, Connection connection)
			throws RdapDataAccessException, SQLException {
		isValidForStore(ipNetwork);
		String query = getQueryGroup().getQuery(STORE_TO_DATABASE);

		Long resultId = null;
		try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
			((IpNetworkDbObj) ipNetwork).storeToDatabase(statement);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();

			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.next();
			resultId = resultSet.getLong(1);
			ipNetwork.setId(resultId);
		}

		storeNestedObjects(ipNetwork, connection);

		return resultId;
	}

	private static void storeNestedObjects(IpNetwork ipNetwork, Connection connection)
			throws SQLException, RequiredValueNotFoundException {
		StatusModel.storeIpNetworkStatusToDatabase(ipNetwork.getStatus(), ipNetwork.getId(), connection);
		RemarkModel.storeIpNetworkRemarksToDatabase(ipNetwork.getRemarks(), ipNetwork.getId(), connection);
		LinkModel.storeIpNetworkLinksToDatabase(ipNetwork.getLinks(), ipNetwork.getId(), connection);
		EventModel.storeIpNetworkEventsToDatabase(ipNetwork.getEvents(), ipNetwork.getId(), connection);
		for (Entity ent : ipNetwork.getEntities()) {
			Long entId = EntityModel.getIdByHandle(ent.getHandle(), connection);
			if (entId == null) {
				throw new NullPointerException(
						"Entity: " + ent.getHandle() + "was not inserted previously to the database");
			}
			ent.setId(entId);
		}
		RoleModel.storeIpNetworkEntityRoles(ipNetwork.getEntities(), ipNetwork.getId(), connection);

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

	private static IpNetwork getByInet4Address(Inet4Address inetAddress, Integer cidr, Connection connection)
			throws SQLException, BadRequestException {
		InetAddress lastAddressFromNetwork = IpUtils.getLastAddressFromNetwork(inetAddress, cidr);

		BigInteger start = IpUtils.addressToNumber(inetAddress);
		BigInteger end = IpUtils.addressToNumber((Inet4Address) lastAddressFromNetwork);

		String query = getQueryGroup().getQuery(GET_BY_IPV4);
		IpNetworkDbObj ipDao = null;
		try (PreparedStatement statement = connection.prepareStatement(query);) {
			statement.setInt(1, cidr);
			statement.setString(2, start.toString());
			statement.setString(3, end.toString());
			ResultSet rs = statement.executeQuery();
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			if (!rs.next()) {
				return null;
			}

			ipDao = new IpNetworkDbObj();
			ipDao.loadFromDatabase(rs);
		}

		return ipDao;
	}

	private static IpNetwork getByInet6Address(Inet6Address inetAddress, Integer cidr, Connection connection)
			throws SQLException, BadRequestException {
		InetAddress lastAddressFromNetwork = IpUtils.getLastAddressFromNetwork(inetAddress, cidr);

		BigInteger startUpperPart = IpUtils.inet6AddressToUpperPart(inetAddress);
		BigInteger startLowerPart = IpUtils.inet6AddressToLowerPart(inetAddress);

		BigInteger endUpperPart = IpUtils.inet6AddressToUpperPart((Inet6Address) lastAddressFromNetwork);
		BigInteger endLowerPart = IpUtils.inet6AddressToLowerPart((Inet6Address) lastAddressFromNetwork);

		String query = getQueryGroup().getQuery(GET_BY_IPV6);
		IpNetworkDbObj ipDao = null;
		try (PreparedStatement statement = connection.prepareStatement(query);) {
			statement.setInt(1, cidr);
			statement.setString(2, startUpperPart.toString());
			statement.setString(3, startLowerPart.toString());

			statement.setString(4, endUpperPart.toString());
			statement.setString(5, endLowerPart.toString());

			ResultSet rs = statement.executeQuery();
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());

			if (!rs.next()) {
				return null;
			}

			ipDao = new IpNetworkDbObj();
			ipDao.loadFromDatabase(rs);
		}

		return ipDao;
	}

	public static IpNetwork getByInetAddress(InetAddress inetAddress, Connection connection)
			throws SQLException, BadRequestException {
		return getByInetAddress(inetAddress, IpUtils.getMaxValidCidr(inetAddress), connection);
	}

	public static IpNetwork getByInetAddress(InetAddress inetAddress, Integer cidr, Connection connection)
			throws SQLException, BadRequestException {
		IpNetwork result = null;
		if (inetAddress instanceof Inet4Address) {
			IpUtils.validateIpv4Cidr(cidr);
			result = getByInet4Address((Inet4Address) inetAddress, cidr, connection);
		} else if (inetAddress instanceof Inet6Address) {
			IpUtils.validateIpv6Cidr(cidr);
			result = getByInet6Address((Inet6Address) inetAddress, cidr, connection);
		} else {
			throw new UnsupportedOperationException("Unsupported class:" + inetAddress.getClass().getName());
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

			result = new IpNetworkDbObj();
			result.loadFromDatabase(rs);
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
				IpNetworkDbObj ipNetwork = new IpNetworkDbObj();
				ipNetwork.loadFromDatabase(rs);
				results.add(ipNetwork);
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

			result = new IpNetworkDbObj();
			result.loadFromDatabase(rs);
		}

		loadNestedObjects(result, connection);

		return result;
	}

}
