package mx.nic.rdap.store.model;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.catalog.IpVersion;
import mx.nic.rdap.core.db.Entity;
import mx.nic.rdap.core.db.IpNetwork;
import mx.nic.rdap.core.ip.AddressBlock;
import mx.nic.rdap.core.ip.IpAddressFormatException;
import mx.nic.rdap.sql.IpUtils;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.exception.IncompleteObjectException;
import mx.nic.rdap.sql.exception.InvalidObjectException;
import mx.nic.rdap.sql.model.CountryCodeModel;
import mx.nic.rdap.sql.model.IpNetworkModel;
import mx.nic.rdap.sql.objects.IpNetworkDbObj;

/**
 * Model for the {@link IpNetworkStoreModel} Object
 * 
 */
public class IpNetworkStoreModel {

	private final static Logger logger = Logger.getLogger(EntityStoreModel.class.getName());

	private final static String QUERY_GROUP = "IpNetworkStore";

	private static QueryGroup queryGroup = null;

	private static final String STORE_TO_DATABASE = "storeToDatabase";
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

	private static void isValidForStore(IpNetwork ipNetwork) throws InvalidObjectException {
		if (ipNetwork.getHandle() == null)
			throw new IncompleteObjectException("handle", "IpNetwork");

		if (ipNetwork.getStartAddress() == null) {
			throw new IncompleteObjectException("startAddress", "IpNetwork");
		}

		if (ipNetwork.getEndAddress() == null) {
			throw new IncompleteObjectException("endAddress", "IpNetwork");
		}

		if (ipNetwork.getIpVersion() == null) {
			throw new IncompleteObjectException("ipVersion", "IpNetwork");
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

		AddressBlock block;
		try {
			block = new AddressBlock(ipNetwork.getStartAddress(), ipNetwork.getPrefix());
		} catch (IpAddressFormatException e) {
			throw new InvalidObjectException(e.getMessage(), e);
		}
		if (!block.getLastAddress().equals(ipNetwork.getEndAddress())) {
			throw new InvalidObjectException(ipNetwork.getEndAddress() + " is not the last address of " + block);
		}
	}

	public static Long storeToDatabase(IpNetwork ipNetwork, Connection connection) throws SQLException {
		isValidForStore(ipNetwork);
		String query = getQueryGroup().getQuery(STORE_TO_DATABASE);

		Long resultId = null;
		try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
			fillPreparedStatement(statement, ipNetwork);
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

	private static void storeNestedObjects(IpNetwork ipNetwork, Connection connection) throws SQLException {
		StatusStoreModel.storeIpNetworkStatusToDatabase(ipNetwork.getStatus(), ipNetwork.getId(), connection);
		RemarkStoreModel.storeIpNetworkRemarksToDatabase(ipNetwork.getRemarks(), ipNetwork.getId(), connection);
		LinkStoreModel.storeIpNetworkLinksToDatabase(ipNetwork.getLinks(), ipNetwork.getId(), connection);
		EventStoreModel.storeIpNetworkEventsToDatabase(ipNetwork.getEvents(), ipNetwork.getId(), connection);
		for (Entity ent : ipNetwork.getEntities()) {
			Long entId = EntityStoreModel.getIdByHandle(ent.getHandle(), connection);
			if (entId == null) {
				throw new NullPointerException(
						"Entity: " + ent.getHandle() + "was not inserted previously to the database");
			}
			ent.setId(entId);
		}
		RoleStoreModel.storeIpNetworkEntityRoles(ipNetwork.getEntities(), ipNetwork.getId(), connection);

	}

	private static void fillPreparedStatement(PreparedStatement preparedStatement, IpNetwork ipNetwork)
			throws SQLException {
		preparedStatement.setString(1, ipNetwork.getHandle());
		if (ipNetwork.getStartAddress() instanceof Inet4Address) {
			preparedStatement.setNull(2, Types.BIGINT);
			preparedStatement.setLong(3,
					IpUtils.addressToNumber((Inet4Address) ipNetwork.getStartAddress()).longValueExact());
			preparedStatement.setNull(4, Types.BIGINT);
			preparedStatement.setLong(5,
					IpUtils.addressToNumber((Inet4Address) ipNetwork.getEndAddress()).longValueExact());
		} else if (ipNetwork.getStartAddress() instanceof Inet6Address) {
			preparedStatement.setString(2,
					IpUtils.inet6AddressToUpperPart((Inet6Address) ipNetwork.getStartAddress()).toString());
			preparedStatement.setString(3,
					IpUtils.inet6AddressToLowerPart((Inet6Address) ipNetwork.getStartAddress()).toString());
			preparedStatement.setString(4,
					IpUtils.inet6AddressToUpperPart((Inet6Address) ipNetwork.getEndAddress()).toString());
			preparedStatement.setString(5,
					IpUtils.inet6AddressToLowerPart((Inet6Address) ipNetwork.getEndAddress()).toString());
		}

		preparedStatement.setString(6, ipNetwork.getName());
		preparedStatement.setString(7, ipNetwork.getType());
		preparedStatement.setString(8, ipNetwork.getPort43());
		preparedStatement.setInt(9, CountryCodeModel.getIdByCountryName(ipNetwork.getCountry()));
		preparedStatement.setInt(10, ipNetwork.getIpVersion().getVersion());
		preparedStatement.setString(11, ipNetwork.getParentHandle());
		preparedStatement.setInt(12, ipNetwork.getPrefix());
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

		IpNetworkModel.loadNestedObjects(result, connection);

		return result;
	}
}
