package mx.nic.rdap.sql.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import mx.nic.rdap.db.exception.InitializationException;
import mx.nic.rdap.sql.DatabaseSession;
import mx.nic.rdap.sql.SQLProviderConfiguration;
import mx.nic.rdap.sql.SchemaConfiguration;
import mx.nic.rdap.sql.model.CountryCodeModel;
import mx.nic.rdap.sql.model.ZoneModel;
import mx.nic.rdap.store.model.AutnumStoreModel;
import mx.nic.rdap.store.model.DomainStoreModel;
import mx.nic.rdap.store.model.DsDataStoreModel;
import mx.nic.rdap.store.model.EntityStoreModel;
import mx.nic.rdap.store.model.EventStoreModel;
import mx.nic.rdap.store.model.IpAddressStoreModel;
import mx.nic.rdap.store.model.IpNetworkStoreModel;
import mx.nic.rdap.store.model.KeyDataStoreModel;
import mx.nic.rdap.store.model.LinkStoreModel;
import mx.nic.rdap.store.model.NameserverStoreModel;
import mx.nic.rdap.store.model.PublicIdStoreModel;
import mx.nic.rdap.store.model.RdapUserRoleStoreModel;
import mx.nic.rdap.store.model.RdapUserStoreModel;
import mx.nic.rdap.store.model.RemarkDescriptionStoreModel;
import mx.nic.rdap.store.model.RemarkStoreModel;
import mx.nic.rdap.store.model.RoleStoreModel;
import mx.nic.rdap.store.model.SecureDNSStoreModel;
import mx.nic.rdap.store.model.StatusStoreModel;
import mx.nic.rdap.store.model.VCardPostalInfoStoreModel;
import mx.nic.rdap.store.model.VCardStoreModel;
import mx.nic.rdap.store.model.VariantStoreModel;
import mx.nic.rdap.store.model.ZoneStoreModel;

/**
 *
 */
public class DatabaseTest {

	/**
	 * Connection for this tests
	 */
	public static Connection connection = null;
	private static String rdapDatabaseConfigurationFile = "database";

	@Before
	public void before() throws SQLException {
		connection = DatabaseSession.getRdapConnection();
		// ZoneModel.loadAllFromDatabase(connection);
		// CountryCodeModel.loadAllFromDatabase(connection);
	}

	@After
	public void after() throws SQLException {
		connection.close();
	}

	@BeforeClass
	public static void init() throws SQLException, IOException, InitializationException {
		DatabaseSession.initRdapConnection(TestUtil.loadProperties(rdapDatabaseConfigurationFile));
		SchemaConfiguration.init(new Properties());
		initAllStoreModels();
		try (Connection con = DatabaseSession.getRdapConnection();) {
			ZoneModel.loadAllFromDatabase(con);
			CountryCodeModel.loadAllFromDatabase(con);
		}
	}

	private static void initAllStoreModels() {
		String schema = SQLProviderConfiguration.getDefaultSchema();

		AutnumStoreModel.loadQueryGroup(schema);
		DomainStoreModel.loadQueryGroup(schema);
		DsDataStoreModel.loadQueryGroup(schema);
		EntityStoreModel.loadQueryGroup(schema);
		EventStoreModel.loadQueryGroup(schema);
		IpAddressStoreModel.loadQueryGroup(schema);
		IpNetworkStoreModel.loadQueryGroup(schema);
		KeyDataStoreModel.loadQueryGroup(schema);
		LinkStoreModel.loadQueryGroup(schema);
		NameserverStoreModel.loadQueryGroup(schema);
		PublicIdStoreModel.loadQueryGroup(schema);
		RdapUserRoleStoreModel.loadQueryGroup(schema);
		RdapUserStoreModel.loadQueryGroup(schema);
		RemarkDescriptionStoreModel.loadQueryGroup(schema);
		RemarkStoreModel.loadQueryGroup(schema);
		RoleStoreModel.loadQueryGroup(schema);
		SecureDNSStoreModel.loadQueryGroup(schema);
		StatusStoreModel.loadQueryGroup(schema);
		VariantStoreModel.loadQueryGroup(schema);
		VCardPostalInfoStoreModel.loadQueryGroup(schema);
		VCardStoreModel.loadQueryGroup(schema);
		ZoneStoreModel.loadQueryGroup(schema);

	}

	@AfterClass
	public static void end() throws SQLException {
		DatabaseSession.closeRdapDataSource();
	}

}
