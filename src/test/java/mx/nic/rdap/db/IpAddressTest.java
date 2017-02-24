package mx.nic.rdap.db;

import java.sql.SQLException;
import java.util.List;

import mx.nic.rdap.db.exception.ObjectNotFoundException;
import mx.nic.rdap.db.model.IpAddressModel;
import mx.nic.rdap.db.objects.IpAddressDbObj;

/**
 * Test for the class IpAddress
 * 
 */
public class IpAddressTest extends DatabaseTest {

	// XXX Is this test necessary?
	// @Test
	public void getAll() {
		try {
			List<IpAddressDbObj> addresses = IpAddressModel.getAll(connection);
			for (IpAddressDbObj ip : addresses) {
				System.out.println(ip.toString());
			}
			assert true;
		} catch (SQLException | ObjectNotFoundException e) {
			e.printStackTrace();
			assert false;
		}

	}
}
