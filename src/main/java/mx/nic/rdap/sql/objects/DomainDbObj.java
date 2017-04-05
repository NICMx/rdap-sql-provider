package mx.nic.rdap.sql.objects;

import java.net.IDN;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import mx.nic.rdap.core.db.Domain;
import mx.nic.rdap.sql.model.ZoneModel;

/**
 * Data access class for the {@link Domain} object.
 * 
 */
public class DomainDbObj extends Domain implements DatabaseObject {

	/**
	 * Default Constructor
	 */
	public DomainDbObj() {
		super();
	}

	/**
	 * Construct Domain using a {@link ResultSet}
	 */
	public DomainDbObj(ResultSet resultSet) throws SQLException {
		super();
		loadFromDatabase(resultSet);
	}

	/**
	 * Loads the information coming from the database in an instance of Domain
	 * 
	 */
	@Override
	public void loadFromDatabase(ResultSet resultSet) throws SQLException {
		this.setId(resultSet.getLong("dom_id"));
		this.setHandle(resultSet.getString("dom_handle"));
		this.setLdhName(resultSet.getString("dom_ldh_name"));
		if (resultSet.getString("dom_unicode_name") == null || resultSet.getString("dom_unicode_name").isEmpty()) {
			this.setUnicodeName(null);
		} else
			this.setUnicodeName(resultSet.getString("dom_unicode_name"));
		this.setPort43(resultSet.getString("dom_port43"));
		this.setZone(ZoneModel.getZoneNameById(resultSet.getInt("zone_id")));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mx.nic.rdap.server.db.DatabaseObject#storeToDatabase(java.sql.
	 * PreparedStatement)
	 */
	@Override
	public void storeToDatabase(PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setString(1, this.getHandle());

		String domName = this.getLdhName();
		String ldhName = IDN.toASCII(domName);
		String unicodeName = IDN.toUnicode(domName);

		if (ldhName.equals(unicodeName)) {
			preparedStatement.setString(2, ldhName);
			preparedStatement.setNull(3, Types.VARCHAR);
		} else {
			preparedStatement.setString(2, ldhName);
			preparedStatement.setString(3, unicodeName);
		}

		preparedStatement.setString(4, this.getPort43());
		preparedStatement.setInt(5, ZoneModel.getIdByZoneName(this.getZone()));

	}

}
