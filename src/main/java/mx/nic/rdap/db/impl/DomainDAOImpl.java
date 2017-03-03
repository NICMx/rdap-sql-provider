package mx.nic.rdap.db.impl;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.Arrays;
import java.util.List;

import mx.nic.rdap.core.db.Domain;
import mx.nic.rdap.db.DatabaseSession;
import mx.nic.rdap.db.exception.InvalidValueException;
import mx.nic.rdap.db.exception.NotImplementedException;
import mx.nic.rdap.db.exception.ObjectNotFoundException;
import mx.nic.rdap.db.exception.RdapDataAccessException;
import mx.nic.rdap.db.model.DomainModel;
import mx.nic.rdap.db.model.ZoneModel;
import mx.nic.rdap.db.spi.DomainDAO;
import mx.nic.rdap.db.struct.SearchResultStruct;

public class DomainDAOImpl implements DomainDAO {

	public Long storeToDatabase(Domain domain, Boolean useNsAsAttribute) throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return DomainModel.storeToDatabase(domain, useNsAsAttribute, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public Domain getByName(String domainName, Boolean useNsAsAttribute) throws RdapDataAccessException {

		String name;
		String zone;

		if (!domainName.contains("."))
			throw new InvalidValueException("Invalid fqdn");

		if (ZoneModel.isReverseAddress(domainName)) {
			zone = ZoneModel.getArpaZoneNameFromAddress(domainName);
			if (zone == null) {
				throw new ObjectNotFoundException("Zone not found.");
			}
			name = ZoneModel.getAddressWithoutArpaZone(domainName);
		} else {
			name = domainName.substring(0, domainName.indexOf('.'));
			zone = domainName.substring(domainName.indexOf('.') + 1);
		}
		if (!ZoneModel.existsZone(zone))
			throw new ObjectNotFoundException("Zone not found.");

		try (Connection connection = DatabaseSession.getRdapConnection()) {
			Domain domain = DomainModel.findByLdhName(name, ZoneModel.getIdByZoneName(zone), useNsAsAttribute,
					connection);
			return domain;
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public SearchResultStruct<Domain> searchByName(String domainName, Integer resultLimit,
			boolean useNameserverAsDomainAttribute) throws RdapDataAccessException {
		if (domainName.contains("*")) {
			List<String> labels = Arrays.asList(domainName.split("\\."));
			for (String label : labels) {
				if (label.contains("*") && !label.endsWith("*"))
					throw new InvalidValueException("Patterns can only have an * at the end.");
			}
		}
		SearchResultStruct<Domain> domains = null;
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			if (domainName.contains(".")) {
				String name = domainName.substring(0, domainName.indexOf('.'));
				String zone = domainName.substring(domainName.indexOf('.') + 1);
				domains = DomainModel.searchByName(name, zone, resultLimit, useNameserverAsDomainAttribute, connection);
			} else {
				domains = DomainModel.searchByName(domainName, resultLimit, useNameserverAsDomainAttribute, connection);
			}
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
		return domains;
	}

	@Override
	public SearchResultStruct<Domain> searchByNsName(String nsName, Integer resultLimit, boolean useNsAsAttribute)
			throws RdapDataAccessException {
		if (nsName.contains("*")) {
			List<String> labels = Arrays.asList(nsName.split("\\."));
			for (String label : labels) {
				if (label.contains("*") && !label.endsWith("*"))
					throw new InvalidValueException("Patterns can only have an * at the end");
			}
		}
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return DomainModel.searchByNsLdhName(nsName, resultLimit, useNsAsAttribute, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public SearchResultStruct<Domain> searchByNsIp(String ip, Integer resultLimit, boolean useNsAsAttribute)
			throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return DomainModel.searchByNsIp(ip, resultLimit, useNsAsAttribute, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

	@Override
	public SearchResultStruct<Domain> searchByRegexName(String regexName, Integer resultLimit,
			boolean useNsAsDomainAttribute) throws RdapDataAccessException {
		SearchResultStruct<Domain> domains = null;
		String[] regexWZone = null;
		if (regexName.contains("\\.")) {
			regexWZone = regexName.split("\\\\.", 2);
		}
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			if (regexWZone == null || Array.getLength(regexWZone) <= 1) {
				domains = DomainModel.searchByRegexName(regexName, resultLimit, useNsAsDomainAttribute, connection);
			} else {
				domains = DomainModel.searchByRegexName(regexWZone[0], regexWZone[1], resultLimit,
						useNsAsDomainAttribute, connection);
			}
		} catch (SQLSyntaxErrorException e) {
			throw new InvalidValueException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
		return domains;
	}

	@Override
	public SearchResultStruct<Domain> searchByRegexNsIp(String ip, Integer resultLimit, boolean useNsAsAttribute)
			throws NotImplementedException {
		throw new NotImplementedException();
	}

}
