package mx.nic.rdap.db;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import mx.nic.rdap.db.exception.ObjectNotFoundException;
import mx.nic.rdap.db.model.IpAddressModel;
import mx.nic.rdap.db.objects.IpAddressDAO;

/**
 * Test for the class IpAddress
 * 
 */
public class IpAddressTest extends DatabaseTest {

	@Test
	public void getAll() {
		try {
			List<IpAddressDAO> addresses = IpAddressModel.getAll(connection);
			for (IpAddressDAO ip : addresses) {
				System.out.println(ip.toString());
			}
			assert true;
		} catch (SQLException | ObjectNotFoundException e) {
			assert false;
			e.printStackTrace();
		}

	}
}
