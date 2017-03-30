/**
 * 
 */
package mx.nic.rdap.db.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.db.QueryGroup;
import mx.nic.rdap.db.exception.ObjectNotFoundException;

/**
 * Model for the Zone table, read all zones in the zone_table and keeps it in
 * memory for quickly access.
 */
public class ZoneModel {

	private final static Logger logger = Logger.getLogger(ZoneModel.class.getName());

	private final static String QUERY_GROUP = "Zone";
	private final static String GET_ALL_QUERY = "getAll";
	private final static String STORE_QUERY = "storeToDatabase";

	private static QueryGroup queryGroup = null;

	private static Map<Integer, String> zoneById;
	private static Map<String, Integer> idByZone;
	public static String REVERSE_IP_V4 = "in-addr.arpa";
	public static String REVERSE_IP_V6 = "ip6.arpa";

	private static final String ZONE_KEY = "zones";
	private static final String IS_REVERSE_IPV4_ENABLED_KEY = "is_reverse_ipv4_enabled";
	private static final String IS_REVERSE_IPV6_ENABLED_KEY = "is_reverse_ipv6_enabled";

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
	 * Validates the configured zones exist in the database.
	 */
	public static void validateConfiguredZones(Properties properties) throws ObjectNotFoundException {
		List<String> configuredZones;
		if (properties.containsKey(ZONE_KEY)) {
			String zones[] = properties.getProperty(ZONE_KEY).trim().split(",");
			List<String> trimmedZones = new ArrayList<String>();
			for (String zone : zones) {
				if (!zone.trim().isEmpty())
					trimmedZones.add(zone.trim());
			}
			configuredZones = trimmedZones;
		} else {
			configuredZones = new ArrayList<>();
		}
		Map<Integer, String> zoneByIdForServer = new HashMap<Integer, String>();
		Map<String, Integer> idByZoneForServer = new HashMap<String, Integer>();

		// Configure reverse zones
		if (Boolean.parseBoolean(properties.getProperty(IS_REVERSE_IPV4_ENABLED_KEY))) {
			String zone = ZoneModel.REVERSE_IP_V4;
			if (ZoneModel.getIdByZone().get(zone) != null) {
				zoneByIdForServer.put(ZoneModel.getIdByZone().get(zone), zone);
				idByZoneForServer.put(zone, ZoneModel.getIdByZoneName(zone));
			} else {
				logger.log(Level.WARNING, "Configured zone not found in database : " + zone);
			}
		}
		configuredZones.remove(ZoneModel.REVERSE_IP_V4);

		if (Boolean.parseBoolean(properties.getProperty(IS_REVERSE_IPV6_ENABLED_KEY))) {
			String zone = ZoneModel.REVERSE_IP_V6;
			if (ZoneModel.getIdByZone().get(zone) != null) {
				zoneByIdForServer.put(ZoneModel.getIdByZone().get(zone), zone);
				idByZoneForServer.put(zone, ZoneModel.getIdByZoneName(zone));
			} else {
				logger.log(Level.WARNING, "Configured zone not found in database : " + zone);
			}
		}
		configuredZones.remove(ZoneModel.REVERSE_IP_V6);

		for (String zone : configuredZones) {
			if (ZoneModel.getIdByZone().get(zone) == null) {
				logger.log(Level.SEVERE, "Configured zone not found in database:" + zone);
				throw new ObjectNotFoundException("Configured zone not found in database:" + zone);
			}
			zoneByIdForServer.put(ZoneModel.getIdByZone().get(zone), zone);
			idByZoneForServer.put(zone, ZoneModel.getIdByZoneName(zone));
		}

		// Ovewrite the hashmaps to only use the configured zones
		ZoneModel.setZoneById(zoneByIdForServer);
		ZoneModel.setIdByZone(idByZoneForServer);
	}

