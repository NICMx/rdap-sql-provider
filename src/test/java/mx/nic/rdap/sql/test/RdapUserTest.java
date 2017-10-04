package mx.nic.rdap.sql.test;

import java.sql.SQLException;

import org.junit.Test;

import mx.nic.rdap.sql.model.RdapUserModel;
import mx.nic.rdap.sql.objects.RdapAccessRoleDbObj;
import mx.nic.rdap.sql.objects.RdapUserDbObj;
import mx.nic.rdap.store.model.RdapAccessRoleStoreModel;
import mx.nic.rdap.store.model.RdapUserStoreModel;

/**
 * Test for the rdapuserDAO class
 * 
 */
public class RdapUserTest extends DatabaseTest {

	private String userName = "Test2";
	private String pass = "12345678A";
	private Integer maxSearchResult = 1;
	private String accessRoleName = "AUTHENTICATED";
	private String accessRoleDescription = "Authenticated users";

	@Test
	public void storeToDatabase() throws SQLException {
		RdapUserDbObj user = new RdapUserDbObj();
		user.setName(userName);
		user.setPass(pass);
		user.setMaxSearchResults(maxSearchResult);
		RdapUserStoreModel.storeToDatabase(user, connection);
	}

	@Test
	public void getByName() throws SQLException {
		RdapUserDbObj user = new RdapUserDbObj();
		user.setName(userName);
		user.setPass(pass);
		user.setMaxSearchResults(maxSearchResult);
		RdapUserStoreModel.storeToDatabase(user, connection);
		RdapUserModel.getByName(userName, connection);
	}

	@Test
	public void storeToDatabaseWithRole() throws SQLException {
		RdapAccessRoleDbObj accessRole = new RdapAccessRoleDbObj();
		accessRole.setName(accessRoleName);
		accessRole.setDescription(accessRoleDescription);
		RdapAccessRoleStoreModel.storeRdapAccessRoleToDatabase(accessRole, connection);

		RdapUserDbObj user = new RdapUserDbObj();
		user.setName(userName);
		user.setPass(pass);
		user.setMaxSearchResults(maxSearchResult);
		user.getAccessRoles().add(accessRole.getName());
		RdapUserStoreModel.storeToDatabase(user, connection);
	}

}
