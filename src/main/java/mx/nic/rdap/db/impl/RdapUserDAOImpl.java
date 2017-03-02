package mx.nic.rdap.db.impl;

import java.sql.Connection;
import java.sql.SQLException;

import mx.nic.rdap.db.DatabaseSession;
import mx.nic.rdap.db.RdapUser;
import mx.nic.rdap.db.exception.RdapDataAccessException;
import mx.nic.rdap.db.model.RdapUserModel;
import mx.nic.rdap.db.spi.RdapUserDAO;

public class RdapUserDAOImpl implements RdapUserDAO {

	@Override
	public RdapUser getByUsername(String username) throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return RdapUserModel.getByName(username, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

}
