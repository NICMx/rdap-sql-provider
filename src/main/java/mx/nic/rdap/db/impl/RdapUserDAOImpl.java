package mx.nic.rdap.db.impl;

import java.sql.Connection;
import java.sql.SQLException;

import mx.nic.rdap.db.DBConnection;
import mx.nic.rdap.db.RdapUser;
import mx.nic.rdap.db.exception.RdapDatabaseException;
import mx.nic.rdap.db.model.RdapUserModel;
import mx.nic.rdap.db.spi.RdapUserSpi;

public class RdapUserDAOImpl implements RdapUserSpi {

	@Override
	public Integer getMaxSearchResults(String username) throws RdapDatabaseException {
		try (Connection connection = DBConnection.getConnection()) {
			return RdapUserModel.getMaxSearchResultsForAuthenticatedUser(username, connection);
		} catch (SQLException e) {
			throw new RdapDatabaseException(e);
		}
	}

	@Override
	public RdapUser getByUsername(String username) throws RdapDatabaseException {
		try (Connection connection = DBConnection.getConnection()) {
			return RdapUserModel.getByName(username, connection);
		} catch (SQLException e) {
			throw new RdapDatabaseException(e);
		}
	}

}
