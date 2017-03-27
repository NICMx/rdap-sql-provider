package mx.nic.rdap.db;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

import mx.nic.rdap.db.exception.InvalidConfigurationException;

public class SQLProviderConfiguration {

	/**
	 * Path to developer sql-provider-configuration file.
	 */
	private final static String CONFIG_FILE = Paths.get("META-INF", "sql_provider_configuration.properties").toString();

	private static Properties configuration;

	// Keys for the configuration file
	private final static String SCHEMA_KEY = "schema";

	private final static String MIGRATION_SCHEMA_KEY = "migration_schema";

	private final static String DYNAMIC_SCHEMA_KEY = "is_dynamic_schema";

	private final static String SCHEMA_TIMER_KEY = "schema_timer_time";

	private static void commonInit(Properties properties) {
		init();

		String schemaKey = properties.getProperty(SCHEMA_KEY);
		if (schemaKey != null) {
			configuration.setProperty(SCHEMA_KEY, schemaKey);
		}

		String dynamicSchemaKey = properties.getProperty(DYNAMIC_SCHEMA_KEY);
		if (dynamicSchemaKey != null) {
			configuration.setProperty(DYNAMIC_SCHEMA_KEY, dynamicSchemaKey);
		}
	}

	public static void initForServer(Properties serverProperties) {
		commonInit(serverProperties);

		String schemaTimer = serverProperties.getProperty(SCHEMA_TIMER_KEY);
		if (schemaTimer != null) {
			configuration.setProperty(SCHEMA_TIMER_KEY, schemaTimer);
		}
		validateConfigurationForServer();
	}

	public static void initForMigrator(Properties migratorProperties) {
		commonInit(migratorProperties);

		String secondSchemaName = migratorProperties.getProperty(MIGRATION_SCHEMA_KEY);
		if (secondSchemaName != null) {
			configuration.setProperty(MIGRATION_SCHEMA_KEY, secondSchemaName);
		}

		validateConfigurationForMigrator();
	}

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

	private static void commonValidation() {
		validateRequiredProperty(SCHEMA_KEY);
		validateRequiredProperty(DYNAMIC_SCHEMA_KEY);
		validateBooleanValue(DYNAMIC_SCHEMA_KEY, configuration.getProperty(DYNAMIC_SCHEMA_KEY));
	}

	private static void validateConfigurationForServer() {
		commonValidation();
		if (isDynamicSchemaOn()) {
			validateRequiredProperty(SCHEMA_TIMER_KEY);
			validateLongValue(SCHEMA_TIMER_KEY, configuration.getProperty(SCHEMA_TIMER_KEY));
		}

	}

	private static void validateConfigurationForMigrator() {
		commonValidation();
		validateRequiredProperty(MIGRATION_SCHEMA_KEY);
		if (getDefaultSchema().equalsIgnoreCase(getMigrationSchema())) {
			throw new InvalidConfigurationException(SCHEMA_KEY + " and " + MIGRATION_SCHEMA_KEY
					+ "have the same values, please provide diferent schema names");
		}
	}

	private static void validateLongValue(String key, String longValue) {
		try {
			Long.parseLong(longValue);
		} catch (NumberFormatException e) {
			throw new InvalidConfigurationException("Invalid value in " + key, e);
		}
	}

	private static void validateBooleanValue(String key, String booleanValue) {
		String bValue = booleanValue.toUpperCase();
		if (!(bValue.equals("TRUE") || bValue.equals("FALSE"))) {
			throw new InvalidConfigurationException("Invalid boolean value '" + key + "' : " + booleanValue);
		}
	}

	private static void validateRequiredProperty(String key) {
		if (configuration.getProperty(key) == null) {
			throw new InvalidConfigurationException("Required property not found : " + key);
		}
	}

	public static String getDefaultSchema() {
		return configuration.getProperty(SCHEMA_KEY).trim();
	}

	public static String getMigrationSchema() {
		return configuration.getProperty(MIGRATION_SCHEMA_KEY).trim();
	}

	public static Boolean isDynamicSchemaOn() {
		return Boolean.parseBoolean(configuration.getProperty(DYNAMIC_SCHEMA_KEY));
	}

	public static long getSchemaTimer() {
		return Long.parseLong(configuration.getProperty(SCHEMA_TIMER_KEY));
	}

}
