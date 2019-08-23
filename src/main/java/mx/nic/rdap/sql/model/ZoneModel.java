/**
 * 
 */
package mx.nic.rdap.sql.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.db.DomainLabel;
import mx.nic.rdap.core.db.DomainLabelException;
import mx.nic.rdap.db.exception.InitializationException;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.SQLProviderConfiguration;
import mx.nic.rdap.sql.exception.ObjectNotFoundException;

/**
 * Model for the Zone table, read all zones in the zone_table and keeps it in
 * memory for quickly access.
 */
public class ZoneModel {

	private final static Logger logger = Logger.getLogger(ZoneModel.class.getName());

	private final static String QUERY_GROUP = "Zone";
	private final static String GET_ALL_QUERY = "getAll";

	private static QueryGroup queryGroup = null;

	private static Map<Integer, String> zoneById;
	private static Map<String, Integer> idByZone;
	public static final String REVERSE_IP_V4 = "in-addr.arpa";
	public static final String REVERSE_IP_V6 = "ip6.arpa";

	private static final String ZONES_WILDCARD_VALUE = "*";

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
	public static void validateConfiguredZones() throws ObjectNotFoundException, InitializationException {
		List<String> configuredZones = new ArrayList<String>();
		String zonesValue = SQLProviderConfiguration.getZones();
		String zones[] = zonesValue.split(",");

		// The wilcard can't be used with other zones
		if (zonesValue.contains(ZONES_WILDCARD_VALUE) && zones.length > 1) {
			throw new InitializationException("Zones configuration: can't mix the wildcard '" + ZONES_WILDCARD_VALUE
					+ "' with other zones, " + "either the wildcard is used or the zones are specified");
		}
		for (String zone : zones) {
			zone = zone.trim().toLowerCase();
			if (zone.isEmpty()) {
				continue;
			}
			if (zone.equals(ZONES_WILDCARD_VALUE)) {
				// If set, overwrite all with the DB values
				configuredZones.clear();
				configuredZones.addAll(ZoneModel.getIdByZone().keySet());
				break;
			}
			if (zone.endsWith(".")) {
				zone = zone.substring(0, zone.length() - 1);
			}
			if (zone.startsWith(".")) {
				zone = zone.substring(1);
			}
			DomainLabel zoneLabel;
			try {
				zoneLabel = new DomainLabel(zone);
			} catch (DomainLabelException e) {
				throw new InitializationException(e.getMessage());
			}
			configuredZones.add(zoneLabel.getULabel());
		}

		Map<Integer, String> zoneByIdForServer = new HashMap<Integer, String>();
		Map<String, Integer> idByZoneForServer = new HashMap<String, Integer>();

		// Configure reverse zones
		if (SQLProviderConfiguration.isReverseIpv4Enabled()) {
			String zone = ZoneModel.REVERSE_IP_V4;
			if (ZoneModel.getIdByZone().get(zone) != null) {
				zoneByIdForServer.put(ZoneModel.getIdByZone().get(zone), zone);
				idByZoneForServer.put(zone, ZoneModel.getIdByZoneName(zone));
			} else {
				logger.log(Level.WARNING, "Configured zone not found in database : " + zone);
			}
		}
		configuredZones.remove(ZoneModel.REVERSE_IP_V4);

		if (SQLProviderConfiguration.isReverseIpv6Enabled()) {
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
		if (SQLProviderConfiguration.isUserSQL() && query == null) {
			return;
		}

		PreparedStatement statement = con.prepareStatement(query);
		ResultSet rs = statement.executeQuery();
		if (!rs.next()) {
			return;
		}

		do {
			Integer zoneId = rs.getInt("zone_id");
			DomainLabel zoneLabel;
			try {
				zoneLabel = new DomainLabel(rs.getString("zone_name").toLowerCase());
			} catch (DomainLabelException e) {
				throw new SQLException(e);
			}
			String zoneName = zoneLabel.getULabel();
			zoneById.put(zoneId, zoneName);
			idByZone.put(zoneName, zoneId);
		} while (rs.next());

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
		return idByZone.get(zoneName.toLowerCase());
	}

	/**
	 * @param zoneName
	 * @return Checks if the zoneName exists in the database.
	 */
	public static boolean existsZone(String zoneName) {
		return idByZone.containsKey(zoneName.toLowerCase());
	}

	/**
	 * validate if a address is in reverse lookup
	 * 
	 */
	public static boolean isReverseAddress(String address) {
		return address.trim().toLowerCase().endsWith(REVERSE_IP_V4) || address.trim().toLowerCase().endsWith(REVERSE_IP_V6);
	}

	public static String getAddressWithoutArpaZone(String address) {
		if (address.toLowerCase().endsWith(REVERSE_IP_V4)) {
			return address.substring(0, address.length() - (REVERSE_IP_V4.length() + 1));
		}

		if (address.toLowerCase().endsWith(REVERSE_IP_V6)) {
			return address.substring(0, address.length() - (REVERSE_IP_V6.length() + 1));
		}

		return address;
	}

	public static String getArpaZoneNameFromAddress(String address) {
		if (address.toLowerCase().endsWith(REVERSE_IP_V4)) {
			return REVERSE_IP_V4;
		}

		if (address.toLowerCase().endsWith(REVERSE_IP_V6)) {
			return REVERSE_IP_V6;
		}

		return null;
	}

	/**
	 * @param zoneById
	 *            the zoneById to set
	 */
	private static void setZoneById(Map<Integer, String> zoneById) {
		ZoneModel.zoneById = zoneById;
	}

	/**
	 * @return the idByZone
	 */
	private static Map<String, Integer> getIdByZone() {
		return idByZone;
	}

	/**
	 * @param idByZone
	 *            the idByZone to set
	 */
	private static void setIdByZone(Map<String, Integer> idByZone) {
		ZoneModel.idByZone = idByZone;
	}

	public static List<Integer> getValidZoneIds() {
		List<Integer> zoneIds = new ArrayList<Integer>(zoneById.keySet());
		return zoneIds;
	}

}
