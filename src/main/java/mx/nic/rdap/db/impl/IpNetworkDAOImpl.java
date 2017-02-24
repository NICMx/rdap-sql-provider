package mx.nic.rdap.db.impl;

import java.sql.Connection;
import java.sql.SQLException;

import mx.nic.rdap.core.db.IpNetwork;
import mx.nic.rdap.db.DatabaseSession;
import mx.nic.rdap.db.exception.RdapDataAccessException;
import mx.nic.rdap.db.model.IpNetworkModel;
import mx.nic.rdap.db.spi.IpNetworkDAO;

public class IpNetworkDAOImpl implements IpNetworkDAO {

	public Long storeToDatabase(IpNetwork ipNetwork) throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return IpNetworkModel.storeToDatabase(ipNetwork, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public IpNetwork getByInetAddress(String ipAddress) throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return IpNetworkModel.getByInetAddress(ipAddress, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public IpNetwork getByInetAddress(String ipAddress, Integer cidr) throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return IpNetworkModel.getByInetAddress(ipAddress, cidr, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public boolean existByInetAddress(String ipAddress, Integer cidr) throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			IpNetworkModel.existByInetAddress(ipAddress, cidr, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}

		return true;
	}

	@Override
	public boolean existByInetAddress(String ipAddress) throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			IpNetworkModel.existByInetAddress(ipAddress, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}

		return true;
	}

}
