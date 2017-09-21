package mx.nic.rdap.sql.impl;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.SQLException;

import mx.nic.rdap.core.db.Domain;
import mx.nic.rdap.core.db.DomainLabel;
import mx.nic.rdap.db.exception.RdapDataAccessException;
import mx.nic.rdap.db.exception.http.NotFoundException;
import mx.nic.rdap.db.exception.http.NotImplementedException;
import mx.nic.rdap.db.spi.DomainDAO;
import mx.nic.rdap.db.struct.SearchResultStruct;
import mx.nic.rdap.sql.DatabaseSession;
import mx.nic.rdap.sql.model.DomainModel;
import mx.nic.rdap.sql.model.ZoneModel;

public class DomainDAOImpl implements DomainDAO {

	private boolean useNsAsAttribute;

	public DomainDAOImpl(boolean useNsAsAttribute) {
		super();
		this.useNsAsAttribute = useNsAsAttribute;
	}

	@Override
	public Domain getByName(DomainLabel domainLabel) throws RdapDataAccessException {

		String name;
		String zone;

		String domainName = domainLabel.getULabel();

		if (ZoneModel.isReverseAddress(domainName)) {
			zone = ZoneModel.getArpaZoneNameFromAddress(domainName);
			if (zone == null) {
				throw new NotFoundException("Zone not found.");
			}
			name = ZoneModel.getAddressWithoutArpaZone(domainName);
		} else {
			name = domainName.substring(0, domainName.indexOf('.'));
			zone = domainName.substring(domainName.indexOf('.') + 1);
		}
		if (!ZoneModel.existsZone(zone))
			throw new NotFoundException("Zone not found.");

		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return DomainModel.findByLdhName(name, ZoneModel.getIdByZoneName(zone), useNsAsAttribute, connection);
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
				domains = DomainModel.searchByName(name, zone, resultLimit, useNsAsAttribute, connection);
			} else {
				domains = DomainModel.searchByName(domainName, resultLimit, useNsAsAttribute, connection);
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
			return DomainModel.searchByNsLdhName(nsName, resultLimit, useNsAsAttribute, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public SearchResultStruct<Domain> searchByNsIp(String ip, int resultLimit) throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return DomainModel.searchByNsIp(ip, resultLimit, useNsAsAttribute, connection);
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
				domains = DomainModel.searchByRegexName(regexName, resultLimit, useNsAsAttribute, connection);
			} else {
				domains = DomainModel.searchByRegexName(regexWZone[0], regexWZone[1], resultLimit, useNsAsAttribute,
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
			return DomainModel.searchByRegexNsLdhName(regexNsName, resultLimit, useNsAsAttribute, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public SearchResultStruct<Domain> searchByRegexNsIp(String ip, int resultLimit) throws NotImplementedException {
		throw new NotImplementedException();
	}

}
