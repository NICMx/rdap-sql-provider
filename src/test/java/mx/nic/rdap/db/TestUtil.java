package mx.nic.rdap.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestUtil {

	/**
	 * Loads the properties configuration file
	 * <code>[classpath]/config/fileName.properties</code> and returns it.
	 * 
	 * @param fileName
	 *            name of the configuration file you want to load.
	 * @return configuration requested.
	 * @throws IOException
	 *             Error attempting to read the configuration out of the
	 *             classpath.
	 */
	public static Properties loadProperties(String fileName) throws IOException {
		fileName = "config/" + fileName + ".properties";
		
		Properties result = new Properties();
		try (InputStream configStream = TestUtil.class.getClassLoader().getResourceAsStream(fileName)) {
 			result.load(configStream);
 		}

 		return result;
	}

}
