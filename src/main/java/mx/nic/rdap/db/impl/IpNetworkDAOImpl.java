package mx.nic.rdap.db.impl;

import java.sql.Connection;
import java.sql.SQLException;

import mx.nic.rdap.core.db.IpNetwork;
import mx.nic.rdap.db.DBConnection;
import mx.nic.rdap.db.exception.RdapDatabaseException;
import mx.nic.rdap.db.model.IpNetworkModel;
import mx.nic.rdap.db.spi.IpNetworkSpi;

public class IpNetworkDAOImpl implements IpNetworkSpi {

	@Override
	public IpNetwork getByInetAddress(String ipAddress) throws RdapDatabaseException {
		try (Connection connection = DBConnection.getConnection()) {
			return IpNetworkModel.getByInetAddress(ipAddress, connection);
		} catch (SQLException e) {
			throw new RdapDatabaseException(e);
		}
	}

	@Override
	public IpNetwork getByInetAddress(String ipAddress, Integer cidr) throws RdapDatabaseException {
		try (Connection connection = DBConnection.getConnection()) {
			return IpNetworkModel.getByInetAddress(ipAddress, cidr, connection);
		} catch (SQLException e) {
			throw new RdapDatabaseException(e);
		}
	}

	@Override
	public boolean existByInetAddress(String ipAddress, Integer cidr) throws RdapDatabaseException {
		try (Connection connection = DBConnection.getConnection()) {
			IpNetworkModel.existByInetAddress(ipAddress, cidr, connection);
		} catch (SQLException e) {
			throw new RdapDatabaseException(e);
		}

		return true;
	}

	@Override
	public boolean existByInetAddress(String ipAddress) throws RdapDatabaseException {
		try (Connection connection = DBConnection.getConnection()) {
			IpNetworkModel.existByInetAddress(ipAddress, connection);
		} catch (SQLException e) {
			throw new RdapDatabaseException(e);
		}

		return true;
	}

}
