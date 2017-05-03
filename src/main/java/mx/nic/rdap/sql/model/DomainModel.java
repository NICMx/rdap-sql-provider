package mx.nic.rdap.sql.model;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.db.Domain;
import mx.nic.rdap.core.db.DomainLabel;
import mx.nic.rdap.core.ip.IpAddressFormatException;
import mx.nic.rdap.core.ip.IpUtils;
import mx.nic.rdap.db.exception.http.BadRequestException;
import mx.nic.rdap.db.exception.http.NotFoundException;
import mx.nic.rdap.db.struct.SearchResultStruct;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.Util;
import mx.nic.rdap.sql.objects.DomainDbObj;
import mx.nic.rdap.sql.objects.IpAddressDbObj;

/**
 * Model for the {@link Domain} Object
 * 
 */
public class DomainModel {

	private final static Logger logger = Logger.getLogger(DomainModel.class.getName());

	private final static String QUERY_GROUP = "Domain";

	private static QueryGroup queryGroup = null;

	private static final String GET_BY_LDH_QUERY = "getByLdhName";
	private static final String SEARCH_BY_PARTIAL_NAME_WITH_PARTIAL_ZONE_QUERY = "searchByPartialNameWPartialZone";
	private static final String SEARCH_BY_NAME_WITH_PARTIAL_ZONE_QUERY = "searchByNameWPartialZone";
	private static final String SEARCH_BY_PARTIAL_NAME_WITH_ZONE_QUERY = "searchByPartialNameWZone";
	private static final String SEARCH_BY_NAME_WITH_ZONE_QUERY = "searchByNameWZone";
	private static final String SEARCH_BY_PARTIAL_NAME_WITHOUT_ZONE_QUERY = "searchByPartialNameWOutZone";
	private static final String SEARCH_BY_NAME_WITHOUT_ZONE_QUERY = "searchByNameWOutZone";
	private static final String SEARCH_BY_NAMESERVER_LDH_QUERY = "searchByNsLdhName";
	private static final String SEARCH_BY_NAMESERVER_IP_QUERY = "searchByNsIp";

	private static final String SEARCH_BY_REGEX_NAME_WITH_ZONE = "searchByRegexNameWithZone";
	private static final String SEARCH_BY_REGEX_NAME_WITHOUT_ZONE = "searchByRegexNameWithOutZone";
	private static final String SEARCH_BY_REGEX_NAMESERVER_LDH_QUERY = "searchByRegexNsLdhName";

	public static void loadQueryGroup(String schema) {
		try {
			QueryGroup qG = new QueryGroup(QUERY_GROUP, schema);
			setQueryGroup(qG);
		} catch (IOException e) {
			throw new RuntimeException("Error loading query group");
		}
	}

	private static void setQueryGroup(QueryGroup qG) {
		queryGroup = qG;
	}

	private static QueryGroup getQueryGroup() {
		return queryGroup;
	}

