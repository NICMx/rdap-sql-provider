package mx.nic.rdap.sql.objects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mx.nic.rdap.core.db.DsData;

/**
 * Data access class for the {@link DsData} Object.
 * 
 */
public class DsDataDbObj extends DsData implements DatabaseObject {

	/**
	 * Default constructor
	 */
	public DsDataDbObj() {
		super();
	}

	/**
	 * Construct DsData using a {@link ResultSet}
	 */
	public DsDataDbObj(ResultSet resultSet) throws SQLException {
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
		this.setId(resultSet.getLong("dsd_id"));
		this.setKeytag(resultSet.getInt("dsd_keytag"));
		this.setAlgorithm(resultSet.getInt("dsd_algorithm"));
		this.setDigest(resultSet.getString("dsd_digest"));
		this.setDigestType(resultSet.getInt("dsd_digest_type"));
		this.setSecureDNSId(resultSet.getLong("sdns_id"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mx.nic.rdap.server.db.DatabaseObject#storeToDatabase(java.sql.
	 * PreparedStatement)
	 */
	@Override
	public void storeToDatabase(PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setLong(1, this.getSecureDNSId());
		preparedStatement.setInt(2, this.getKeytag());
		preparedStatement.setInt(3, this.getAlgorithm());
		preparedStatement.setString(4, this.getDigest());
		preparedStatement.setInt(5, this.getDigestType());

	}

}
