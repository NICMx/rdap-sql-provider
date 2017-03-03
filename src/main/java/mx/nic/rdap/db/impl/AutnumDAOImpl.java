package mx.nic.rdap.db.impl;

import java.sql.Connection;
import java.sql.SQLException;

import mx.nic.rdap.core.db.Autnum;
import mx.nic.rdap.db.DatabaseSession;
import mx.nic.rdap.db.exception.RdapDataAccessException;
import mx.nic.rdap.db.model.AutnumModel;
import mx.nic.rdap.db.spi.AutnumDAO;

public class AutnumDAOImpl implements AutnumDAO {

	public Long storeToDatabase(Autnum autnum) throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return AutnumModel.storeToDatabase(autnum, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public Autnum getByRange(Long autnumValue) throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return AutnumModel.getByRange(autnumValue, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

}
