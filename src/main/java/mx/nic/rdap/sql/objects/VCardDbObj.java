package mx.nic.rdap.sql.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

import mx.nic.rdap.core.db.VCard;

/**
 * Data access class for the {@link VCard} Object
 * 
 */
public class VCardDbObj extends VCard implements DatabaseObject {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mx.nic.rdap.core.db.DatabaseObject#loadFromDatabase(java.sql.ResultSet)
	 */
	@Override
	public void loadFromDatabase(ResultSet resultSet) throws SQLException {
		setId(resultSet.getLong("vca_id"));
		setName(resultSet.getString("vca_name"));
		setCompanyName(resultSet.getString("vca_company_name"));
		setCompanyURL(resultSet.getString("vca_company_url"));
		setEmail(resultSet.getString("vca_email"));
		setVoice(resultSet.getString("vca_voice"));
		setCellphone(resultSet.getString("vca_cellphone"));
		setFax(resultSet.getString("vca_fax"));
		setJobTitle(resultSet.getString("vca_job_title"));
	}

}
