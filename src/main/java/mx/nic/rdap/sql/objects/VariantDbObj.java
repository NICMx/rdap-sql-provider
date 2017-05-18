package mx.nic.rdap.sql.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

import mx.nic.rdap.core.db.Variant;

/**
 * Data access class for the {@link Variant} object.
 * 
 */
public class VariantDbObj extends Variant implements DatabaseObject {

	/**
	 * Default constructor
	 */
	public VariantDbObj() {
		super();
	}

	/**
	 * Construcs Variant using a {@link ResultSet}
	 * 
	 */
	public VariantDbObj(ResultSet resultSet) throws SQLException {
		super();
		loadFromDatabase(resultSet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mx.nic.rdap.server.db.DatabaseObject#loadFromDatabase(java.sql.ResultSet)
	 */
	@Override
	public void loadFromDatabase(ResultSet resultSet) throws SQLException {
		this.setId(resultSet.getLong("var_id"));
		this.setIdnTable(resultSet.getString("var_idn_table"));
		this.setDomainId(resultSet.getLong("dom_id"));
	}

}
