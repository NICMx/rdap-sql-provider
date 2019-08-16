package mx.nic.rdap.sql.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.db.VCard;
import mx.nic.rdap.core.db.VCardPostalInfo;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.SQLProviderConfiguration;
import mx.nic.rdap.sql.exception.ObjectNotFoundException;
import mx.nic.rdap.sql.objects.VCardDbObj;

/**
 * Model for the {@link VCard} object
 * 
 */
public class VCardModel {

	private static final Logger logger = Logger.getLogger(VCardModel.class.getName());

	private static final String QUERY_GROUP = "VCard";

	private static QueryGroup queryGroup = null;

	private final static String GET_BY_ENTITY_QUERY = "getByEntityId";
	private final static String GET_CONTACT_URIS_BY_VCARD_ID = "getContactUrisByVcardId";

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
	 * Get a {@link List} of {@link VCard} belonging to an Entity by
	 * the entity Id.
	 */
	public static List<VCard> getByEntityId(Long entityId, Connection connection) throws SQLException {
		List<VCard> vCardResults = null;
		String query = getQueryGroup().getQuery(GET_BY_ENTITY_QUERY);
		if (SQLProviderConfiguration.isUserSQL() && query == null) {
			return Collections.emptyList();
		}

		try (PreparedStatement statement = connection.prepareStatement(query);) {
			statement.setLong(1, entityId);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			ResultSet resultSet = statement.executeQuery();

			vCardResults = processListResultSet(resultSet, connection);
		}

		for (VCard vCard : vCardResults) {
			setSonObjects(vCard, connection);
		}
		return vCardResults;
	}

	/**
	 * Get and Set the nested objects of the {@link VCard}.
	 * 
	 */
	private static void setSonObjects(VCard vCard, Connection connection) throws SQLException {
		String query = getQueryGroup().getQuery(GET_CONTACT_URIS_BY_VCARD_ID);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, vCard.getId());
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				vCard.getContactUri().add(rs.getString("vcu_uri"));
			}
		}

		try {
			List<VCardPostalInfo> postalInfoList = VCardPostalInfoModel.getByVCardId(vCard.getId(), connection);
			vCard.setPostalInfo(postalInfoList);
		} catch (ObjectNotFoundException e) {
			// TODO: a VCard couldn't have postal info ?
		}
	}

	/**
	 * Process a {@link ResultSet} and return a {@link List} of {@link VCard}s.
	 */
	private static List<VCard> processListResultSet(ResultSet resultSet, Connection connection) throws SQLException {
		List<VCard> result = new ArrayList<>();
		if (!resultSet.next()) {
			return Collections.emptyList();
		}
		do {
			VCardDbObj vCard = new VCardDbObj();
			vCard.loadFromDatabase(resultSet);
			result.add(vCard);
		} while (resultSet.next());

		return result;
	}

}
