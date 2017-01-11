package mx.nic.rdap.db.objects;

import java.net.IDN;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import mx.nic.rdap.core.db.Nameserver;

/**
 * Data access class for the {@link Nameserver} object.
 * 
 */
public class NameserverDAO extends Nameserver implements DatabaseObject {

	/**
	 * Constructor default
	 */
	public NameserverDAO() {
		super();
	}

	/**
	 * Contruct a NameserverDAO using a {@link ResultSet}
	 * 
	 */
	public NameserverDAO(ResultSet resultSet) throws SQLException {
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
		this.setId(resultSet.getLong("nse_id"));
		this.setHandle(resultSet.getString("nse_handle"));
		this.setPunycodeName(resultSet.getString("nse_ldh_name"));
		if (resultSet.getString("nse_unicode_name") == null || resultSet.getString("nse_unicode_name").isEmpty()) {
			this.setUnicodeName(null);
		} else
			this.setUnicodeName(resultSet.getString("nse_unicode_name"));
		this.setPort43(resultSet.getString("nse_port43"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mx.nic.rdap.core.db.DatabaseObject#storeToDatabase(java.sql.
	 * PreparedStatement)
	 */
	@Override
	public void storeToDatabase(PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setString(1, this.getHandle());

		String nsName = this.getLdhName();
		String ldhName = IDN.toASCII(nsName);
		String unicodeName = IDN.toUnicode(nsName);
		if (ldhName.equals(unicodeName)) {
			preparedStatement.setString(2, ldhName);
			preparedStatement.setNull(3, Types.VARCHAR);
		} else {
			preparedStatement.setString(2, ldhName);
			preparedStatement.setString(3, unicodeName);
		}

		preparedStatement.setString(4, this.getPort43());
	}

	/**
	 * Same as storeToDatabase,but using different order and should use the
	 * object id as criteria
	 */
	public void updateInDatabase(PreparedStatement preparedStatement) throws SQLException {
		String nsName = this.getLdhName();
		String ldhName = IDN.toASCII(nsName);
		String unicodeName = IDN.toUnicode(nsName);
		if (ldhName.equals(unicodeName)) {
			preparedStatement.setString(1, ldhName);
			preparedStatement.setNull(2, Types.VARCHAR);
		} else {
			preparedStatement.setString(1, ldhName);
			preparedStatement.setString(2, unicodeName);
		}

		preparedStatement.setString(3, this.getPort43());
		preparedStatement.setLong(4, this.getId());
	}

}
