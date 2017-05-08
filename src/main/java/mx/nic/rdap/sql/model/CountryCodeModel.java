package mx.nic.rdap.sql.model;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.SQLProviderConfiguration;

/**
 * Model for the CountryCode table, read all CountryCodes in the
 * country_code_table and keeps it in memory for quickly access.
 *
 */

public class CountryCodeModel {

	private final static String COUNTRIES_CODE_FILE = "META-INF/country_codes.properties";

	private final static String QUERY_GROUP = "CountryCode";

	private static QueryGroup queryGroup = null;

	private static Map<Integer, String> countryNameById;
	private static Map<String, Integer> idByCountryName;
	private static final String GET_ALL_QUERY = "getAll";

	public static void loadQueryGroup(String schema) {
		try {
			QueryGroup qG = new QueryGroup(QUERY_GROUP, schema);
			setQueryGroup(qG);
		} catch (IOException e) {
			throw new RuntimeException("Error loading query group");
		}
	}

	private static void setQueryGroup(QueryGroup qG) {
		queryGroup = qG;
	}

	private static QueryGroup getQueryGroup() {
		return queryGroup;
	}

	/**
	 * Load all the country codes stored in the database.
	 * 
	 */
	public static void loadAllFromDatabase(Connection con) throws SQLException {
		countryNameById = new HashMap<Integer, String>();
		idByCountryName = new HashMap<String, Integer>();

		String query = getQueryGroup().getQuery(GET_ALL_QUERY);
		if (SQLProviderConfiguration.isUserSQL() && query == null) {
			loadFromProperties();
			return;
		}
		PreparedStatement statement = con.prepareStatement(query);
		ResultSet rs = statement.executeQuery();
		if (!rs.next()) {
			return;
		}

		do {
			Integer id = rs.getInt("ccd_id");
			String countryName = rs.getString("ccd_code");
			countryNameById.put(id, countryName);
			idByCountryName.put(countryName, id);
		} while (rs.next());

		Logger.getLogger(CountryCodeModel.class.getName()).log(Level.INFO,
				"Loaded " + countryNameById.size() + " country codes.");
	}

	private static void loadFromProperties() {
		Properties p = new Properties();
		try (InputStream in = CountryCodeModel.class.getClassLoader().getResourceAsStream(COUNTRIES_CODE_FILE)) {
			p.load(in);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		for (Entry<Object, Object> entry : p.entrySet()) {
			Integer key = Integer.parseInt(((String) entry.getKey()));
			String value = (String) entry.getValue();
			countryNameById.put(key, value);
			idByCountryName.put(value, key);
		}
	}

	public static String getCountryNameById(Integer id) {
		return countryNameById.get(id);
	}

	public static Integer getIdByCountryName(String countryName) {
		return idByCountryName.get(countryName);
	}
}
