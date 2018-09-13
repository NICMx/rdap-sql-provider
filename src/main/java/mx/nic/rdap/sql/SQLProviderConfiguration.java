package mx.nic.rdap.sql;

import java.io.IOException;
import java.io.InputStream;
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
	private static final String ZONES_KEY = "zones";
	private static final String IS_REVERSE_IPV4_ENABLED_KEY = "is_reverse_ipv4_enabled";
	private static final String IS_REVERSE_IPV6_ENABLED_KEY = "is_reverse_ipv6_enabled";
	private final static String USER_SQL_FILES_KEY = "sql_files_directory";
	private final static String IS_NS_SHARING_NAME_ENABLED_KEY = "is_ns_sharing_name_enabled";

	/**
	 * Default Path in the META-INF folder of the server, to be use for the user
	 * to write SQL files.
	 */
	public final static String DEFAULT_USER_SQL_FILE_PATH = "user_sql_files/";

	/**
	 * User custom path from the root of the disk
	 */
	private static String userSQLFilePath;

	/**
	 * Indicates if the user write SQL files.
	 */
	private static boolean isUserSQL;

	/**
	 * Schema that will be used at the database
	 */
	private static String schema;

	/**
	 * Managed zones by the server
	 */
	private static String zones;

	/**
	 * Indicate if the reverse ipv4 domain search is enabled
	 */
	private static boolean isReverseIpv4Enabled;

	/**
	 * Indicate if the reverse ipv6 domain search is enabled
	 */
	private static boolean isReverseIpv6Enabled;

	/**
	 * Indicate if the nameserver-sharing-name conformance is active
	 */
	private static boolean isNsSharingNameEnabled;

	/**
	 * Load properties configured from the server. The default properties are loaded
	 * first, if the server has any of the expected properties then the default
	 * value is overwritten.
	 * 
	 * @param serverProperties
	 *            Properties configured by the server
	 */
	public static void initForServer(Properties serverProperties) {
		init();

		schema = getStringProperty(serverProperties, SCHEMA_KEY);
		zones = getStringProperty(serverProperties, ZONES_KEY);
		isReverseIpv4Enabled = getBooleanProperty(serverProperties, IS_REVERSE_IPV4_ENABLED_KEY);
		isReverseIpv6Enabled = getBooleanProperty(serverProperties, IS_REVERSE_IPV6_ENABLED_KEY);
		isNsSharingNameEnabled = getBooleanProperty(serverProperties, IS_NS_SHARING_NAME_ENABLED_KEY);

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

	/**
	 * Load the properties default values
	 */
	private static void init() {
		ClassLoader classLoader = SQLProviderConfiguration.class.getClassLoader();
		Properties p = new Properties();

		try (InputStream ins = classLoader.getResourceAsStream(CONFIG_FILE)) {
			p.load(ins);
		} catch (IOException e) {
			throw new RuntimeException("Error while reading the SQLProviderConfiguration File", e);
		}

		configuration = p;
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

	private static void validateRequiredProperty(String key) {
		if (configuration.getProperty(key) == null) {
			throw new InvalidConfigurationException("Required property not found : " + key);
		}
		if (configuration.getProperty(key).trim().isEmpty()) {
			throw new InvalidConfigurationException("Required property '" + key + "' can't have a null or empty value");
		}
	}

	/**
	 * Loads to 'configuration' properties, validates and return the value of a
	 * String property from the properties sent as parameter, throws an
	 * {@link InvalidConfigurationException} if there's an error
	 * 
	 * @param serverProperties
	 *            Properties from where the property can be loaded
	 * @param propertyKey
	 *            Property key
	 * @return String value of the property
	 */
	private static String getStringProperty(Properties serverProperties, String propertyKey) {
		String propertyAsString = serverProperties.getProperty(propertyKey);
		if (propertyAsString != null) {
			configuration.setProperty(propertyKey, propertyAsString);
		}
		validateRequiredProperty(propertyKey);
		return configuration.getProperty(propertyKey).trim();
	}

	/**
	 * Loads to 'configuration' properties, validates and return the value of a
	 * boolean property from the properties sent as parameter, throws an
	 * {@link InvalidConfigurationException} if there's an error
	 * 
	 * @param serverProperties
	 *            Properties from where the property can be loaded
	 * @param propertyKey
	 *            Property key
	 * @return boolean value of the property
	 */
	private static boolean getBooleanProperty(Properties serverProperties, String propertyKey) {
		String propertyAsString = serverProperties.getProperty(propertyKey);
		if (propertyAsString != null) {
			configuration.setProperty(propertyKey, propertyAsString);
		}
		validateRequiredProperty(propertyKey);
		propertyAsString = configuration.getProperty(propertyKey).trim();
		if (propertyAsString.equalsIgnoreCase("true")) {
			return true;
		} else if (propertyAsString.equalsIgnoreCase("false")) {
			return false;
		}
		throw new InvalidConfigurationException("Property '" + propertyKey + "' has an invalid value '"
				+ propertyAsString + "', must be 'true' or 'false'.");
	}

	/**
	 * @return Schema that will be used at the database
	 */
	public static String getDefaultSchema() {
		return schema;
	}

	/**
	 * @return Zones managed by the server
	 */
	public static String getZones() {
		return zones;
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

	/**
	 * @return <code>true</code> if the reverse ipv4 domain search is enabled,
	 *         otherwise <code>false</code>.
	 */
	public static boolean isReverseIpv4Enabled() {
		return isReverseIpv4Enabled;
	}

	/**
	 * @return <code>true</code> if the reverse ipv6 domain search is enabled,
	 *         otherwise <code>false</code>.
	 */
	public static boolean isReverseIpv6Enabled() {
		return isReverseIpv6Enabled;
	}

	/**
	 * @return <code>true</code> If the user enable the ns-sharing-name conformance,
	 *         otherwise <code>false</code>
	 */
	public static boolean isNsSharingNameEnabled() {
		return isNsSharingNameEnabled;
	}
}
