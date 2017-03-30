package mx.nic.rdap.db.objects;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import mx.nic.rdap.core.catalog.IpVersion;
import mx.nic.rdap.core.db.IpNetwork;
import mx.nic.rdap.core.ip.AddressBlock;
import mx.nic.rdap.core.ip.IpAddressFormatException;
import mx.nic.rdap.db.model.CountryCodeModel;
import mx.nic.rdap.sql.IpUtils;

/**
 * Data access class for the {@link IpNetwork} object
 */
public class IpNetworkDbObj extends IpNetwork implements DatabaseObject {

	/**
	 * Default Constructor
	 */
	public IpNetworkDbObj() {
		super();
	}

	/**
	 * Constructs an IpNetwork using a {@link ResultSet}
	 */
	public IpNetworkDbObj(ResultSet resultSet) throws SQLException {
		super();
		loadFromDatabase(resultSet);
	}

	@Override
	public void loadFromDatabase(ResultSet rs) throws SQLException {
		setId(rs.getLong("ine_id"));
		setHandle(rs.getString("ine_handle"));
		setParentHandle(rs.getString("ine_parent_handle"));

		IpVersion ipVersion = IpVersion.getByVersionNumber(rs.getInt("ip_version_id"));
		if (ipVersion == null) {
			throw new SQLException("Table value for IP version is invalid: " + rs.getInt("ip_version_id"));
		}

		try {
			InetAddress startAddress = null;
			InetAddress endAddress = null;
			switch (ipVersion) {
			case V4:
				startAddress = IpUtils.numberToInet4(rs.getString("ine_start_address_down"));
				endAddress = IpUtils.numberToInet4(rs.getString("ine_end_address_down"));
				break;
			case V6:
				startAddress = IpUtils.numberToInet6(rs.getString("ine_start_address_up"),
						rs.getString("ine_start_address_down"));
				endAddress = IpUtils.numberToInet6(rs.getString("ine_end_address_up"),
						rs.getString("ine_end_address_down"));
				break;
			}
			setAddressBlock(new AddressBlock(startAddress, endAddress));

		} catch (IpAddressFormatException e) {
			throw new SQLException(e);
		}

		setName(rs.getString("ine_name"));
		setType(rs.getString("ine_type"));
		setCountry(CountryCodeModel.getCountryNameById(rs.getInt("ccd_id")));
		setPort43(rs.getString("ine_port43"));

		int cidr = rs.getInt("ine_cidr");
		
		AddressBlock block = getAddressBlock();
		if (cidr != block.getPrefix()) {
			throw new SQLException("The database's address range does not match the prefix. " //
					+ "start address: " + block.getAddress() //
					+ ", end address: " + block.getLastAddress() //
					+ ", prefix: " + cidr);
		}
	}

	@Override
	public void storeToDatabase(PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setString(1, getHandle());

		InetAddress startAddress = getAddressBlock().getAddress();
		InetAddress endAddress = getAddressBlock().getLastAddress();
		if (startAddress instanceof Inet4Address) {
			preparedStatement.setNull(2, Types.BIGINT);
			preparedStatement.setLong(3, IpUtils.addressToNumber((Inet4Address) startAddress).longValueExact());
			preparedStatement.setNull(4, Types.BIGINT);
			preparedStatement.setLong(5, IpUtils.addressToNumber((Inet4Address) endAddress).longValueExact());
		} else if (startAddress instanceof Inet6Address) {
			preparedStatement.setString(2,
					IpUtils.inet6AddressToUpperPart((Inet6Address) startAddress).toString());
			preparedStatement.setString(3,
					IpUtils.inet6AddressToLowerPart((Inet6Address) startAddress).toString());
			preparedStatement.setString(4, IpUtils.inet6AddressToUpperPart((Inet6Address) endAddress).toString());
			preparedStatement.setString(5, IpUtils.inet6AddressToLowerPart((Inet6Address) endAddress).toString());
		}

		preparedStatement.setString(6, getName());
		preparedStatement.setString(7, getType());
		preparedStatement.setString(8, getPort43());
		preparedStatement.setInt(9, CountryCodeModel.getIdByCountryName(getCountry()));
		preparedStatement.setInt(10, getIpVersion().getVersion());
		preparedStatement.setString(11, getParentHandle());
		preparedStatement.setInt(12, getAddressBlock().getPrefix());
	}

	/**
	 * Same as storeToDatabase,but using different order and should use the
	 * object id as criteria
	 */
	public void updateInDatabase(PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setString(1, getHandle());

		InetAddress startAddress = getAddressBlock().getAddress();
		InetAddress endAddress = getAddressBlock().getLastAddress();
		if (startAddress instanceof Inet4Address) {
			preparedStatement.setNull(2, Types.BIGINT);
			preparedStatement.setLong(3, IpUtils.addressToNumber((Inet4Address) startAddress).longValueExact());
			preparedStatement.setNull(4, Types.BIGINT);
			preparedStatement.setLong(5, IpUtils.addressToNumber((Inet4Address) endAddress).longValueExact());
		} else if (startAddress instanceof Inet6Address) {
			preparedStatement.setString(2,
					IpUtils.inet6AddressToUpperPart((Inet6Address) startAddress).toString());
			preparedStatement.setString(3,
					IpUtils.inet6AddressToLowerPart((Inet6Address) startAddress).toString());
			preparedStatement.setString(4, IpUtils.inet6AddressToUpperPart((Inet6Address) endAddress).toString());
			preparedStatement.setString(5, IpUtils.inet6AddressToLowerPart((Inet6Address) endAddress).toString());
		}

		preparedStatement.setString(6, getName());
		preparedStatement.setString(7, getType());
		preparedStatement.setString(8, getPort43());
		preparedStatement.setInt(9, CountryCodeModel.getIdByCountryName(getCountry()));
		preparedStatement.setInt(10, getIpVersion().getVersion());
		preparedStatement.setString(11, getParentHandle());
		preparedStatement.setInt(12, getAddressBlock().getPrefix());
		preparedStatement.setLong(13, getId());

	}

}
