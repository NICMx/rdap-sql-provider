
package mx.nic.rdap.db.model;

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

import mx.nic.rdap.core.db.Event;
import mx.nic.rdap.db.QueryGroup;
import mx.nic.rdap.db.exception.RequiredValueNotFoundException;
import mx.nic.rdap.db.objects.EventDbObj;

/**
 * The model for {@link Event} Object
 * 
 */
public class EventModel {

	private final static Logger logger = Logger.getLogger(RemarkModel.class.getName());

	private final static String QUERY_GROUP = "Event";

	protected static QueryGroup queryGroup = null;

	private static final String NAMESERVER_GET_QUERY = "getByNameServerId";
	private static final String DS_DATA_GET_QUERY = "getByDsDataId";
	private static final String DOMAIN_GET_QUERY = "getByDomainId";
	private static final String ENTITY_GET_QUERY = "getByEntityId";
	private static final String AUTNUM_GET_QUERY = "getByAutnumId";
	private static final String IP_NETWORK_GET_QUERY = "getByIpNetworkId";
	private static final String KEY_DATA_GET_QUERY = "getByKeyDataId";

	private static final String NAMESERVER_STORE_QUERY = "storeNameserverEventsToDatabase";
	private static final String DS_DATA_STORE_QUERY = "storeDsDataEventsToDatabase";
	private static final String DOMAIN_STORE_QUERY = "storeDomainEventsToDatabase";
	private static final String ENTITY_STORE_QUERY = "storeEntityEventsToDatabase";
	private static final String AUTNUM_STORE_QUERY = "storeAutnumEventsToDatabase";
	private static final String IP_NETWORK_STORE_QUERY = "storeIpNetworkEventsToDatabase";
	private static final String KEY_DATA_STORE_QUERY = "storeKeyDataEventsToDatabase";

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

	private static void isValidForStore(Event event) throws RequiredValueNotFoundException {
		if (event.getEventAction() == null)
			throw new RequiredValueNotFoundException("eventAction", "Event");
		if (event.getEventDate() == null)
			throw new RequiredValueNotFoundException("eventDate", "Event");
	}

	public static long storeToDatabase(Event event, Connection connection)
			throws SQLException, RequiredValueNotFoundException {
		isValidForStore(event);
		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery("storeToDatabase"),
				Statement.RETURN_GENERATED_KEYS)) {
			((EventDbObj) event).storeToDatabase(statement);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			result.next();
			Long eventId = result.getLong(1);// The id of the link inserted
			event.setId(eventId);
			LinkModel.storeEventLinksToDatabase(event.getLinks(), eventId, connection);
			return eventId;
		}
	}

	public static void storeNameserverEventsToDatabase(List<Event> events, Long nameserverId, Connection connection)
			throws SQLException, RequiredValueNotFoundException {
		storeRelationEventsToDatabase(events, nameserverId, connection, NAMESERVER_STORE_QUERY);
	}

	public static void storeEntityEventsToDatabase(List<Event> events, Long entityId, Connection connection)
			throws SQLException, RequiredValueNotFoundException {
		storeRelationEventsToDatabase(events, entityId, connection, ENTITY_STORE_QUERY);
	}

	public static void storeDomainEventsToDatabase(List<Event> events, Long domainId, Connection connection)
			throws SQLException, RequiredValueNotFoundException {
		storeRelationEventsToDatabase(events, domainId, connection, DOMAIN_STORE_QUERY);
	}

	public static void storeAutnumEventsToDatabase(List<Event> events, Long autnumId, Connection connection)
			throws SQLException, RequiredValueNotFoundException {
		storeRelationEventsToDatabase(events, autnumId, connection, AUTNUM_STORE_QUERY);
	}

	public static void storeDsDataEventsToDatabase(List<Event> events, Long dsDataId, Connection connection)
			throws SQLException, RequiredValueNotFoundException {
		storeRelationEventsToDatabase(events, dsDataId, connection, DS_DATA_STORE_QUERY);
	}

	public static void storeKeyDataEventsToDatabase(List<Event> events, Long keyDataId, Connection connection)
			throws SQLException, RequiredValueNotFoundException {
		storeRelationEventsToDatabase(events, keyDataId, connection, KEY_DATA_STORE_QUERY);
	}

	public static void storeIpNetworkEventsToDatabase(List<Event> events, Long ipNetworkId, Connection connection)
			throws SQLException, RequiredValueNotFoundException {
		storeRelationEventsToDatabase(events, ipNetworkId, connection, IP_NETWORK_STORE_QUERY);
	}

	private static void storeRelationEventsToDatabase(List<Event> events, Long id, Connection connection,
			String storeQueryId) throws SQLException, RequiredValueNotFoundException {
		if (events.isEmpty())
			return;

		String query = getQueryGroup().getQuery(storeQueryId);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			for (Event event : events) {
				Long eventId = EventModel.storeToDatabase(event, connection);
				statement.setLong(1, id);
				statement.setLong(2, eventId);
				logger.log(Level.INFO, "Excuting QUERY:" + statement.toString());
				statement.executeUpdate();
			}
		}
	}

	public static List<Event> getByNameServerId(Long nameserverId, Connection connection) throws SQLException {
		return getByRelationId(nameserverId, connection, NAMESERVER_GET_QUERY);
	}

	public static List<Event> getByDsDataId(Long dsDataId, Connection connection) throws SQLException {
		return getByRelationId(dsDataId, connection, DS_DATA_GET_QUERY);
	}

	public static List<Event> getByKeyDataId(Long keyDataId, Connection connection) throws SQLException {
		return getByRelationId(keyDataId, connection, KEY_DATA_GET_QUERY);
	}

	public static List<Event> getByDomainId(Long domainId, Connection connection) throws SQLException {
		return getByRelationId(domainId, connection, DOMAIN_GET_QUERY);
	}

	public static List<Event> getByAutnumId(Long autnumId, Connection connection) throws SQLException {
		return getByRelationId(autnumId, connection, AUTNUM_GET_QUERY);
	}

	public static List<Event> getByEntityId(Long entityId, Connection connection) throws SQLException {
		return getByRelationId(entityId, connection, ENTITY_GET_QUERY);
	}

	public static List<Event> getByIpNetworkId(Long ipNetworkId, Connection connection) throws SQLException {
		return getByRelationId(ipNetworkId, connection, IP_NETWORK_GET_QUERY);
	}

	private static List<Event> getByRelationId(Long id, Connection connection, String getQueryId) throws SQLException {
		String query = getQueryGroup().getQuery(getQueryId);
		List<Event> result = null;

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, id);
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					return Collections.emptyList();
				}
				List<Event> events = new ArrayList<Event>();
				do {
					EventDbObj event = new EventDbObj(resultSet);
					event.getLinks().addAll(LinkModel.getByEventId(event.getId(), connection));
					events.add(event);
				} while (resultSet.next());
				result = events;
			}
		}

		return result;
	}

	public static List<Event> getAll(Connection connection) throws SQLException {
		String query = getQueryGroup().getQuery("getAll");
		List<Event> result = null;
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					return Collections.emptyList();
				}
				List<Event> events = new ArrayList<Event>();
				do {
					EventDbObj event = new EventDbObj(resultSet);
					event.getLinks().addAll(LinkModel.getByEventId(event.getId(), connection));
					events.add(event);
				} while (resultSet.next());
				result = events;
			}
		}

		return result;
	}

}
