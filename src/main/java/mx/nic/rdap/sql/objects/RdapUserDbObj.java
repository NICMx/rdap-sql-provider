package mx.nic.rdap.sql.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

import mx.nic.rdap.db.RdapUser;

/**
 * Data access class for the RDAPUser. The object is a data structure of an rdap
 * user information
 * 
 */
public class RdapUserDbObj extends RdapUser implements DatabaseObject {

	/**
	 * Construct a RdapUser from a resultSet
	 */
	public RdapUserDbObj(ResultSet resultSet) throws SQLException {
		super();
		loadFromDatabase(resultSet);
	}

	public RdapUserDbObj() {
		// Empty
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mx.nic.rdap.server.db.DatabaseObject#loadFromDatabase(java.sql.ResultSet)
	 */
	@Override
	public void loadFromDatabase(ResultSet resultSet) throws SQLException {
		this.setId(resultSet.getLong("rus_id"));
		this.setName(resultSet.getString("rus_name"));
		this.setPass(resultSet.getString("rus_pass"));
		this.setMaxSearchResults(resultSet.getInt("rus_max_search_results"));
	}

}
