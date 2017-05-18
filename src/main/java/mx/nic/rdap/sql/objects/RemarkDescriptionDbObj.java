package mx.nic.rdap.sql.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

import mx.nic.rdap.core.db.RemarkDescription;

/**
 * Data access class for the {@link RemarkDescription} object.
 */
public class RemarkDescriptionDbObj extends RemarkDescription implements DatabaseObject {

	/**
	 * Default constructor
	 */
	public RemarkDescriptionDbObj() {
		super();
	}

	/**
	 * Constructs RemarkDescription using a {@link ResultSet}
	 * 
	 */
	public RemarkDescriptionDbObj(ResultSet resultSet) throws SQLException {
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
		this.setRemarkId(resultSet.getLong("rem_id"));
		this.setDescription(resultSet.getString("rde_description"));
		int rdeOrder = resultSet.getInt("rde_order");
		if (!resultSet.wasNull())
			this.setOrder(rdeOrder);
	}

}
