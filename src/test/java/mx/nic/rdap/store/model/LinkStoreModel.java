package mx.nic.rdap.store.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.db.Link;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.exception.IncompleteObjectException;
import mx.nic.rdap.sql.exception.InvalidObjectException;

/**
 * Model for the {@link Link} Object
 * 
 */
public class LinkStoreModel {

	private final static Logger logger = Logger.getLogger(LinkStoreModel.class.getName());

	private final static String QUERY_GROUP = "LinkStore";

	private static QueryGroup queryGroup = null;

	private static final String NAMESERVER_STORE_QUERY = "storeNameserverLinksToDatabase";
	private static final String EVENT_STORE_QUERY = "storeEventLinksToDatabase";
	private static final String REMARK_STORE_QUERY = "storeRemarkLinksToDatabase";
	private static final String DS_DATA_STORE_QUERY = "storeDsDataLinksToDatabase";
	private static final String DOMAIN_STORE_QUERY = "storeDomainLinksToDatabase";
	private static final String ENTITY_STORE_QUERY = "storeEntityLinksToDatabase";
	private static final String AUTNUM_STORE_QUERY = "storeAutnumLinksToDatabase";
	private static final String IP_NETWORK_STORE_QUERY = "storeIpNetworkLinksToDatabase";
	private static final String KEY_DATA_STORE_QUERY = "storeKeyDataLinksToDatabase";
	private static final String HREFLANG_STORE_QUERY = "storeLinkHreflangs";

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
		Long linkId = null;
		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery("storeToDatabase"),
				Statement.RETURN_GENERATED_KEYS)) {
			fillPreparedStatement(statement, link);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			result.next();
			linkId = result.getLong(1);// The id of the link inserted
			link.setId(linkId);
		}

		storeLinkHreflang(link, connection);
		return linkId;
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
				Long linkId = LinkStoreModel.storeToDatabase(link, connection);
				statement.setLong(1, id);
				statement.setLong(2, linkId);
				logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
				statement.executeUpdate();
			}
		}
	}

	private static void storeLinkHreflang(Link link, Connection connection) throws SQLException {
		if (link.getHreflang().isEmpty()) {
			return;
		}
		String query = getQueryGroup().getQuery(HREFLANG_STORE_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			for (String lang : link.getHreflang()) {
				statement.setLong(1, link.getId());
				statement.setString(2, lang);
				statement.executeUpdate();
			}
		}
	}

	private static void fillPreparedStatement(PreparedStatement preparedStatement, Link link) throws SQLException {
		preparedStatement.setString(1, link.getValue());
		preparedStatement.setString(2, link.getRel());
		preparedStatement.setString(3, link.getHref());
		preparedStatement.setString(4, link.getTitle());
		preparedStatement.setString(5, link.getMedia());
		preparedStatement.setString(6, link.getType());
	}
}
