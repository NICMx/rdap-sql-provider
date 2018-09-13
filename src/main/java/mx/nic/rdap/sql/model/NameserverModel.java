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
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.catalog.Role;
import mx.nic.rdap.core.db.DomainLabel;
import mx.nic.rdap.core.db.Entity;
import mx.nic.rdap.core.db.Nameserver;
import mx.nic.rdap.core.ip.IpAddressFormatException;
import mx.nic.rdap.core.ip.IpUtils;
import mx.nic.rdap.db.exception.http.BadRequestException;
import mx.nic.rdap.db.exception.http.NotImplementedException;
import mx.nic.rdap.db.exception.http.UnprocessableEntityException;
import mx.nic.rdap.db.struct.SearchResultStruct;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.SQLProviderConfiguration;
import mx.nic.rdap.sql.objects.NameserverDbObj;

/**
 * Model for the {@link Nameserver} Object
 * 
 */
public class NameserverModel {

	private final static Logger logger = Logger.getLogger(NameserverModel.class.getName());

	private final static String QUERY_GROUP = "Nameserver";

	private static QueryGroup queryGroup = null;

	private static final String FIND_BY_NAME_QUERY = "findByName";
	private static final String COUNT_BY_NAME_QUERY = "countByName";
	private static final String FIND_BY_HANDLE_QUERY = "findByHandle";

	private static final String SEARCH_BY_PARTIAL_NAME_QUERY = "searchByPartialName";
	private static final String SEARCH_BY_NAME_QUERY = "searchByName";
	private static final String SEARCH_BY_IP6_QUERY = "searchByIp6";
	private static final String SEARCH_BY_IP4_QUERY = "searchByIp4";

	private static final String DOMAIN_GET_QUERY = "getByDomainId";

	private static final String SEARCH_BY_NAME_REGEX_QUERY = "searchByRegexName";

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

