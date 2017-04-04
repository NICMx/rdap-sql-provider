package mx.nic.rdap.sql;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import mx.nic.rdap.core.db.Link;
import mx.nic.rdap.sql.model.LinkModel;
import mx.nic.rdap.sql.objects.LinkDbObj;

/**
 * Test for the class link
 * 
 */
public class LinkTest extends DatabaseTest {

	@Test
	/**
	 * Store a link in the database
	 */
	public void insert() throws SQLException {
		Link link = new LinkDbObj();
		link.setValue("spotify.com");
		link.setHref("test");
		LinkModel.storeToDatabase(link, connection);
	}

	// XXX Is this test necessary? @Test
	public void getAll() throws SQLException {
		List<Link> links = LinkModel.getAll(connection);
		for (Link link : links) {
			System.out.println(link.toString());
		}
	}
}
