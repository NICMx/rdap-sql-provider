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

import mx.nic.rdap.core.catalog.VariantRelation;
import mx.nic.rdap.core.db.Variant;
import mx.nic.rdap.core.db.VariantName;
import mx.nic.rdap.sql.QueryGroup;

/**
 * Model for the {@link Variant} Object
 * 
 */
public class VariantStoreModel {

	private final static Logger logger = Logger.getLogger(VariantStoreModel.class.getName());

	private final static String QUERY_GROUP = "VariantStore";
	private static final String STORE_QUERY = "storeToDatabase";
	private static final String STORE_RELATION_QUERY = "storeVariantRelation";
	private static final String STORE_NAMES_QUERY = "storeVariantNames";

	private static QueryGroup queryGroup = null;

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

	public static void storeAllToDatabase(List<Variant> variants, Long domainId, Connection connection)
			throws SQLException {
		for (Variant variant : variants) {
			variant.setDomainId(domainId);
			VariantStoreModel.storeToDatabase(variant, connection);
		}
	}

	private static Long storeToDatabase(Variant variant, Connection connection) throws SQLException {
		Long variantInsertedId = null;
		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery(STORE_QUERY),
				Statement.RETURN_GENERATED_KEYS)) {
			fillPreparedStatement(statement, variant);
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			statement.executeUpdate();
			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.next();
			variantInsertedId = resultSet.getLong(1);
			variant.setId(variantInsertedId);
		}

		storeVariantNames(variant, connection);
		storeVariantRelations(variant, connection);

		return variantInsertedId;
	}

	private static void storeVariantRelations(Variant variant, Connection connection) throws SQLException {
		if (variant.getRelations().isEmpty())
			return;

		Long variantId = variant.getId();
		try (PreparedStatement statement = connection
				.prepareStatement(getQueryGroup().getQuery(STORE_RELATION_QUERY))) {
			for (VariantRelation relation : variant.getRelations()) {
				statement.setInt(1, relation.getId());
				statement.setLong(2, variantId);
				logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
				statement.executeUpdate();
			}
		}
	}

	private static void storeVariantNames(Variant variant, Connection connection) throws SQLException {
		if (variant.getVariantNames().isEmpty())
			return;

		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery(STORE_NAMES_QUERY))) {
			Long variantId = variant.getId();
			for (VariantName variantName : variant.getVariantNames()) {
				statement.setString(1, variantName.getLdhName());
				statement.setLong(2, variantId);
				logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
				statement.executeUpdate();
			}
		}
	}

	private static void fillPreparedStatement(PreparedStatement preparedStatement, Variant variant)
			throws SQLException {
		preparedStatement.setString(1, variant.getIdnTable());
		preparedStatement.setLong(2, variant.getDomainId());
	}

}
