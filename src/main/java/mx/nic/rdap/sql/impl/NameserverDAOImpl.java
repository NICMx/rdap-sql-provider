package mx.nic.rdap.sql.impl;

import java.sql.Connection;
import java.sql.SQLException;

import mx.nic.rdap.core.db.DomainLabel;
import mx.nic.rdap.core.db.Nameserver;
import mx.nic.rdap.db.exception.RdapDataAccessException;
import mx.nic.rdap.db.exception.http.NotImplementedException;
import mx.nic.rdap.db.spi.NameserverDAO;
import mx.nic.rdap.db.struct.SearchResultStruct;
import mx.nic.rdap.sql.DatabaseSession;
import mx.nic.rdap.sql.SQLProviderConfiguration;
import mx.nic.rdap.sql.model.NameserverModel;

public class NameserverDAOImpl implements NameserverDAO {

	@Override
	public Nameserver getByName(DomainLabel name) throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return NameserverModel.findByName(name, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public Nameserver getByHandle(String handle) throws RdapDataAccessException {
		if (!isNameserverSharingNameConformance()) {
			throw new NotImplementedException();
		}
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return NameserverModel.findByHandle(handle, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public SearchResultStruct<Nameserver> searchByName(DomainLabel domainLabel, int resultLimit)
			throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return NameserverModel.searchByName(domainLabel.getULabel(), resultLimit, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public SearchResultStruct<Nameserver> searchByIp(String ipaddressPattern, int resultLimit)
			throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return NameserverModel.searchByIp(ipaddressPattern, resultLimit, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public SearchResultStruct<Nameserver> searchByRegexName(String namePattern, int resultLimit)
			throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return NameserverModel.searchByRegexName(namePattern, resultLimit, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public SearchResultStruct<Nameserver> searchByRegexIp(String ipaddressPattern, int resultLimit)
			throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public int getNameserverCount(DomainLabel name) throws RdapDataAccessException {
		if (!isNameserverSharingNameConformance()) {
			throw new NotImplementedException();
		}
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return NameserverModel.count(name, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public boolean isNameserverSharingNameConformance() {
		return SQLProviderConfiguration.isNsSharingNameEnabled();
	}


}
