package mx.nic.rdap.sql.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

import mx.nic.rdap.core.db.KeyData;

/**
 * Data access class for the {@link KeyData} Object.
 * 
 */
public class KeyDataDbObj extends KeyData implements DatabaseObject {

	/**
	 * Default constructor
	 */
	public KeyDataDbObj() {
		super();
	}

	/**
	 * Construct KeyData using a {@link ResultSet}
	 */
	public KeyDataDbObj(ResultSet resultSet) throws SQLException {
		super();
		loadFromDatabase(resultSet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mx.nic.rdap.server.db.DatabaseObject#loadFromDatabase(java.sql.ResultSet)
	 */
	@Override
	public void loadFromDatabase(ResultSet resultSet) throws SQLException {
		this.setId(resultSet.getLong("kd_id"));

		this.setFlags(resultSet.getInt("kd_flags"));
		this.setProtocol(resultSet.getInt("kd_protocol"));
		this.setPublicKey(resultSet.getString("kd_public_key"));
		this.setAlgorithm(resultSet.getInt("kd_algorithm"));
		this.setSecureDNSId(resultSet.getLong("sdns_id"));
	}

}