	public static void loadAllFromDatabase(Connection con) throws SQLException {
		zoneById = new HashMap<Integer, String>();
		idByZone = new HashMap<String, Integer>();

		String query = getQueryGroup().getQuery(GET_ALL_QUERY);

		PreparedStatement statement = con.prepareStatement(query);
		ResultSet rs = statement.executeQuery();
		if (!rs.next()) {
			return;
		}

		do {
			Integer zoneId = rs.getInt("zone_id");
			String zoneName = rs.getString("zone_name");
			zoneById.put(zoneId, zoneName);
			idByZone.put(zoneName, zoneId);
		} while (rs.next());

	}

	public static Integer storeToDatabase(String zoneName, Connection connection) throws SQLException {
		Integer idByZoneName = getIdByZoneName(zoneName);

		if (idByZoneName != null) {
			return idByZoneName;
		}

		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery(STORE_QUERY),
				Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, zoneName);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();
			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.next();
			// Inserted Zone's Id
			Integer zoneId = resultSet.getInt(1);
			zoneById.put(zoneId, zoneName);
			idByZone.put(zoneName, zoneId);

			return zoneId;
		}
	}

	public static void cleanZoneTables() {
		zoneById = new HashMap<Integer, String>();
		idByZone = new HashMap<String, Integer>();
	}

	/**
	 * Get zoneName from an id
	 * 
	 * @param id
	 *            identifier related to a zone.
	 * @return The ZoneName if the id is related to a zone, otherwise null.
	 */
	public static String getZoneNameById(Integer id) {
		return zoneById.get(id);
	}

	/**
	 * @param zoneName
	 *            Name of the zone
	 * @return The Id if the zone exists in the database, otherwise null.
	 */
	public static Integer getIdByZoneName(String zoneName) {
		return idByZone.get(zoneName);
	}

	/**
	 * @param zoneName
	 * @return Checks if the zoneName exists in the database.
	 */
	public static boolean existsZone(String zoneName) {
		return idByZone.containsKey(zoneName);
	}

	/**
	 * @param zoneId
	 * @return Checks if the id is related to a zone.
	 */
	public static boolean existsZoneById(Integer zoneId) {
		return zoneById.containsKey(zoneId);
	}

	/**
	 * validate if a address is in reverse lookup
	 * 
	 */
	public static boolean isReverseAddress(String address) {
		return address.trim().endsWith(REVERSE_IP_V4) || address.trim().endsWith(REVERSE_IP_V6);
	}

	public static String getAddressWithoutArpaZone(String address) {
		if (address.endsWith(REVERSE_IP_V4)) {
			return address.substring(0, address.length() - (REVERSE_IP_V4.length() + 1));
		}

		if (address.endsWith(REVERSE_IP_V6)) {
			return address.substring(0, address.length() - (REVERSE_IP_V6.length() + 1));
		}

		return address;
	}

	public static Integer getZoneIdForArpaZone(String address) {
		if (address.endsWith(REVERSE_IP_V4)) {
			return getIdByZoneName(REVERSE_IP_V4);
		}

		if (address.endsWith(REVERSE_IP_V6)) {
			return getIdByZoneName(REVERSE_IP_V6);
		}

		return null;
	}

	public static String getArpaZoneNameFromAddress(String address) {
		if (address.endsWith(REVERSE_IP_V4)) {
			return REVERSE_IP_V4;
		}

		if (address.endsWith(REVERSE_IP_V6)) {
			return REVERSE_IP_V6;
		}

		return null;
	}

	/**
	 * @return the zoneById
	 */
	public static Map<Integer, String> getZoneById() {
		return zoneById;
	}

	/**
	 * @param zoneById
	 *            the zoneById to set
	 */
	public static void setZoneById(Map<Integer, String> zoneById) {
		ZoneModel.zoneById = zoneById;
	}

	/**
	 * @return the idByZone
	 */
	public static Map<String, Integer> getIdByZone() {
		return idByZone;
	}

	/**
	 * @param idByZone
	 *            the idByZone to set
	 */
	public static void setIdByZone(Map<String, Integer> idByZone) {
		ZoneModel.idByZone = idByZone;
	}

	public static List<Integer> getValidZoneIds() {
		List<Integer> zoneIds = new ArrayList<Integer>(zoneById.keySet());
		return zoneIds;
	}

}
