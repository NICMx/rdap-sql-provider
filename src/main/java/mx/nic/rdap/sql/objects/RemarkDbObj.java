package mx.nic.rdap.sql.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

import mx.nic.rdap.core.db.Remark;

/**
 * Data access class for the {@link Remark} Object.
 * 
 */
public class RemarkDbObj extends Remark implements DatabaseObject {

	/**
	 * Default constructor
	 */
	public RemarkDbObj() {
		super();
	}

	/**
	 * Construct Remark using a {@link ResultSet}
	 * 
	 */
	public RemarkDbObj(ResultSet resultSet) throws SQLException {
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

}
