package mx.nic.rdap.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import mx.nic.rdap.db.exception.InitializationException;
import mx.nic.rdap.db.model.CountryCodeModel;
import mx.nic.rdap.db.model.ZoneModel;

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
		try (Connection con = DatabaseSession.getRdapConnection();) {
			ZoneModel.loadAllFromDatabase(con);
			CountryCodeModel.loadAllFromDatabase(con);
		}
	}

	@AfterClass
	public static void end() throws SQLException {
		DatabaseSession.closeRdapDataSource();
	}

}
