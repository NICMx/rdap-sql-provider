package mx.nic.rdap.sql.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

import mx.nic.rdap.core.db.Autnum;
import mx.nic.rdap.sql.model.CountryCodeModel;

/**
 * Data access class for the {@link Autnum} object.
 *
 */
public class AutnumDbObj extends Autnum implements DatabaseObject {

	/**
	 * Default constructor
	 */
	public AutnumDbObj() {
		super();
	}

	/**
	 * Constructs an Autonomous System Number using a {@link ResultSet}
	 */
	public AutnumDbObj(ResultSet resultSet) throws SQLException {
		super();
		loadFromDatabase(resultSet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mx.nic.rdap.db.DatabaseObject#loadFromDatabase(java.sql.ResultSet)
	 */
	@Override
	public void loadFromDatabase(ResultSet resultSet) throws SQLException {
		this.setId(resultSet.getLong("asn_id"));
		this.setHandle(resultSet.getString("asn_handle"));
		this.setStartAutnum(resultSet.getLong("asn_start_autnum"));
		this.setEndAutnum(resultSet.getLong("asn_end_autnum"));
		this.setName(resultSet.getString("asn_name"));
		this.setType(resultSet.getString("asn_type"));
		this.setPort43(resultSet.getString("asn_port43"));
		this.setCountry(CountryCodeModel.getCountryNameById(resultSet.getInt("ccd_id")));
	}

}