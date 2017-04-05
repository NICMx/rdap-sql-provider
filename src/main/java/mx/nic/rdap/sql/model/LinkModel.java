package mx.nic.rdap.sql.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.db.Link;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.exception.IncompleteObjectException;
import mx.nic.rdap.sql.exception.InvalidObjectException;
import mx.nic.rdap.sql.objects.LinkDbObj;

/**
 * Model for the {@link Link} Object
 * 
 */
public class LinkModel {

	private final static Logger logger = Logger.getLogger(LinkModel.class.getName());

	private final static String QUERY_GROUP = "Link";

	private static QueryGroup queryGroup = null;

	private static final String NAMESERVER_GET_QUERY = "getByNameServerId";
	private static final String EVENT_GET_QUERY = "getByEventId";
	private static final String DS_DATA_GET_QUERY = "getByDsDataId";
	private static final String DOMAIN_GET_QUERY = "getByDomainId";
	private static final String REMARK_GET_QUERY = "getByRemarkId";
	private static final String ENTITY_GET_QUERY = "getByEntityId";
	private static final String AUTNUM_GET_QUERY = "getByAutnumId";
	private static final String IP_NETWORK_GET_QUERY = "getByIpNetworkId";
	private static final String KEY_DATA_GET_QUERY = "getByKeyDataId";

	private static final String NAMESERVER_STORE_QUERY = "storeNameserverLinksToDatabase";
	private static final String EVENT_STORE_QUERY = "storeEventLinksToDatabase";
	private static final String REMARK_STORE_QUERY = "storeRemarkLinksToDatabase";
	private static final String DS_DATA_STORE_QUERY = "storeDsDataLinksToDatabase";
	private static final String DOMAIN_STORE_QUERY = "storeDomainLinksToDatabase";
	private static final String ENTITY_STORE_QUERY = "storeEntityLinksToDatabase";
	private static final String AUTNUM_STORE_QUERY = "storeAutnumLinksToDatabase";
	private static final String IP_NETWORK_STORE_QUERY = "storeIpNetworkLinksToDatabase";
	private static final String KEY_DATA_STORE_QUERY = "storeKeyDataLinksToDatabase";

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

	/**
	 * Validate the required attributes for the link
	 * 
	 */
	private static void isValidForStore(Link link) throws InvalidObjectException {
		if (link.getValue() == null || link.getValue().isEmpty())
			throw new IncompleteObjectException("value", "Link");
		if (link.getHref() == null || link.getHref().isEmpty())
			throw new IncompleteObjectException("href", "Link");
	}

