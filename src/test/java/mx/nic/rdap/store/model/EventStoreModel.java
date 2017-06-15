
package mx.nic.rdap.store.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.db.Event;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.exception.IncompleteObjectException;
import mx.nic.rdap.sql.exception.InvalidObjectException;

/**
 * The model for {@link Event} Object
 * 
 */
public class EventStoreModel {

	private final static Logger logger = Logger.getLogger(RemarkStoreModel.class.getName());

	private final static String QUERY_GROUP = "EventStore";

	private static QueryGroup queryGroup = null;

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

	private static void isValidForStore(Event event) throws InvalidObjectException {
		if (event.getEventAction() == null)
			throw new IncompleteObjectException("eventAction", "Event");
		if (event.getEventDate() == null)
			throw new IncompleteObjectException("eventDate", "Event");
	}

	private static long storeToDatabase(Event event, Connection connection) throws SQLException {
		isValidForStore(event);
		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery("storeToDatabase"),
				Statement.RETURN_GENERATED_KEYS)) {
			fillPreparedStatement(statement, event);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			result.next();
			Long eventId = result.getLong(1);// The id of the link inserted
			event.setId(eventId);
			LinkStoreModel.storeEventLinksToDatabase(event.getLinks(), eventId, connection);
			return eventId;
		}
	}

	public static void storeNameserverEventsToDatabase(List<Event> events, Long nameserverId, Connection connection)
			throws SQLException {
		storeRelationEventsToDatabase(events, nameserverId, connection, NAMESERVER_STORE_QUERY);
	}

	public static void storeEntityEventsToDatabase(List<Event> events, Long entityId, Connection connection)
			throws SQLException {
		storeRelationEventsToDatabase(events, entityId, connection, ENTITY_STORE_QUERY);
	}

	public static void storeDomainEventsToDatabase(List<Event> events, Long domainId, Connection connection)
			throws SQLException {
		storeRelationEventsToDatabase(events, domainId, connection, DOMAIN_STORE_QUERY);
	}

	public static void storeAutnumEventsToDatabase(List<Event> events, Long autnumId, Connection connection)
			throws SQLException {
		storeRelationEventsToDatabase(events, autnumId, connection, AUTNUM_STORE_QUERY);
	}

	public static void storeDsDataEventsToDatabase(List<Event> events, Long dsDataId, Connection connection)
			throws SQLException {
		storeRelationEventsToDatabase(events, dsDataId, connection, DS_DATA_STORE_QUERY);
	}

	public static void storeKeyDataEventsToDatabase(List<Event> events, Long keyDataId, Connection connection)
			throws SQLException {
		storeRelationEventsToDatabase(events, keyDataId, connection, KEY_DATA_STORE_QUERY);
	}

	public static void storeIpNetworkEventsToDatabase(List<Event> events, Long ipNetworkId, Connection connection)
			throws SQLException {
		storeRelationEventsToDatabase(events, ipNetworkId, connection, IP_NETWORK_STORE_QUERY);
	}

	private static void storeRelationEventsToDatabase(List<Event> events, Long id, Connection connection,
			String storeQueryId) throws SQLException {
		if (events.isEmpty())
			return;

		String query = getQueryGroup().getQuery(storeQueryId);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			for (Event event : events) {
				Long eventId = EventStoreModel.storeToDatabase(event, connection);
				statement.setLong(1, id);
				statement.setLong(2, eventId);
				logger.log(Level.INFO, "Excuting QUERY:" + statement.toString());
				statement.executeUpdate();
			}
		}
	}

	private static void fillPreparedStatement(PreparedStatement preparedStatement, Event event) throws SQLException {
		preparedStatement.setLong(1, event.getEventAction().getId());
		preparedStatement.setString(2, event.getEventActor());
		preparedStatement.setTimestamp(3, new Timestamp(event.getEventDate().getTime()));
	}
}
