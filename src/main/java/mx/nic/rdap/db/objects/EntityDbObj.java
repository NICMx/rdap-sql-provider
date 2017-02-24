package mx.nic.rdap.db.objects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mx.nic.rdap.core.db.Entity;

/**
 * Data access class for the {@link Entity} Object
 * 
 */
public class EntityDbObj extends Entity implements DatabaseObject {

	/**
	 * Default Constructor
	 */
	public EntityDbObj() {
		super();
	}

	/**
	 * Construct Entity using a {@link ResultSet}
	 * 
	 */
	public EntityDbObj(ResultSet resultSet) throws SQLException {
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
		setId(resultSet.getLong("ent_id"));
		setHandle(resultSet.getString("ent_handle"));
		setPort43(resultSet.getString("ent_port43"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mx.nic.rdap.core.db.DatabaseObject#storeToDatabase(java.sql.
	 * PreparedStatement)
	 */
	@Override
	public void storeToDatabase(PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setString(1, getHandle());
		preparedStatement.setString(2, getPort43());
	}

	/**
	 * Same as storeToDatabase,but using different order and should use the
	 * object id as criteria
	 */
	public void updateInDatabase(PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setString(1, getPort43());
		preparedStatement.setLong(2, getId());
	}

}
