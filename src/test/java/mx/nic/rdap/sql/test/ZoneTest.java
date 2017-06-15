/**
 * 
 */
package mx.nic.rdap.sql.test;

import java.sql.SQLException;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import mx.nic.rdap.sql.model.ZoneModel;
import mx.nic.rdap.store.model.ZoneStoreModel;

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
	public void insertAndGetBy() throws SQLException {
		Random random = new Random();
		int randomInt = random.nextInt();

		String zoneName = "example" + randomInt + ".mx";
		Integer zoneId = ZoneStoreModel.storeToDatabase(zoneName, connection);
		String byId = ZoneModel.getZoneNameById(zoneId);

		Assert.assertTrue("Get by Id fails", zoneName.equals(byId));
	}

}