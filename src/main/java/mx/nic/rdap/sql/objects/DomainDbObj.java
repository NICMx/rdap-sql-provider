package mx.nic.rdap.sql.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

import mx.nic.rdap.core.db.Domain;
import mx.nic.rdap.core.db.DomainLabel;
import mx.nic.rdap.core.db.DomainLabelException;
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
		if (resultSet.getString("dom_unicode_name") != null && !resultSet.getString("dom_unicode_name").isEmpty()) {
			try {
				DomainLabel d = new DomainLabel(resultSet.getString("dom_unicode_name"));
				this.setLdhName(d.getALabel());
				if (!d.isALabel())
					this.setUnicodeName(d.getULabel());
			} catch (DomainLabelException e) {
				throw new SQLException(e);
			}
		}
		this.setPort43(resultSet.getString("dom_port43"));
		this.setZone(ZoneModel.getZoneNameById(resultSet.getInt("zone_id")));
	}

}
