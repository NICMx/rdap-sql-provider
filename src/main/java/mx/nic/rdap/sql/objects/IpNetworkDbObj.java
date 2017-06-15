package mx.nic.rdap.sql.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

import mx.nic.rdap.core.catalog.IpVersion;
import mx.nic.rdap.core.db.IpNetwork;
import mx.nic.rdap.core.ip.IpAddressFormatException;
import mx.nic.rdap.sql.IpUtils;
import mx.nic.rdap.sql.model.CountryCodeModel;

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
		setIpVersion(ipVersion);

		try {
			switch (ipVersion) {
			case V4:
				setStartAddress(IpUtils.numberToInet4(rs.getString("ine_start_address_down")));
				setEndAddress(IpUtils.numberToInet4(rs.getString("ine_end_address_down")));
				break;
			case V6:
				setStartAddress(IpUtils.numberToInet6(rs.getString("ine_start_address_up"),
						rs.getString("ine_start_address_down")));
				setEndAddress(IpUtils.numberToInet6(rs.getString("ine_end_address_up"),
						rs.getString("ine_end_address_down")));
				break;
			}
		} catch (IpAddressFormatException e) {
			throw new SQLException(e);
		}

		setName(rs.getString("ine_name"));
		setType(rs.getString("ine_type"));
		setCountry(CountryCodeModel.getCountryNameById(rs.getInt("ccd_id")));
		setPort43(rs.getString("ine_port43"));
		setPrefix(rs.getInt("ine_cidr"));
	}

}