	public static DomainDbObj findByLdhName(String name, Integer zoneId, boolean useNameserverAsDomainAttribute,
			Connection connection) throws SQLException {
		String query = getQueryGroup().getQuery(GET_BY_LDH_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, DomainLabel.nameToASCII(name));
			statement.setString(2, DomainLabel.nameToUnicode(name));
			statement.setInt(3, zoneId);
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					return null;
				}
				DomainDbObj domain = new DomainDbObj(resultSet);
				loadNestedObjects(domain, useNameserverAsDomainAttribute, connection);
				return domain;
			}
		}
	}

	/**
	 * Searches a domain by it´s name and TLD
	 */
	public static SearchResultStruct<Domain> searchByName(String name, String zone, int resultLimit,
			boolean useNameserverAsDomainAttribute, Connection connection) throws SQLException, NotFoundException {
		SearchResultStruct<Domain> result = new SearchResultStruct<Domain>();
		// Hack to know is there is more domains that the limit, used for
		// notices
		resultLimit = resultLimit + 1;
		boolean isPartialZone = zone.contains("*");
		boolean isPartialName = name.contains("*");
		String query = null;
		List<Integer> zoneIds = null;

		if (isPartialZone) {
			zoneIds = ZoneModel.getValidZoneIds();

			zone = zone.replaceAll("\\*", "%");
			if (isPartialName) {
				name = name.replaceAll("\\*", "%");
				query = getQueryGroup().getQuery(SEARCH_BY_PARTIAL_NAME_WITH_PARTIAL_ZONE_QUERY);
				query = Util.createDynamicQueryWithInClause(zoneIds.size(), query);
			} else {
				query = getQueryGroup().getQuery(SEARCH_BY_NAME_WITH_PARTIAL_ZONE_QUERY);
				query = Util.createDynamicQueryWithInClause(zoneIds.size(), query);
			}
		} else {

			if (!ZoneModel.existsZone(zone)) {
				throw new NotFoundException("Zone not found.");
			}

			if (isPartialName) {
				name = name.replaceAll("\\*", "%");
				query = getQueryGroup().getQuery(SEARCH_BY_PARTIAL_NAME_WITH_ZONE_QUERY);
			} else {
				query = getQueryGroup().getQuery(SEARCH_BY_NAME_WITH_ZONE_QUERY);
			}
		}

		try (PreparedStatement statement = connection.prepareStatement(query);) {

			if (isPartialZone) {
				for (int i = 1; i <= zoneIds.size(); i++) {
					statement.setInt(i, zoneIds.get(i - 1));
				}
				statement.setString(zoneIds.size() + 1, name);
				statement.setString(zoneIds.size() + 2, name);
				statement.setString(zoneIds.size() + 3, zone);
				statement.setInt(zoneIds.size() + 4, resultLimit);
			} else {
				statement.setString(1, name);
				statement.setString(2, name);
				Integer zoneId = ZoneModel.getIdByZoneName(zone);
				statement.setInt(3, zoneId);
				statement.setInt(4, resultLimit);
			}

			logger.log(Level.INFO, "Executing query" + statement.toString());
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next()) {
				throw new NotFoundException("Object not found.");
			}
			List<DomainDbObj> domains = new ArrayList<DomainDbObj>();
			do {
				DomainDbObj domain = new DomainDbObj(resultSet);
				domains.add(domain);
			} while (resultSet.next());
			resultLimit = resultLimit - 1;// Back to the original limit
			if (domains.size() > resultLimit) {
				result.setResultSetWasLimitedByUserConfiguration(true);
				domains.remove(domains.size() - 1);
			}
			for (DomainDbObj domain : domains) {
				loadNestedObjects(domain, useNameserverAsDomainAttribute, connection);
			}
			result.setSearchResultsLimitForUser(resultLimit);
			result.getResults().addAll(domains);
			return result;
		}
	}

	public static SearchResultStruct<Domain> searchByName(String domainName, int resultLimit,
			boolean useNameserverAsDomainAttribute, Connection connection) throws SQLException {
		String query;
		if (domainName.contains("*")) {
			domainName = domainName.replaceAll("\\*", "%");
			query = getQueryGroup().getQuery(SEARCH_BY_PARTIAL_NAME_WITHOUT_ZONE_QUERY);
		} else {
			query = getQueryGroup().getQuery(SEARCH_BY_NAME_WITHOUT_ZONE_QUERY);
		}
		return searchByName(domainName, resultLimit, useNameserverAsDomainAttribute, connection, query);
	}

	public static SearchResultStruct<Domain> searchByRegexName(String regexName, int resultLimit,
			boolean useNameserverAsDomainAttribute, Connection connection) throws SQLException {
		String query = getQueryGroup().getQuery(SEARCH_BY_REGEX_NAME_WITHOUT_ZONE);
		return searchByName(regexName, resultLimit, useNameserverAsDomainAttribute, connection, query);
	}

	public static SearchResultStruct<Domain> searchByRegexName(String name, String zone, int resultLimit,
			boolean useNameserverAsDomainAttribute, Connection connection) throws SQLException {
		SearchResultStruct<Domain> result = new SearchResultStruct<Domain>();
		// Hack to know is there is more domains that the limit, used for
		// notices
		resultLimit = resultLimit + 1;
		String query = getQueryGroup().getQuery(SEARCH_BY_REGEX_NAME_WITH_ZONE);
		List<Integer> zoneIds = ZoneModel.getValidZoneIds();
		query = Util.createDynamicQueryWithInClause(zoneIds.size(), query);

		try (PreparedStatement statement = connection.prepareStatement(query);) {

			for (int i = 1; i <= zoneIds.size(); i++) {
				statement.setInt(i, zoneIds.get(i - 1));
			}
			statement.setString(zoneIds.size() + 1, name);
			statement.setString(zoneIds.size() + 2, name);
			statement.setString(zoneIds.size() + 3, zone);
			statement.setInt(zoneIds.size() + 4, resultLimit);

			logger.log(Level.INFO, "Executing query" + statement.toString());
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next()) {
				return null;
			}
			List<DomainDbObj> domains = new ArrayList<DomainDbObj>();
			do {
				DomainDbObj domain = new DomainDbObj(resultSet);
				domains.add(domain);
			} while (resultSet.next());
			resultLimit = resultLimit - 1;// Back to the original limit
			if (domains.size() > resultLimit) {
				result.setResultSetWasLimitedByUserConfiguration(true);
				domains.remove(domains.size() - 1);
			}
			for (DomainDbObj domain : domains) {
				loadNestedObjects(domain, useNameserverAsDomainAttribute, connection);
			}
			result.setSearchResultsLimitForUser(resultLimit);
			result.getResults().addAll(domains);
			return result;
		}
	}

	/**
	 * Searches a domain by it's name when user don´t care about the TLD
	 */
	private static SearchResultStruct<Domain> searchByName(String domainName, int resultLimit,
			boolean useNameserverAsDomainAttribute, Connection connection, String query) throws SQLException {
		SearchResultStruct<Domain> result = new SearchResultStruct<Domain>();
		// Hack to know is there is more domains that the limit, used for
		// notices
		resultLimit = resultLimit + 1;
		List<Integer> zoneIds = ZoneModel.getValidZoneIds();
		query = Util.createDynamicQueryWithInClause(zoneIds.size(), query);

		try (PreparedStatement statement = connection.prepareStatement(query);) {

			for (int i = 1; i <= zoneIds.size(); i++) {
				statement.setInt(i, zoneIds.get(i - 1));
			}

			statement.setString(zoneIds.size() + 1, domainName);
			statement.setString(zoneIds.size() + 2, domainName);
			statement.setInt(zoneIds.size() + 3, resultLimit);
			logger.log(Level.INFO, "Executing query" + statement.toString());
			ResultSet resultSet = statement.executeQuery();

			if (!resultSet.next()) {
				return null;
			}
			List<DomainDbObj> domains = new ArrayList<DomainDbObj>();
			do {
				DomainDbObj domain = new DomainDbObj(resultSet);
				domains.add(domain);
			} while (resultSet.next());
			resultLimit = resultLimit - 1;// Back to the original limit
			if (domains.size() > resultLimit) {
				result.setResultSetWasLimitedByUserConfiguration(true);
				domains.remove(domains.size() - 1);
			}
			for (DomainDbObj domain : domains) {
				loadNestedObjects(domain, useNameserverAsDomainAttribute, connection);
			}
			result.setSearchResultsLimitForUser(resultLimit);
			result.getResults().addAll(domains);
			return result;
		}
	}

	public static SearchResultStruct<Domain> searchByNsLdhName(String name, int resultLimit,
			boolean useNameserverAsDomainAttribute, Connection connection) throws SQLException {
		String query = getQueryGroup().getQuery(SEARCH_BY_NAMESERVER_LDH_QUERY);
		name = name.replace("*", "%");
		return searchByNsLdhName(name, resultLimit, useNameserverAsDomainAttribute, connection, query);
	}

	public static SearchResultStruct<Domain> searchByRegexNsLdhName(String name, int resultLimit,
			boolean useNameserverAsDomainAttribute, Connection connection) throws SQLException {
		String query = getQueryGroup().getQuery(SEARCH_BY_REGEX_NAMESERVER_LDH_QUERY);
		return searchByNsLdhName(name, resultLimit, useNameserverAsDomainAttribute, connection, query);
	}

	/**
	 * Searches all domains with a nameserver by name
	 */
	private static SearchResultStruct<Domain> searchByNsLdhName(String name, int resultLimit,
			boolean useNameserverAsDomainAttribute, Connection connection, String query) throws SQLException {
		SearchResultStruct<Domain> result = new SearchResultStruct<Domain>();
		// Hack to know is there is more domains that the limit, used for
		// notices
		resultLimit = resultLimit + 1;
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, name);
			statement.setString(2, name);
			statement.setInt(3, resultLimit);
			logger.log(Level.INFO, "Executing query" + statement.toString());
			ResultSet resultSet = statement.executeQuery();

			if (!resultSet.next()) {
				return null;
			}
			List<DomainDbObj> domains = new ArrayList<DomainDbObj>();
			do {
				DomainDbObj domain = new DomainDbObj(resultSet);
				domains.add(domain);
			} while (resultSet.next());
			resultLimit = resultLimit - 1;// Back to the original limit
			if (domains.size() > resultLimit) {
				result.setResultSetWasLimitedByUserConfiguration(true);
				domains.remove(domains.size() - 1);
			}
			for (DomainDbObj domain : domains) {
				loadNestedObjects(domain, useNameserverAsDomainAttribute, connection);
			}
			result.setSearchResultsLimitForUser(resultLimit);
			result.getResults().addAll(domains);
			return result;
		}
	}

	/**
	 * searches all domains with a nameserver by address
	 */
	public static SearchResultStruct<Domain> searchByNsIp(String ip, int resultLimit,
			boolean useNameserverAsDomainAttribute, Connection connection) throws SQLException, BadRequestException {
		SearchResultStruct<Domain> result = new SearchResultStruct<Domain>();
		// Hack to know is there is more domains that the limit, used for
		// notices
		resultLimit = resultLimit + 1;
		IpAddressDbObj ipAddress = new IpAddressDbObj();

		InetAddress address;
		try {
			address = IpUtils.parseAddress(ip);
		} catch (IpAddressFormatException e) {
			throw new BadRequestException(e);
		}

		ipAddress.setAddress(address);
		if (ipAddress.getAddress() instanceof Inet4Address) {
			ipAddress.setType(4);

		} else if (ipAddress.getAddress() instanceof Inet6Address) {
			ipAddress.setType(6);
		}
		try (PreparedStatement statement = connection
				.prepareStatement(getQueryGroup().getQuery(SEARCH_BY_NAMESERVER_IP_QUERY))) {
			statement.setInt(1, ipAddress.getType());
			statement.setString(2, ipAddress.getAddress().getHostAddress());
			statement.setString(3, ipAddress.getAddress().getHostAddress());
			statement.setInt(4, resultLimit);
			logger.log(Level.INFO, "Executing query" + statement.toString());
			ResultSet resultSet = statement.executeQuery();

			if (!resultSet.next()) {
				return null;
			}
			List<DomainDbObj> domains = new ArrayList<DomainDbObj>();
			do {
				DomainDbObj domain = new DomainDbObj(resultSet);
				loadNestedObjects(domain, useNameserverAsDomainAttribute, connection);
				domains.add(domain);
			} while (resultSet.next());
			resultLimit = resultLimit - 1;// Back to the original limit
			if (domains.size() > resultLimit) {
				result.setResultSetWasLimitedByUserConfiguration(true);
				domains.remove(domains.size() - 1);
			}
			for (DomainDbObj domain : domains) {
				loadNestedObjects(domain, useNameserverAsDomainAttribute, connection);
			}
			result.setSearchResultsLimitForUser(resultLimit);
			result.getResults().addAll(domains);
			return result;
		}

	}

	/**
	 * Load the nested object of the domain
	 * 
	 * @param useNameserverAsDomainAttribute
	 *            if false, load all the nameserver object
	 * 
	 */
	private static void loadNestedObjects(Domain domain, boolean useNameserverAsDomainAttribute, Connection connection)
			throws SQLException {
		Long domainId = domain.getId();

		// Retrieve the events
		domain.getEvents().addAll(EventModel.getByDomainId(domainId, connection));

		// Retrieve the links
		domain.getLinks().addAll(LinkModel.getByDomainId(domainId, connection));

		// Retrieve the status
		domain.getStatus().addAll(StatusModel.getByDomainId(domainId, connection));

		// Retrieve the remarks
		domain.getRemarks().addAll(RemarkModel.getByDomainId(domainId, connection));

		// Retrieve the public ids
		domain.setPublicIds(PublicIdModel.getByDomain(domainId, connection));

		// Retrieve the secure dns
		domain.setSecureDNS(SecureDNSModel.getByDomain(domainId, connection));

		// Retrieve the variants
		domain.setVariants(VariantModel.getByDomainId(domainId, connection));

		// Retrieve the domainsNs
		domain.getNameServers()
				.addAll(NameserverModel.getByDomainId(domainId, useNameserverAsDomainAttribute, connection));

		// Retrieve the entities
		domain.getEntities().addAll(EntityModel.getEntitiesByDomainId(domainId, connection));

		// Retrieve the ipNetwork
		domain.setIpNetwork(IpNetworkModel.getByDomainId(domainId, connection));
	}

}
