package mx.nic.rdap.sql.objects;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mx.nic.rdap.core.db.IpAddress;
import mx.nic.rdap.core.db.IpNetwork;

/**
 * Data access class for the {@link IpAddress} Object.Object representig an IpAddress,
 * different to {@link IpNetwork}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see mx.nic.rdap.core.db.DatabaseObject#storeToDatabase(java.sql.
	 * PreparedStatement)
	 */
	@Override
	public void storeToDatabase(PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setLong(1, this.getNameserverId());
		preparedStatement.setInt(2, this.getType());
		// To store the ipv6,use an if clause, the third parameter is the type to compare if is a ipv4 or a ipv6
		preparedStatement.setInt(3, this.getType());
		preparedStatement.setString(4, this.getAddress().getHostAddress());
		preparedStatement.setString(5, this.getAddress().getHostAddress());

	}

}
