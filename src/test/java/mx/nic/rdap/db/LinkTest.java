package mx.nic.rdap.db;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import mx.nic.rdap.core.db.Link;
import mx.nic.rdap.db.exception.RequiredValueNotFoundException;
import mx.nic.rdap.db.model.LinkModel;
import mx.nic.rdap.db.objects.LinkDbObj;

/**
 * Test for the class link
 * 
 */
public class LinkTest extends DatabaseTest {

	@Test
	/**
	 * Store a link in the database
	 */
	public void insert() {
		try {

			Link link = new LinkDbObj();
			link.setValue("spotify.com");
			link.setHref("test");
			LinkModel.storeToDatabase(link, connection);
			assert true;
		} catch (RequiredValueNotFoundException | SQLException e) {
			e.printStackTrace();
			assert false;
		}
	}

	// XXX Is this test necessary? @Test
	public void getAll() throws IOException {
		try {
			List<Link> links = LinkModel.getAll(connection);
			for (Link link : links) {
				System.out.println(link.toString());
			}
			assert true;
		} catch (SQLException e) {
			e.printStackTrace();
			assert false;
		}

	}
}
