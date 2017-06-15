package mx.nic.rdap.sql.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

import mx.nic.rdap.core.db.Nameserver;

/**
 * Data access class for the {@link Nameserver} object.
 * 
 */
public class NameserverDbObj extends Nameserver implements DatabaseObject {

	/**
	 * Constructor default
	 */
	public NameserverDbObj() {
		super();
	}

	/**
	 * Contruct a NameserverDAO using a {@link ResultSet}
	 * 
	 */
	public NameserverDbObj(ResultSet resultSet) throws SQLException {
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
		this.setId(resultSet.getLong("nse_id"));
		this.setHandle(resultSet.getString("nse_handle"));
		this.setLdhName(resultSet.getString("nse_ldh_name"));
		if (resultSet.getString("nse_unicode_name") == null || resultSet.getString("nse_unicode_name").isEmpty()) {
			this.setUnicodeName(null);
		} else
			this.setUnicodeName(resultSet.getString("nse_unicode_name"));
		this.setPort43(resultSet.getString("nse_port43"));
	}

}
