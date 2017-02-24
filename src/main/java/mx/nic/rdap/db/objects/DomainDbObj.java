package mx.nic.rdap.db.objects;

import java.net.IDN;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import mx.nic.rdap.core.db.Domain;
import mx.nic.rdap.db.model.ZoneModel;

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
		this.setPunycodeName(resultSet.getString("dom_ldh_name"));
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

	/**
	 * Same as storeToDatabase,but using different order and should use the
	 * object id as criteria
	 */
	public void updateInDatabase(PreparedStatement preparedStatement) throws SQLException {

		String domName = this.getLdhName();
		String ldhName = IDN.toASCII(domName);
		String unicodeName = IDN.toUnicode(domName);
		if (ldhName.equals(unicodeName)) {
			preparedStatement.setString(1, ldhName);
			preparedStatement.setNull(2, Types.VARCHAR);
		} else {
			preparedStatement.setString(1, ldhName);
			preparedStatement.setString(2, unicodeName);
		}
		preparedStatement.setString(1, this.getLdhName());

		preparedStatement.setString(3, this.getPort43());
		preparedStatement.setInt(4, ZoneModel.getIdByZoneName(this.getZone()));
		preparedStatement.setLong(5, this.getId());
	}

}
