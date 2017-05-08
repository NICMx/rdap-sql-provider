package mx.nic.rdap.sql.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.db.Link;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.SQLProviderConfiguration;
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
	private static final String HREFLANG_GET_QUERY = "getLinkHreflangs";

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
		if (SQLProviderConfiguration.isUserSQL() && query == null) {
			return Collections.emptyList();
		}
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

		for (Link link : result) {
			List<String> linkHreflangs = getLinkHreflangs(link.getId(), connection);
			link.getHreflang().addAll(linkHreflangs);
		}
		return result;
	}

	private static List<String> getLinkHreflangs(Long linkId, Connection connection) throws SQLException {
		List<String> result;
		String query = getQueryGroup().getQuery(HREFLANG_GET_QUERY);
		if (SQLProviderConfiguration.isUserSQL() && query == null) {
			return Collections.emptyList();
		}
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, linkId);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			ResultSet rs = statement.executeQuery();
			if (!rs.next()) {
				return Collections.emptyList();
			}
			result = new ArrayList<>();
			do {
				result.add(rs.getString("lan_hreflang"));
			} while (rs.next());
		}

		return result;
	}
}
