package mx.nic.rdap.db.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

import mx.nic.rdap.core.db.Nameserver;
import mx.nic.rdap.db.DatabaseSession;
import mx.nic.rdap.db.exception.InvalidValueException;
import mx.nic.rdap.db.exception.NotImplementedException;
import mx.nic.rdap.db.exception.RdapDataAccessException;
import mx.nic.rdap.db.model.NameserverModel;
import mx.nic.rdap.db.spi.NameserverDAO;
import mx.nic.rdap.db.struct.SearchResultStruct;

public class NameserverDAOImpl implements NameserverDAO {

	public void storeToDatabase(Nameserver nameserver) throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			NameserverModel.storeToDatabase(nameserver, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}

	}

	@Override
	public Nameserver getByName(String name) throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return NameserverModel.findByName(name, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public SearchResultStruct<Nameserver> searchByName(String namePattern, Integer resultLimit)
			throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return NameserverModel.searchByName(namePattern, resultLimit, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public SearchResultStruct<Nameserver> searchByIp(String ipaddressPattern, Integer resultLimit)
			throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return NameserverModel.searchByIp(ipaddressPattern, resultLimit, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public SearchResultStruct<Nameserver> searchByRegexName(String namePattern, Integer resultLimit)
			throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return NameserverModel.searchByRegexName(namePattern, resultLimit, connection);
		} catch (SQLSyntaxErrorException e) {
			throw new InvalidValueException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public SearchResultStruct<Nameserver> searchByRegexIp(String ipaddressPattern, Integer resultLimit)
			throws NotImplementedException {
		throw new NotImplementedException();
	}

}
