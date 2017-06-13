package mx.nic.rdap.sql;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.sql.exception.InvalidConfigurationException;

public class SQLProviderConfiguration {

	private static final Logger logger = Logger.getLogger(SQLProviderConfiguration.class.getName());

	/**
	 * Path to developer sql-provider-configuration file.
	 */
	private final static String CONFIG_FILE = Paths.get("META-INF", "sql_provider_configuration.properties").toString();

	private static Properties configuration;

	// Keys for the configuration file
	private final static String SCHEMA_KEY = "schema";
	private final static String USER_SQL_FILES_KEY = "sql_files_directory";

	// Default Path in the META-INF folder of the server, to be use for the user
	// to write SQL files.
	public final static String DEFAULT_USER_SQL_FILE_PATH = "user_sql_files/";

	/**
	 * User custom path from the root of the disk
	 */
	private static String userSQLFilePath;

	/**
	 * Indicates if the user write SQL files.
	 */
	private static boolean isUserSQL;

	public static void initForServer(Properties serverProperties) {
		init();

		String schemaKey = serverProperties.getProperty(SCHEMA_KEY);
		if (schemaKey != null) {
			configuration.setProperty(SCHEMA_KEY, schemaKey);
		}

		validateRequiredProperty(SCHEMA_KEY);

		// checks if the user puts sql files outside of the project
		String property = serverProperties.getProperty(USER_SQL_FILES_KEY);
		if (property != null) {
			userSQLFilePath = property.trim();
			validateUserSQLFilesPath(getUserSQLFilesPath());

			isUserSQL = true;
			logger.log(Level.INFO, "Custom SQL files will be read from '" + getUserSQLFilesPath() + "'.");
		} else {
			isUserSQL = false;
		}

		// checks if the user puts custom SQL files inside the deployed project.
		if (isUserSQL == false) {
			URL resource = SQLProviderConfiguration.class.getClassLoader().getResource(DEFAULT_USER_SQL_FILE_PATH);
			if (resource != null) {
				// The user put custom SQL files inside the deployed projects.
				isUserSQL = true;
				logger.log(Level.INFO, "Custom SQL files will be read from '" + DEFAULT_USER_SQL_FILE_PATH + "'.");
			} else {
				logger.log(Level.INFO, "Default SQL files will be loaded.");
			}
		}
	}

	private static void validateUserSQLFilesPath(String pathString) {
		Path path = Paths.get(pathString);
		if (Files.notExists(path)) {
			NoSuchFileException e = new NoSuchFileException(pathString);
			throw new RuntimeException(e);
		}

		if (!Files.isDirectory(path)) {
			Exception e = new NotDirectoryException(pathString);
			throw new RuntimeException(e);
		}
	}

	private static void init() {
		// ClassLoader classLoader =
		// SQLProviderConfiguration.class.getClassLoader();
		
		Properties p = new Properties();
		p.setProperty("schema", "rdap");

		// try (InputStream ins = classLoader.getResourceAsStream(CONFIG_FILE))
		// {
		// p.load(ins);
		// } catch (IOException e) {
		// throw new RuntimeException("Error while reading the
		// SQLProviderConfiguration File", e);
		// }

		configuration = p;
	}

	private static void validateRequiredProperty(String key) {
		if (configuration.getProperty(key) == null) {
			throw new InvalidConfigurationException("Required property not found : " + key);
		}
	}

	public static String getDefaultSchema() {
		return configuration.getProperty(SCHEMA_KEY).trim();
	}

	/**
	 * @return User custom path from the root of the disk, null if using default
	 *         path for user SQL files.
	 */
	public static String getUserSQLFilesPath() {
		return userSQLFilePath;
	}

	/**
	 * @return <code>true</code> if the user writes SQL files, otherwise
	 *         <code>false</code>.
	 */
	public static boolean isUserSQL() {
		return isUserSQL;
	}
}
