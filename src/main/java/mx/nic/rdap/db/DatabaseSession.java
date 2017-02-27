package mx.nic.rdap.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import mx.nic.rdap.db.exception.InitializationException;
import mx.nic.rdap.db.service.DataAccessService;

/**
 * Manage the Datasource used in the database connection
 */
public class DatabaseSession {

	private static final Logger logger = Logger.getLogger(DataAccessService.class.getName());

	/** From the migrator's perspective, this is the "destination" database. */
	private static DataSource rdapDataSource;
	/** From the migrator's perspective, this is the "source" database. */
	private static DataSource originDataSource;

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

	public static void initRdapConnection(Properties config) throws InitializationException {
		/*
		 * This code is rather awkward, I admit.
		 * 
		 * The problem is, at compile time, the application does not know the
		 * data access implementation, and the data access implementation does
		 * not know the application.
		 * 
		 * So we don't know for sure where the data source can be found, and the
		 * application cannot tell us. There is no interface for the application
		 * to provide us with a data source, because the data source is OUR
		 * problem. From the app's perspective, it might not even exist.
		 * 
		 * So what we're going to do is probe the candidate locations and stick
		 * with what works.
		 */

		try {
			Context initContext = new InitialContext();
			// TODO Make the string lookup a property variable
			rdapDataSource = (DataSource) initContext.lookup("java:comp/env/jdbc/rdap");
			logger.info("Found a data source in the context.");
			return;
		} catch (NamingException e) {
			logger.info("I could not find the RDAP data source in the context. "
					+ "This won't be a problem if I can find it in the configuration. Attempting that now... ");
		}

		rdapDataSource = loadDataSourceFromProperties(config);
	}

	public static void initOriginConnection(Properties config) throws InitializationException {
		originDataSource = loadDataSourceFromProperties(config);
	}

	private static DataSource loadDataSourceFromProperties(Properties config) throws InitializationException {
		String driverClassName = config.getProperty("driverClassName");
		String url = config.getProperty("url");
		if (driverClassName == null || url == null) {
			throw new InitializationException("I can't find a data source in the configuration.");
		}

		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(config.getProperty("userName"));
		dataSource.setPassword(config.getProperty("password"));
		dataSource.setDefaultAutoCommit(false);

		try {
			testDatabase(dataSource);
		} catch (SQLException e) {
			throw new InitializationException("The database connection test yielded failure.", e);
		}

		return dataSource;
	}

	public static void closeRdapDataSource() throws SQLException {
		if (rdapDataSource instanceof BasicDataSource) {
			((BasicDataSource) rdapDataSource).close();
		}
	}

	public static void closeOriginDataSource() throws SQLException {
		if (originDataSource instanceof BasicDataSource) {
			((BasicDataSource) originDataSource).close();
		}
	}
}
