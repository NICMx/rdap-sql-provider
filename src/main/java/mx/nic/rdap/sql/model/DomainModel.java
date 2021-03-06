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
import mx.nic.rdap.core.db.DomainLabelException;
import mx.nic.rdap.core.ip.IpAddressFormatException;
import mx.nic.rdap.core.ip.IpUtils;
import mx.nic.rdap.db.exception.http.BadRequestException;
import mx.nic.rdap.db.exception.http.NotFoundException;
import mx.nic.rdap.db.exception.http.NotImplementedException;
import mx.nic.rdap.db.exception.http.UnprocessableEntityException;
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

	private static final String GET_BY_NAME_QUERY = "getByDomainName";
	private static final String SEARCH_BY_PARTIAL_NAME_WITH_PARTIAL_ZONE_QUERY = "searchByPartialNameWPartialZone";
	private static final String SEARCH_BY_NAME_WITH_PARTIAL_ZONE_QUERY = "searchByNameWPartialZone";
	private static final String SEARCH_BY_PARTIAL_NAME_WITH_ZONE_QUERY = "searchByPartialNameWZone";
	private static final String SEARCH_BY_NAME_WITH_ZONE_QUERY = "searchByNameWZone";
	private static final String SEARCH_BY_PARTIAL_NAME_WITHOUT_ZONE_QUERY = "searchByPartialNameWOutZone";
	private static final String SEARCH_BY_NAME_WITHOUT_ZONE_QUERY = "searchByNameWOutZone";
	private static final String SEARCH_BY_NAMESERVER_NAME_QUERY = "searchByNsName";
	private static final String SEARCH_BY_NAMESERVER_IP_QUERY = "searchByNsIp";

	private static final String SEARCH_BY_REGEX_NAME_WITH_ZONE = "searchByRegexNameWithZone";
	private static final String SEARCH_BY_REGEX_NAME_WITHOUT_ZONE = "searchByRegexNameWithOutZone";
	private static final String SEARCH_BY_REGEX_NAMESERVER_NAME_QUERY = "searchByRegexNsName";

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

	public static DomainDbObj findByDomainName(DomainLabel domainLabel, Connection connection)
			throws SQLException, NotImplementedException, NotFoundException {
		String domainName = domainLabel.getULabel();
		String name;
		String zone;

		DomainLabel nameToSearch = null;
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
		Integer zoneId = ZoneModel.getIdByZoneName(zone);
		try {
			nameToSearch = new DomainLabel(name);
		} catch (DomainLabelException e) {
			// The label was already valid, handle as no result found
			return null;
		}

		String query = getQueryGroup().getQuery(GET_BY_NAME_QUERY);
		QueryGroup.userImplemented(query);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, nameToSearch.getULabel());
			statement.setInt(2, zoneId);
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					return null;
				}
				DomainDbObj domain = new DomainDbObj(resultSet);
				loadNestedObjects(domain, connection);
				return domain;
			}
		}
	}

	/**
	 * Searches a domain by it´s name and TLD
	 */
	public static SearchResultStruct<Domain> searchByName(String name, String zone, int resultLimit,
			Connection connection) throws SQLException, NotFoundException, NotImplementedException {
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
			if (zoneIds.isEmpty()) {
				throw new NotFoundException("Zone not found.");
			}

			// Escape special chars for the "LIKE" sentence and consecutive wildcards
			// are treated as one
			zone = zone.replaceAll("(\\%|\\_)", "\\\\$1").replaceAll("(\\*)+", "\\*").replaceAll("\\*", "%");
			if (isPartialName) {
				// Escape special chars for the "LIKE" sentence and consecutive
				// wildcards are treated as one
				name = name.replaceAll("(\\%|\\_)", "\\\\$1").replaceAll("(\\*)+", "\\*").replaceAll("\\*", "%");
				query = getQueryGroup().getQuery(SEARCH_BY_PARTIAL_NAME_WITH_PARTIAL_ZONE_QUERY);
			} else {
				query = getQueryGroup().getQuery(SEARCH_BY_NAME_WITH_PARTIAL_ZONE_QUERY);
			}

			QueryGroup.userImplemented(query);

			query = Util.createDynamicQueryWithInClause(zoneIds.size(), query);
		} else {

			if (!ZoneModel.existsZone(zone)) {
				throw new NotFoundException("Zone not found.");
			}

			if (isPartialName) {
				// Escape special chars for the "LIKE" sentence and consecutive
				// wildcards are treated as one
				name = name.replaceAll("(\\%|\\_)", "\\\\$1").replaceAll("(\\*)+", "\\*").replaceAll("\\*", "%");
				query = getQueryGroup().getQuery(SEARCH_BY_PARTIAL_NAME_WITH_ZONE_QUERY);
			} else {
				query = getQueryGroup().getQuery(SEARCH_BY_NAME_WITH_ZONE_QUERY);
			}

			QueryGroup.userImplemented(query);

		}

		try (PreparedStatement statement = connection.prepareStatement(query);) {

			if (isPartialZone) {
				for (int i = 1; i <= zoneIds.size(); i++) {
					statement.setInt(i, zoneIds.get(i - 1));
				}
				statement.setString(zoneIds.size() + 1, name);
				statement.setString(zoneIds.size() + 2, zone);
				statement.setInt(zoneIds.size() + 3, resultLimit);
			} else {
				statement.setString(1, name);
				Integer zoneId = ZoneModel.getIdByZoneName(zone);
				statement.setInt(2, zoneId);
				statement.setInt(3, resultLimit);
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
				loadNestedObjects(domain, connection);
			}
			result.setSearchResultsLimitForUser(resultLimit);
			result.getResults().addAll(domains);
			return result;
		}
	}

	public static SearchResultStruct<Domain> searchByName(String domainName, int resultLimit, Connection connection)
			throws SQLException, NotImplementedException, NotFoundException {
		String query;
		if (domainName.contains("*")) {
			// Escape special chars for the "LIKE" sentence and consecutive wildcards
			// are treated as one
			domainName = domainName.replaceAll("(\\%|\\_)", "\\\\$1").replaceAll("(\\*)+", "\\*").replaceAll("\\*",
					"%");
			query = getQueryGroup().getQuery(SEARCH_BY_PARTIAL_NAME_WITHOUT_ZONE_QUERY);
		} else {
			query = getQueryGroup().getQuery(SEARCH_BY_NAME_WITHOUT_ZONE_QUERY);
		}
		return searchByName(domainName.toLowerCase(), domainName, resultLimit, connection, query);
	}

	public static SearchResultStruct<Domain> searchByRegexName(String regexName, int resultLimit, Connection connection)
			throws SQLException, NotImplementedException, NotFoundException {
		String query = getQueryGroup().getQuery(SEARCH_BY_REGEX_NAME_WITHOUT_ZONE);
		return searchByName(regexName, resultLimit, connection, query);
	}

	public static SearchResultStruct<Domain> searchByRegexName(String name, String zone, int resultLimit,
			Connection connection) throws SQLException, NotImplementedException, NotFoundException {
		SearchResultStruct<Domain> result = new SearchResultStruct<Domain>();
		// Hack to know is there is more domains that the limit, used for
		// notices
		resultLimit = resultLimit + 1;
		String query = getQueryGroup().getQuery(SEARCH_BY_REGEX_NAME_WITH_ZONE);
		QueryGroup.userImplemented(query);

		List<Integer> zoneIds = ZoneModel.getValidZoneIds();
		if (zoneIds.isEmpty()) {
			throw new NotFoundException("Zone not found.");
		}
		query = Util.createDynamicQueryWithInClause(zoneIds.size(), query);

		try (PreparedStatement statement = connection.prepareStatement(query);) {

			for (int i = 1; i <= zoneIds.size(); i++) {
				statement.setInt(i, zoneIds.get(i - 1));
			}
			statement.setString(zoneIds.size() + 1, name);
			statement.setString(zoneIds.size() + 2, zone);
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
				loadNestedObjects(domain, connection);
			}
			result.setSearchResultsLimitForUser(resultLimit);
			result.getResults().addAll(domains);
			return result;
		}
	}

	/**
	 * Wrapper for
	 * {@link mx.nic.rdap.sql.model.DomainModel#searchByName(String, String, boolean, Connection, String)},
	 * useful when the same namePattern is used as ALabel and ULabel.
	 * 
	 * @param domainName
	 * @param resultLimit
	 * @param useNameserverAsDomainAttribute
	 * @param connection
	 * @param query
	 * @return SearchResultStruct of Domains
	 * @throws SQLException
	 * @throws NotImplementedException
	 */
	private static SearchResultStruct<Domain> searchByName(String domainName, int resultLimit, Connection connection,
			String query) throws SQLException, NotImplementedException, NotFoundException {
		return searchByName(domainName, domainName, resultLimit, connection, query);
	}

	/**
	 * Searches a domain by it's name (A-Label and U-Label) when user don´t care
	 * about the TLD
	 * 
	 * @throws NotImplementedException
	 */
	private static SearchResultStruct<Domain> searchByName(String domainNameALabel, String domainNameULabel,
			int resultLimit, Connection connection, String query)
			throws SQLException, NotImplementedException, NotFoundException {
		QueryGroup.userImplemented(query);

		SearchResultStruct<Domain> result = new SearchResultStruct<Domain>();
		// Hack to know is there is more domains that the limit, used for
		// notices
		resultLimit = resultLimit + 1;
		List<Integer> zoneIds = ZoneModel.getValidZoneIds();
		if (zoneIds.isEmpty()) {
			throw new NotFoundException("Zone not found.");
		}
		query = Util.createDynamicQueryWithInClause(zoneIds.size(), query);

		try (PreparedStatement statement = connection.prepareStatement(query);) {

			for (int i = 1; i <= zoneIds.size(); i++) {
				statement.setInt(i, zoneIds.get(i - 1));
			}

			statement.setString(zoneIds.size() + 1, domainNameULabel);
			statement.setInt(zoneIds.size() + 2, resultLimit);
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
				loadNestedObjects(domain, connection);
			}
			result.setSearchResultsLimitForUser(resultLimit);
			result.getResults().addAll(domains);
			return result;
		}
	}

	public static SearchResultStruct<Domain> searchByNsLdhName(String name, int resultLimit, Connection connection)
			throws SQLException, NotImplementedException {
		String query = getQueryGroup().getQuery(SEARCH_BY_NAMESERVER_NAME_QUERY);
		// Escape special chars for the "LIKE" sentence and consecutive wildcards are
		// treated as one
		name = name.replaceAll("(\\%|\\_)", "\\\\$1").replaceAll("(\\*)+", "\\*").replace("*", "%");
		return searchByNsLdhName(name, resultLimit, connection, query);
	}

	public static SearchResultStruct<Domain> searchByRegexNsLdhName(String name, int resultLimit, Connection connection)
			throws SQLException, NotImplementedException {
		String query = getQueryGroup().getQuery(SEARCH_BY_REGEX_NAMESERVER_NAME_QUERY);
		return searchByNsLdhName(name, resultLimit, connection, query);
	}

	/**
	 * Searches all domains with a nameserver by name
	 */
	private static SearchResultStruct<Domain> searchByNsLdhName(String name, int resultLimit, Connection connection,
			String query) throws SQLException, NotImplementedException {
		QueryGroup.userImplemented(query);

		SearchResultStruct<Domain> result = new SearchResultStruct<Domain>();
		// Hack to know is there is more domains that the limit, used for
		// notices
		resultLimit = resultLimit + 1;
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, name);
			statement.setInt(2, resultLimit);
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
				loadNestedObjects(domain, connection);
			}
			result.setSearchResultsLimitForUser(resultLimit);
			result.getResults().addAll(domains);
			return result;
		}
	}

	/**
	 * searches all domains with a nameserver by address
	 */
	public static SearchResultStruct<Domain> searchByNsIp(String ip, int resultLimit, Connection connection)
			throws SQLException, BadRequestException, NotImplementedException, UnprocessableEntityException {
		String query = getQueryGroup().getQuery(SEARCH_BY_NAMESERVER_IP_QUERY);
		QueryGroup.userImplemented(query);

		SearchResultStruct<Domain> result = new SearchResultStruct<Domain>();
		// Hack to know is there is more domains that the limit, used for
		// notices
		resultLimit = resultLimit + 1;
		IpAddressDbObj ipAddress = new IpAddressDbObj();

		if (ip.contains("*")) {
			throw new UnprocessableEntityException("Partial search using IPs isn't implemented, try another search");
		}
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
		try (PreparedStatement statement = connection.prepareStatement(query)) {
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
				loadNestedObjects(domain, connection);
				domains.add(domain);
			} while (resultSet.next());
			resultLimit = resultLimit - 1;// Back to the original limit
			if (domains.size() > resultLimit) {
				result.setResultSetWasLimitedByUserConfiguration(true);
				domains.remove(domains.size() - 1);
			}
			for (DomainDbObj domain : domains) {
				loadNestedObjects(domain, connection);
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
	private static void loadNestedObjects(Domain domain, Connection connection) throws SQLException {
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
		domain.getNameServers().addAll(NameserverModel.getByDomainId(domainId, connection));

		// Retrieve the entities
		domain.getEntities().addAll(EntityModel.getEntitiesByDomainId(domainId, connection));

		// Retrieve the ipNetwork
		domain.setIpNetwork(IpNetworkModel.getByDomainId(domainId, connection));
	}

}
