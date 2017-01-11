package mx.nic.rdap.db.objects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mx.nic.rdap.core.db.Remark;

/**
 * Data access class for the {@link Remark} Object.
 * 
 */
public class RemarkDAO extends Remark implements DatabaseObject {

	/**
	 * Default constructor
	 */
	public RemarkDAO() {
		super();
	}

	/**
	 * Construct Remark using a {@link ResultSet}
	 * 
	 */
	public RemarkDAO(ResultSet resultSet) throws SQLException {
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
		this.setId(resultSet.getLong("rem_id"));
		this.setTitle(resultSet.getString("rem_title"));
		this.setType(resultSet.getString("rem_type"));
		this.setLanguage(resultSet.getString("rem_lang"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mx.nic.rdap.core.db.DatabaseObject#storeToDatabase(java.sql.
	 * PreparedStatement)
	 */
	@Override
	public void storeToDatabase(PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setString(1, this.getTitle());
		preparedStatement.setString(2, this.getType());
		preparedStatement.setString(3, this.getLanguage());
	}

}
