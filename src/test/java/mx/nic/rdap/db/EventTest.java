package mx.nic.rdap.db;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import mx.nic.rdap.core.catalog.EventAction;
import mx.nic.rdap.core.db.Event;
import mx.nic.rdap.core.db.Link;
import mx.nic.rdap.db.model.EventModel;
import mx.nic.rdap.db.objects.EventDbObj;
import mx.nic.rdap.db.objects.LinkDbObj;

/**
 * Test for the class Event
 *
 */
public class EventTest extends DatabaseTest {

	@Test
	/**
	 * Store an event in the database
	 */
	public void insert() throws SQLException {
		Event event = new EventDbObj();
		event.setEventAction(EventAction.DELETION);
		event.setEventDate(new Date());
		event.setEventActor("dalpuche");
		Link link = new LinkDbObj();
		link.setValue("linkofevent.com");
		link.setHref("lele");
		event.getLinks().add(link);
		EventModel.storeToDatabase(event, connection);
	}

	// XXX Is this test necessary? @Test
	/**
	 * Test that retrieve an array of events from the DB
	 */
	public void getAll() throws SQLException {
		List<Event> events = EventModel.getAll(connection);
		for (Event event : events) {
			System.out.println(event.toString());
		}
	}
}
