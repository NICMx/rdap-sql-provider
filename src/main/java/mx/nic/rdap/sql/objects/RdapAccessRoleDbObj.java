package mx.nic.rdap.sql.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

import mx.nic.rdap.db.RdapAccessRole;

/**
 * Data access class for the RDAPAccesRole. The object is a data structure of an rdap
 * access role information
 * 
 */
public class RdapAccessRoleDbObj extends RdapAccessRole implements DatabaseObject {

	/**
	 * Construct a RdapAccessRole from a resultSet
	 */
	public RdapAccessRoleDbObj(ResultSet resultSet) throws SQLException {
		super();
		loadFromDatabase(resultSet);
	}

	public RdapAccessRoleDbObj() {
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
		this.setName(resultSet.getString("rar_name").toLowerCase());
		this.setDescription(resultSet.getString("rar_description"));
	}

}
