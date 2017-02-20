package mx.nic.rdap.db.impl;

import java.sql.Connection;
import java.sql.SQLException;

import mx.nic.rdap.core.db.Autnum;
import mx.nic.rdap.db.DBConnection;
import mx.nic.rdap.db.exception.RdapDatabaseException;
import mx.nic.rdap.db.model.AutnumModel;
import mx.nic.rdap.db.spi.AutnumSpi;

public class AutnumDAOImpl implements AutnumSpi {

	@Override
	public Autnum getByRange(Long autnumValue) throws RdapDatabaseException {
		try (Connection connection = DBConnection.getConnection()) {
			return AutnumModel.getByRange(autnumValue, connection);
		} catch (SQLException e) {
			throw new RdapDatabaseException(e);
		}
	}

	@Override
	public boolean existByRange(Long autnumValue) throws RdapDatabaseException {
		try (Connection connection = DBConnection.getConnection()) {
			AutnumModel.existByRange(autnumValue, connection);
		} catch (SQLException e) {
			throw new RdapDatabaseException(e);
		}

		return true;
	}

}
