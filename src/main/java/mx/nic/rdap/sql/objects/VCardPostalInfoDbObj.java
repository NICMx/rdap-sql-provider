package mx.nic.rdap.sql.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

import mx.nic.rdap.core.db.VCardPostalInfo;

/**
 * Data access class for the {@link VCardPostalInfo} object
 * 
 */
public class VCardPostalInfoDbObj extends VCardPostalInfo implements DatabaseObject {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mx.nic.rdap.core.db.DatabaseObject#loadFromDatabase(java.sql.ResultSet)
	 */
	@Override
	public void loadFromDatabase(ResultSet resultSet) throws SQLException {
		setId(resultSet.getLong("vpi_id"));
		setVCardId(resultSet.getLong("vca_id"));
		setType(resultSet.getString("vpi_type"));
		setCountry(resultSet.getString("vpi_country"));
		setCountryCode(resultSet.getString("vpi_country_code"));
		setCity(resultSet.getString("vpi_city"));
		setStreet1(resultSet.getString("vpi_street1"));
		setStreet2(resultSet.getString("vpi_street2"));
		setStreet3(resultSet.getString("vpi_street3"));
		setState(resultSet.getString("vpi_state"));
		setPostalCode(resultSet.getString("vpi_postal_code"));
	}

}