	/**
	 * Store a Link in the Database
	 * 
	 */
	private static Long storeToDatabase(Link link, Connection connection) throws SQLException {
		isValidForStore(link);
		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery("storeToDatabase"),
				Statement.RETURN_GENERATED_KEYS)) {
			((LinkDbObj) link).storeToDatabase(statement);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			result.next();
			Long linkId = result.getLong(1);// The id of the link inserted
			link.setId(linkId);
			return linkId;
		}
	}

	public static void storeNameserverLinksToDatabase(List<Link> links, Long nameserverId, Connection connection)
			throws SQLException {
		storeLinkRelationToDatabase(links, nameserverId, connection, NAMESERVER_STORE_QUERY);
	}

	public static void storeDomainLinksToDatabase(List<Link> links, Long domainId, Connection connection)
			throws SQLException {
		storeLinkRelationToDatabase(links, domainId, connection, DOMAIN_STORE_QUERY);
	}

	public static void storeDsDataLinksToDatabase(List<Link> links, Long dsDataId, Connection connection)
			throws SQLException {
		storeLinkRelationToDatabase(links, dsDataId, connection, DS_DATA_STORE_QUERY);
	}

	public static void storeKeyDataLinksToDatabase(List<Link> links, Long keyDataId, Connection connection)
			throws SQLException {
		storeLinkRelationToDatabase(links, keyDataId, connection, KEY_DATA_STORE_QUERY);
	}

	public static void storeEventLinksToDatabase(List<Link> links, Long eventId, Connection connection)
			throws SQLException {
		storeLinkRelationToDatabase(links, eventId, connection, EVENT_STORE_QUERY);
	}

	public static void storeRemarkLinksToDatabase(List<Link> links, Long remarkId, Connection connection)
			throws SQLException {
		storeLinkRelationToDatabase(links, remarkId, connection, REMARK_STORE_QUERY);
	}

	public static void storeAutnumLinksToDatabase(List<Link> links, Long autnumId, Connection connection)
			throws SQLException {
		storeLinkRelationToDatabase(links, autnumId, connection, AUTNUM_STORE_QUERY);

	}

	public static void storeEntityLinksToDatabase(List<Link> links, Long entityId, Connection connection)
			throws SQLException {
		storeLinkRelationToDatabase(links, entityId, connection, ENTITY_STORE_QUERY);
	}

	public static void storeIpNetworkLinksToDatabase(List<Link> links, Long ipNetworkId, Connection connection)
			throws SQLException {
		storeLinkRelationToDatabase(links, ipNetworkId, connection, IP_NETWORK_STORE_QUERY);
	}

	private static void storeLinkRelationToDatabase(List<Link> links, Long id, Connection connection,
			String storeQueryId) throws SQLException {
		if (links.isEmpty())
			return;

		String query = getQueryGroup().getQuery(storeQueryId);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			for (Link link : links) {
				Long linkId = LinkModel.storeToDatabase(link, connection);
				statement.setLong(1, id);
				statement.setLong(2, linkId);
				logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
				statement.executeUpdate();
			}
		}
	}

	public static List<LinkDbObj> getByNameServerId(Long nameserverId, Connection connection) throws SQLException {
		return getByRelationId(nameserverId, connection, NAMESERVER_GET_QUERY);
	}

	public static List<LinkDbObj> getByDomainId(Long domainId, Connection connection) throws SQLException {
		return getByRelationId(domainId, connection, DOMAIN_GET_QUERY);
	}

	public static List<LinkDbObj> getByEventId(Long eventId, Connection connection) throws SQLException {
		return getByRelationId(eventId, connection, EVENT_GET_QUERY);
	}

	public static List<LinkDbObj> getByRemarkId(Long remarkId, Connection connection) throws SQLException {
		return getByRelationId(remarkId, connection, REMARK_GET_QUERY);
	}

	public static List<LinkDbObj> getByDsDataId(Long dsDataId, Connection connection) throws SQLException {
		return getByRelationId(dsDataId, connection, DS_DATA_GET_QUERY);
	}

	public static List<LinkDbObj> getByKeyDataId(Long keyDataId, Connection connection) throws SQLException {
		return getByRelationId(keyDataId, connection, KEY_DATA_GET_QUERY);
	}

	public static List<LinkDbObj> getByEntityId(Long entityId, Connection connection) throws SQLException {
		return getByRelationId(entityId, connection, ENTITY_GET_QUERY);
	}

	public static List<LinkDbObj> getByAutnumId(Long autnumId, Connection connection) throws SQLException {
		return getByRelationId(autnumId, connection, AUTNUM_GET_QUERY);
	}

	public static List<LinkDbObj> getByIpNetworkId(Long ipNetworkId, Connection connection) throws SQLException {
		return getByRelationId(ipNetworkId, connection, IP_NETWORK_GET_QUERY);
	}

	private static List<LinkDbObj> getByRelationId(Long id, Connection connection, String queryGetId)
			throws SQLException {
		String query = getQueryGroup().getQuery(queryGetId);
		List<LinkDbObj> result = null;

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, id);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					return Collections.emptyList(); // A Data can have no links
				}
				List<LinkDbObj> links = new ArrayList<LinkDbObj>();
				do {
					LinkDbObj link = new LinkDbObj(resultSet);
					links.add(link);
				} while (resultSet.next());
				result = links;
			}
		}

		return result;
	}

}
