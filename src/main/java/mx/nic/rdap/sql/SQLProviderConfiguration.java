package mx.nic.rdap.sql;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

import mx.nic.rdap.sql.exception.InvalidConfigurationException;

public class SQLProviderConfiguration {

	/**
	 * Path to developer sql-provider-configuration file.
	 */
	private final static String CONFIG_FILE = Paths.get("META-INF", "sql_provider_configuration.properties").toString();

	private static Properties configuration;

	// Keys for the configuration file
	private final static String SCHEMA_KEY = "schema";

	public static void initForServer(Properties serverProperties) {
		init();

		String schemaKey = serverProperties.getProperty(SCHEMA_KEY);
		if (schemaKey != null) {
			configuration.setProperty(SCHEMA_KEY, schemaKey);
		}

		validateRequiredProperty(SCHEMA_KEY);
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

	private static void validateRequiredProperty(String key) {
		if (configuration.getProperty(key) == null) {
			throw new InvalidConfigurationException("Required property not found : " + key);
		}
	}

	public static String getDefaultSchema() {
		return configuration.getProperty(SCHEMA_KEY).trim();
	}

}
