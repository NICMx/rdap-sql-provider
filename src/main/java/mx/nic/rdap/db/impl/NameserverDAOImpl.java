package mx.nic.rdap.db.impl;

import java.sql.Connection;
import java.sql.SQLException;

import mx.nic.rdap.core.db.Nameserver;
import mx.nic.rdap.db.DBConnection;
import mx.nic.rdap.db.exception.NotImplementedException;
import mx.nic.rdap.db.exception.RdapDatabaseException;
import mx.nic.rdap.db.model.NameserverModel;
import mx.nic.rdap.db.spi.NameserverSpi;
import mx.nic.rdap.db.struct.SearchResultStruct;

public class NameserverDAOImpl implements NameserverSpi {

	@Override
	public void storeToDatabase(Nameserver nameserver) throws RdapDatabaseException {
		try (Connection connection = DBConnection.getConnection()) {
			NameserverModel.storeToDatabase(nameserver, connection);
		} catch (SQLException e) {
			throw new RdapDatabaseException(e);
		}

	}

	@Override
	public Nameserver getByName(String name) throws RdapDatabaseException {
		try (Connection connection = DBConnection.getConnection()) {
			return NameserverModel.findByName(name, connection);
		} catch (SQLException e) {
			throw new RdapDatabaseException(e);
		}
	}

	@Override
	public SearchResultStruct searchByName(String namePattern, Integer resultLimit) throws RdapDatabaseException {
		try (Connection connection = DBConnection.getConnection()) {
			return NameserverModel.searchByName(namePattern, resultLimit, connection);
		} catch (SQLException e) {
			throw new RdapDatabaseException(e);
		}
	}

	@Override
	public SearchResultStruct searchByIp(String ipaddressPattern, Integer resultLimit) throws RdapDatabaseException {
		try (Connection connection = DBConnection.getConnection()) {
			return NameserverModel.searchByIp(ipaddressPattern, resultLimit, connection);
		} catch (SQLException e) {
			throw new RdapDatabaseException(e);
		}
	}

	@Override
	public SearchResultStruct searchByRegexName(String namePattern, Integer resultLimit) throws RdapDatabaseException {
		try (Connection connection = DBConnection.getConnection()) {
			return NameserverModel.searchByRegexName(namePattern, resultLimit, connection);
		} catch (SQLException e) {
			throw new RdapDatabaseException(e);
		}
	}

	@Override
	public SearchResultStruct searchByRegexIp(String ipaddressPattern, Integer resultLimit)
			throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public boolean existByName(String name) throws RdapDatabaseException {
		try (Connection connection = DBConnection.getConnection()) {
			NameserverModel.existByName(name, connection);
		} catch (SQLException e) {
			throw new RdapDatabaseException(e);
		}
		return true;
	}

}
