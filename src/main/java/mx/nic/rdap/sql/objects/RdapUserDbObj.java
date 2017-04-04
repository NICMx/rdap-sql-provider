package mx.nic.rdap.sql.objects;

import java.sql.PreparedStatement;
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
		setUserRole(new RdapUserRoleDbObj());
	}

	public RdapUserDbObj() {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see mx.nic.rdap.server.db.DatabaseObject#storeToDatabase(java.sql.
	 * PreparedStatement)
	 */
	@Override
	public void storeToDatabase(PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setString(1, this.getName());
		preparedStatement.setString(2, this.getPass());
		if (this.getMaxSearchResults() != null)
			preparedStatement.setInt(3, this.getMaxSearchResults());
		else
			preparedStatement.setNull(3, java.sql.Types.INTEGER);
	}

	/**
	 * Same as storeToDatabase,but using different order and should use the
	 * object id as criteria
	 */
	public void updateInDatabase(PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setString(1, this.getPass());
		if (this.getMaxSearchResults() != null)
			preparedStatement.setInt(2, this.getMaxSearchResults());
		else
			preparedStatement.setNull(2, java.sql.Types.INTEGER);
		preparedStatement.setLong(3, this.getId());
	}

}
