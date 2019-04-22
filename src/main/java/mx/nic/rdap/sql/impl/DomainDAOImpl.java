package mx.nic.rdap.sql.impl;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.SQLException;

import mx.nic.rdap.core.db.Domain;
import mx.nic.rdap.core.db.DomainLabel;
import mx.nic.rdap.db.exception.RdapDataAccessException;
import mx.nic.rdap.db.exception.http.NotImplementedException;
import mx.nic.rdap.db.spi.DomainDAO;
import mx.nic.rdap.db.struct.SearchResultStruct;
import mx.nic.rdap.sql.DatabaseSession;
import mx.nic.rdap.sql.model.DomainModel;

public class DomainDAOImpl implements DomainDAO {


	public DomainDAOImpl() {
		super();
	}

	@Override
	public Domain getByName(DomainLabel domainLabel) throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return DomainModel.findByDomainName(domainLabel, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public SearchResultStruct<Domain> searchByName(DomainLabel domainLabel, int resultLimit)
			throws RdapDataAccessException {

		String domainName = domainLabel.getULabel();
		SearchResultStruct<Domain> domains = null;
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			if (domainName.contains(".")) {
				String name = domainName.substring(0, domainName.indexOf('.'));
				String zone = domainName.substring(domainName.indexOf('.') + 1);
				domains = DomainModel.searchByName(name, zone, resultLimit, connection);
			} else {
				domains = DomainModel.searchByName(domainName, resultLimit, connection);
			}
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
		return domains;
	}

	@Override
	public SearchResultStruct<Domain> searchByNsLDHName(DomainLabel domainLabel, int resultLimit)
			throws RdapDataAccessException {
		String nsName = domainLabel.getULabel();
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return DomainModel.searchByNsLdhName(nsName, resultLimit, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public SearchResultStruct<Domain> searchByNsIp(String ip, int resultLimit) throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return DomainModel.searchByNsIp(ip, resultLimit, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public SearchResultStruct<Domain> searchByRegexName(String regexName, int resultLimit)
			throws RdapDataAccessException {
		SearchResultStruct<Domain> domains = null;
		String[] regexWZone = null;
		if (regexName.contains("\\.")) {
			regexWZone = regexName.split("\\\\.", 2);
		}
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			if (regexWZone == null || Array.getLength(regexWZone) <= 1) {
				domains = DomainModel.searchByRegexName(regexName, resultLimit, connection);
			} else {
				domains = DomainModel.searchByRegexName(regexWZone[0], regexWZone[1], resultLimit,
						connection);
			}
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
		return domains;
	}

	@Override
	public SearchResultStruct<Domain> searchByRegexNsLDHName(String regexNsName, int resultLimit)
			throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return DomainModel.searchByRegexNsLdhName(regexNsName, resultLimit, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public SearchResultStruct<Domain> searchByRegexNsIp(String ip, int resultLimit) throws NotImplementedException {
		throw new NotImplementedException();
	}

}
