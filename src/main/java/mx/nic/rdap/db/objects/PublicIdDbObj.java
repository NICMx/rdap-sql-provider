package mx.nic.rdap.db.objects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mx.nic.rdap.core.db.PublicId;

/**
 * Data access class for the {@link PublicId} object.
 */
public class PublicIdDbObj extends PublicId implements DatabaseObject {

	/**
	 * Default Constructor
	 */
	public PublicIdDbObj() {
		super();

	}

	/**
	 * Constructs PublicId using a {@link ResultSet}
	 */
	public PublicIdDbObj(ResultSet resultSet) throws SQLException {
		super();
		this.loadFromDatabase(resultSet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mx.nic.rdap.server.db.DatabaseObject#loadFromDatabase(java.sql.ResultSet)
	 */
	@Override
	public void loadFromDatabase(ResultSet resultSet) throws SQLException {
		this.setId(resultSet.getLong("pid_id"));
		this.setType(resultSet.getString("pid_type"));
		this.setPublicId(resultSet.getString("pid_identifier"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mx.nic.rdap.server.db.DatabaseObject#storeToDatabase(java.sql.
	 * PreparedStatement)
	 */
	@Override
	public void storeToDatabase(PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setString(1, this.getType());
		preparedStatement.setString(2, this.getPublicId());
	}

}