	public static NameserverDbObj findByName(DomainLabel name, Connection connection)
			throws SQLException, NotImplementedException {
		String query = getQueryGroup().getQuery(FIND_BY_NAME_QUERY);
		QueryGroup.userImplemented(query);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, name.getALabel().toLowerCase());
			statement.setString(2, name.getULabel());
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					return null;
				}
				NameserverDbObj nameserver = new NameserverDbObj(resultSet);
				NameserverModel.loadNestedObjects(nameserver, connection);
				return nameserver;
			}
		}
	}

	public static NameserverDbObj findByHandle(String handle, Connection connection)
			throws SQLException, NotImplementedException {
		String query = getQueryGroup().getQuery(FIND_BY_HANDLE_QUERY);
		QueryGroup.userImplemented(query);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, handle);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					return null;
				}
				NameserverDbObj nameserver = new NameserverDbObj(resultSet);
				NameserverModel.loadNestedObjects(nameserver, connection);
				return nameserver;
			}
		}
	}

	public static int count(DomainLabel name, Connection connection) throws SQLException, NotImplementedException {
		String query = getQueryGroup().getQuery(COUNT_BY_NAME_QUERY);
		QueryGroup.userImplemented(query);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, name.getALabel().toLowerCase());
			statement.setString(2, name.getULabel());
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					return 0;
				}

				return resultSet.getInt(1);
			}
		}
	}

	public static SearchResultStruct<Nameserver> searchByName(String namePattern, int resultLimit,
			Connection connection) throws SQLException, NotImplementedException {
		String query = null;
		if (namePattern.contains("*")) {// check if is a partial search
			query = getQueryGroup().getQuery(SEARCH_BY_PARTIAL_NAME_QUERY);
			// Escape special chars for the "LIKE" sentence and consecutive wildcards
			// are treated as one
			namePattern = namePattern.replaceAll("(\\%|\\_)", "\\\\$1").replaceAll("(\\*)+", "\\*").replace('*', '%');
		} else {
			query = getQueryGroup().getQuery(SEARCH_BY_NAME_QUERY);
		}
		return searchByName(namePattern.toLowerCase(), namePattern, resultLimit, connection, query);
	}

	public static SearchResultStruct<Nameserver> searchByRegexName(String namePattern, int resultLimit,
			Connection connection) throws SQLException, NotImplementedException {
		return searchByName(namePattern, resultLimit, connection, getQueryGroup().getQuery(SEARCH_BY_NAME_REGEX_QUERY));
	}

	/**
	 * Wrapper for
	 * {@link mx.nic.rdap.sql.model.NameserverModel#searchByName(String, String, int, Connection, String)},
	 * useful when the same namePattern is used as ALabel and ULabel.
	 * 
	 * @param namePattern
	 * @param resultLimit
	 * @param connection
	 * @param query
	 * @return SearchResultStruct of Nameservers
	 * @throws SQLException
	 * @throws NotImplementedException
	 */
	private static SearchResultStruct<Nameserver> searchByName(String namePattern, int resultLimit,
			Connection connection, String query) throws SQLException, NotImplementedException {
		return searchByName(namePattern, namePattern, resultLimit, connection, query);
	}

	private static SearchResultStruct<Nameserver> searchByName(String namePatternALabel, String namePatternULabel,
			int resultLimit, Connection connection, String query) throws SQLException, NotImplementedException {
		QueryGroup.userImplemented(query);
		SearchResultStruct<Nameserver> result = new SearchResultStruct<Nameserver>();
		// Hack to know is there is more domains that the limit, used for
		// notices
		resultLimit = resultLimit + 1;
		List<NameserverDbObj> nameservers = new ArrayList<NameserverDbObj>();
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, namePatternALabel);
			statement.setString(2, namePatternULabel);
			statement.setInt(3, resultLimit);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			try (ResultSet resultSet = statement.executeQuery()) {

				if (!resultSet.next()) {
					return null;
				}
				do {
					NameserverDbObj nameserver = new NameserverDbObj(resultSet);
					nameservers.add(nameserver);
				} while (resultSet.next());

				resultLimit = resultLimit - 1;// Back to the original limit
				if (nameservers.size() > resultLimit) {
					result.setResultSetWasLimitedByUserConfiguration(true);
					nameservers.remove(nameservers.size() - 1);
				}
				for (NameserverDbObj nameserver : nameservers) {
					loadNestedObjects(nameserver, connection);
				}
				result.setSearchResultsLimitForUser(resultLimit);
				result.getResults().addAll(nameservers);
				return result;
			}
		}
	}

	public static SearchResultStruct<Nameserver> searchByIp(String ipaddressPattern, int resultLimit,
			Connection connection)
			throws SQLException, BadRequestException, NotImplementedException, UnprocessableEntityException {
		SearchResultStruct<Nameserver> result = new SearchResultStruct<Nameserver>();
		// Hack to know is there is more domains that the limit, used for
		// notices
		resultLimit = resultLimit + 1;
		String query = null;
		List<NameserverDbObj> nameservers = new ArrayList<NameserverDbObj>();

		if (ipaddressPattern.contains("*")) {
			throw new UnprocessableEntityException("Partial search using IPs isn't implemented, try another search");
		}
		InetAddress address;
		try {
			address = IpUtils.parseAddress(ipaddressPattern);
		} catch (IpAddressFormatException e) {
			throw new BadRequestException(e);
		}

		if (address instanceof Inet6Address) {
			query = getQueryGroup().getQuery(SEARCH_BY_IP6_QUERY);
		} else if (address instanceof Inet4Address) {
			query = getQueryGroup().getQuery(SEARCH_BY_IP4_QUERY);
		} else {
			throw new NotImplementedException("Not implemented for : " + address.getClass().getName());
		}
		QueryGroup.userImplemented(query);

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, ipaddressPattern);
			statement.setInt(2, resultLimit);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			try (ResultSet resultSet = statement.executeQuery()) {

				if (!resultSet.next()) {
					return null;
				}
				do {
					NameserverDbObj nameserver = new NameserverDbObj(resultSet);
					nameservers.add(nameserver);
				} while (resultSet.next());

				resultLimit = resultLimit - 1;// Back to the original limit
				if (nameservers.size() > resultLimit) {
					result.setResultSetWasLimitedByUserConfiguration(true);
					nameservers.remove(nameservers.size() - 1);
				}
				for (NameserverDbObj nameserver : nameservers) {
					loadNestedObjects(nameserver, connection);
				}
				result.setSearchResultsLimitForUser(resultLimit);
				result.getResults().addAll(nameservers);
				return result;
			}
		}
	}

	/**
	 * Find nameservers that belongs from a domain by the domain's id
	 * 
	 * @param useNameserverAsDomainAttribute
	 *            if true, don't have to load nested objects
	 * 
	 */
	public static List<Nameserver> getByDomainId(Long domainId, Connection connection) throws SQLException {
		String query = getQueryGroup().getQuery(DOMAIN_GET_QUERY);
		if (SQLProviderConfiguration.isUserSQL() && query == null) {
			return Collections.emptyList();
		}
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, domainId);
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					return Collections.emptyList();
				}
				List<Nameserver> nameservers = new ArrayList<Nameserver>();
				do {
					Nameserver nameserver = new NameserverDbObj(resultSet);
					// Retrieve the ipAddress
					nameserver.setIpAddresses(
							IpAddressModel.getIpAddressStructByNameserverId(nameserver.getId(), connection));
					NameserverModel.loadNestedObjects(nameserver, connection);
					nameservers.add(nameserver);
				} while (resultSet.next());
				return nameservers;
			}
		}
	}

	public static void loadNestedObjects(Nameserver nameserver, Connection connection) throws SQLException {
		// Retrieve the status
		nameserver.getStatus().addAll(StatusModel.getByNameServerId(nameserver.getId(), connection));

		// Retrieve the remarks
		nameserver.getRemarks().addAll(RemarkModel.getByNameserverId(nameserver.getId(), connection));

		// Retrieve the links
		nameserver.getLinks().addAll(LinkModel.getByNameServerId(nameserver.getId(), connection));

		// Retrieve the events
		nameserver.getEvents().addAll(EventModel.getByNameServerId(nameserver.getId(), connection));
		// Retrieve the entities
		List<Entity> entities = EntityModel.getEntitiesByNameserverId(nameserver.getId(), connection);
		nameserver.getEntities().addAll(entities);
		for (Entity entity : entities) {
			List<Role> roles = RoleModel.getNameserverEntityRole(nameserver.getId(), entity.getId(), connection);
			entity.setRoles(roles);
		}
	}

}