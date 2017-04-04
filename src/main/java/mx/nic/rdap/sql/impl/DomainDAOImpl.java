package mx.nic.rdap.sql.impl;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import mx.nic.rdap.core.db.Domain;
import mx.nic.rdap.db.exception.RdapDataAccessException;
import mx.nic.rdap.db.exception.http.BadRequestException;
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

	public Long storeToDatabase(Domain domain) throws SQLException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return DomainModel.storeToDatabase(domain, useNsAsAttribute, connection);
		}
	}

	@Override
	public Domain getByName(String domainName) throws RdapDataAccessException {

		String name;
		String zone;

		// TODO validations shouls be done by the server.
		if (!domainName.contains("."))
			throw new BadRequestException("Invalid fqdn");

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
	public SearchResultStruct<Domain> searchByName(String domainName, int resultLimit) throws RdapDataAccessException {
		if (domainName.contains("*")) {
			List<String> labels = Arrays.asList(domainName.split("\\."));
			for (String label : labels) {
				if (label.contains("*") && !label.endsWith("*"))
					throw new BadRequestException("Patterns can only have an * at the end.");
			}
		}
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
	public SearchResultStruct<Domain> searchByNsName(String nsName, int resultLimit) throws RdapDataAccessException {
		if (nsName.contains("*")) {
			List<String> labels = Arrays.asList(nsName.split("\\."));
			for (String label : labels) {
				if (label.contains("*") && !label.endsWith("*"))
					throw new BadRequestException("Patterns can only have an * at the end");
			}
		}
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
	public SearchResultStruct<Domain> searchByRegexNsName(String regexNsName, int resultLimit)
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
