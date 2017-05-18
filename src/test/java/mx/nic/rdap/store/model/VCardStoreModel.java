package mx.nic.rdap.store.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.db.VCard;
import mx.nic.rdap.core.db.VCardPostalInfo;
import mx.nic.rdap.sql.QueryGroup;

/**
 * Model for the {@link VCard} object
 * 
 */
public class VCardStoreModel {

	private static final Logger logger = Logger.getLogger(VCardStoreModel.class.getName());

	private static final String QUERY_GROUP = "VCardStore";

	private static QueryGroup queryGroup = null;

	private final static String STORE_QUERY = "storeToDatabase";
	private final static String STORE_ENTITY_CONTACT_QUERY = "storeEntityContact";

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

	public static long storeToDatabase(VCard vCard, Connection connection) throws SQLException {
		long vCardId;

		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery(STORE_QUERY),
				Statement.RETURN_GENERATED_KEYS);) {
			fillPreparedStatement(statement, vCard);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();

			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.next();
			vCardId = resultSet.getLong(1);
			vCard.setId(vCardId);
		}

		for (VCardPostalInfo postalInfo : vCard.getPostalInfo()) {
			postalInfo.setVCardId(vCardId);
			VCardPostalInfoStoreModel.storeToDatabase(postalInfo, connection);
		}

		return vCardId;
	}

	public static void storeRegistrarContactToDatabase(List<VCard> vCardList, Long registrarId, Connection connection)
			throws SQLException {
		if (vCardList.isEmpty())
			return;

		try (PreparedStatement statement = connection.prepareStatement(
				getQueryGroup().getQuery(STORE_ENTITY_CONTACT_QUERY), Statement.RETURN_GENERATED_KEYS);) {
			for (VCard vCard : vCardList) {
				statement.setLong(1, registrarId);
				statement.setLong(2, vCard.getId());
				logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
				statement.executeUpdate();
			}
		}
	}

	private static void fillPreparedStatement(PreparedStatement preparedStatement, VCard vCard) throws SQLException {
		preparedStatement.setString(1, vCard.getName());
		preparedStatement.setString(2, vCard.getCompanyName());
		preparedStatement.setString(3, vCard.getCompanyURL());
		preparedStatement.setString(4, vCard.getEmail());
		preparedStatement.setString(5, vCard.getVoice());
		preparedStatement.setString(6, vCard.getCellphone());
		preparedStatement.setString(7, vCard.getFax());
		preparedStatement.setString(8, vCard.getJobTitle());
	}

}
