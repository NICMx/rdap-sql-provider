package mx.nic.rdap.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

/**
 * Manage the Datasource used in the database connection
 */
public class DatabaseSession {

	private static BasicDataSource rdapDataSource;
	private static BasicDataSource originDataSource;

	private static void testDatabase(BasicDataSource ds) throws SQLException {
		// http://stackoverflow.com/questions/3668506
		final String TEST_QUERY = "select 1";
		try (Connection connection = ds.getConnection(); Statement statement = connection.createStatement();) {
			ResultSet resultSet = statement.executeQuery(TEST_QUERY);

			if (!resultSet.next()) {
				throw new SQLException("'" + TEST_QUERY + "' returned no rows.");
			}
			int result = resultSet.getInt(1);
			if (result != 1) {
				throw new SQLException("'" + TEST_QUERY + "' returned " + result);
			}
		}
	}

	public static Connection getRdapConnection() throws SQLException {
		return rdapDataSource.getConnection();
	}

	public static Connection getMigrationConnection() throws SQLException {
		return originDataSource.getConnection();
	}

	public static void initRdapConnection(Properties config) throws SQLException {
		rdapDataSource = new BasicDataSource();
		rdapDataSource.setDriverClassName(config.getProperty("driverClassName"));
		rdapDataSource.setUrl(config.getProperty("url"));
		rdapDataSource.setUsername(config.getProperty("userName"));
		rdapDataSource.setPassword(config.getProperty("password"));
		rdapDataSource.setDefaultAutoCommit(false);
		testDatabase(rdapDataSource);
	}

	public static void initOriginConnection(Properties config) throws SQLException {
		originDataSource = new BasicDataSource();
		originDataSource.setDriverClassName(config.getProperty("driverClassName"));
		originDataSource.setUrl(config.getProperty("url"));
		originDataSource.setUsername(config.getProperty("userName"));
		originDataSource.setPassword(config.getProperty("password"));
		originDataSource.setDefaultAutoCommit(false);
		testDatabase(originDataSource);
	}

	public static DataSource getRdapDataSource(){
		return rdapDataSource;
	}
	
	public static void closeRdapDataSource() throws SQLException {
		rdapDataSource.close();
	}

	public static void closeOriginDataSource() throws SQLException {
		originDataSource.close();
	}
}
