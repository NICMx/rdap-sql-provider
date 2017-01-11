/**
 * 
 */
package mx.nic.rdap.db;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import mx.nic.rdap.db.exception.RequiredValueNotFoundException;
import mx.nic.rdap.db.model.ZoneModel;

/**
 * Tests for the {@link ZoneModel}
 * 
 */
public class ZoneTest extends DatabaseTest {

	@Test
	/**
	 * Creates a new Zone instance and stores it in the database, then it get an
	 * instance with the id generated
	 */
	public void insertAndGetBy() throws IOException, SQLException, RequiredValueNotFoundException {
		Random random = new Random();
		int randomInt = random.nextInt();

		String zoneName = "example" + randomInt + ".mx";
		Integer zoneId = null;

		try {
			zoneId = ZoneModel.storeToDatabase(zoneName, connection);
		} catch (SQLException e) {
			e.printStackTrace();
			fail();
		}

		String byId = ZoneModel.getZoneNameById(zoneId);

		Assert.assertTrue("Get by Id fails", zoneName.equals(byId));
	}

}