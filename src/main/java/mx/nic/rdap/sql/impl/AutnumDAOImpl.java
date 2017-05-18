package mx.nic.rdap.sql.impl;

import java.sql.Connection;
import java.sql.SQLException;

import mx.nic.rdap.core.db.Autnum;
import mx.nic.rdap.db.exception.RdapDataAccessException;
import mx.nic.rdap.db.spi.AutnumDAO;
import mx.nic.rdap.sql.DatabaseSession;
import mx.nic.rdap.sql.model.AutnumModel;

public class AutnumDAOImpl implements AutnumDAO {

	@Override
	public Autnum getByRange(long autnumValue) throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return AutnumModel.getByRange(autnumValue, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

}
