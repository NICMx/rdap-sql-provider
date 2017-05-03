package mx.nic.rdap.sql.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

import mx.nic.rdap.db.RdapUserRole;

/**
 * Data access class for the RDAPUserRole. The object is a data structure of an
 * rdap user role information
 * 
 */
public class RdapUserRoleDbObj extends RdapUserRole implements DatabaseObject {

	/**
	 * Construct a RdapUserRole using a {@link ResultSet}
	 */
	public RdapUserRoleDbObj(ResultSet resultSet) throws SQLException {
		super();
		loadFromDatabase(resultSet);
	}

	public RdapUserRoleDbObj() {
		// no code;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mx.nic.rdap.server.db.DatabaseObject#loadFromDatabase(java.sql.ResultSet)
	 */
	@Override
	public void loadFromDatabase(ResultSet resultSet) throws SQLException {
		this.setUserName(resultSet.getString("rus_name"));
		this.setRoleName(resultSet.getString("rur_name"));
	}

}
