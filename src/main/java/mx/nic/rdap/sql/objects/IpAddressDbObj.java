package mx.nic.rdap.sql.objects;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;

import mx.nic.rdap.core.db.IpAddress;
import mx.nic.rdap.core.db.IpNetwork;

/**
 * Data access class for the {@link IpAddress} Object.Object representig an
 * IpAddress, different to {@link IpNetwork}
 * 
 */
public class IpAddressDbObj extends IpAddress implements DatabaseObject {

	/**
	 * Default Constructor
	 */
	public IpAddressDbObj() {
		super();
	}

	/**
	 * Construct an IpAddressDAO using a {@link ResultSet}
	 * 
	 */
	public IpAddressDbObj(ResultSet resultSet) throws SQLException {
		super();
		loadFromDatabase(resultSet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mx.nic.rdap.core.db.DatabaseObject#loadFromDatabase(java.sql.ResultSet)
	 */
	@Override
	public void loadFromDatabase(ResultSet resultSet) throws SQLException {
		this.setId(resultSet.getLong("iad_id"));
		this.setNameserverId(resultSet.getLong("nse_id"));
		try {
			this.setAddress(InetAddress.getByName(resultSet.getString("iad_value")));
		} catch (UnknownHostException e) {
			// The address is validate in the store process,so this exception
			// will never be throw.
		}
		if (this.getAddress() instanceof Inet4Address) {
			this.setType(4);
		} else if (this.getAddress() instanceof Inet6Address) {
			this.setType(6);
		}

	}

}
