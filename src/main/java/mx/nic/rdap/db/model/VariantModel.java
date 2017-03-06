package mx.nic.rdap.db.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.catalog.VariantRelation;
import mx.nic.rdap.core.db.Variant;
import mx.nic.rdap.core.db.VariantName;
import mx.nic.rdap.db.QueryGroup;
import mx.nic.rdap.db.exception.ObjectNotFoundException;
import mx.nic.rdap.db.objects.VariantDbObj;

/**
 * Model for the {@link Variant} Object
 * 
 */
public class VariantModel {

	private final static Logger logger = Logger.getLogger(VariantModel.class.getName());

	private final static String QUERY_GROUP = "Variant";
	private static final String STORE_QUERY = "storeToDatabase";
	private static final String STORE_RELATION_QUERY = "storeVariantRelation";
	private static final String STORE_NAMES_QUERY = "storeVariantNames";
	private static final String GET_BY_DOMAIN_QUERY = "getByDomainId";
	private static final String GET_RELATION_BY_VARIANT_QUERY = "getVariantRelationsByVariantId";
	private static final String GET_BY_ID_QUERY = "getById";
	private static final String GET_NAMES_BY_VARIANT_QUERY = "getVariantNamesByVariantId";

	protected static QueryGroup queryGroup = null;

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
			VariantModel.storeToDatabase(variant, connection);
		}
	}

	public static Long storeToDatabase(Variant variant, Connection connection) throws SQLException {
		Long variantInsertedId = null;
		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery(STORE_QUERY),
				Statement.RETURN_GENERATED_KEYS)) {
			((VariantDbObj) variant).storeToDatabase(statement);
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

	public static List<Variant> getByDomainId(Long domainId, Connection connection)
			throws SQLException, ObjectNotFoundException {
		List<Variant> variants = null;
		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery(GET_BY_DOMAIN_QUERY))) {
			statement.setLong(1, domainId);
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next()) {
				// A domain can have no variants.
				return Collections.emptyList();
			}

			variants = new ArrayList<>();
			do {
				VariantDbObj variant = new VariantDbObj(resultSet);
				variants.add(variant);
			} while (resultSet.next());
		}

		for (Variant variant : variants) {
			setVariantNames(variant, connection);
			setVariantRelations(variant, connection);
		}

		return variants;
	}

	public static Variant getById(Long variantId, Connection connection) throws SQLException, ObjectNotFoundException {
		Variant result = null;
		String query = getQueryGroup().getQuery(GET_BY_ID_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query);) {
			statement.setLong(1, variantId);
			logger.log(Level.INFO, "Executing QUERY" + statement.toString());
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next()) {
				throw new ObjectNotFoundException("Object Not found");
			}

			result = new VariantDbObj(resultSet);
		}

		setVariantNames(result, connection);
		setVariantRelations(result, connection);

		return result;
	}

	private static void setVariantRelations(Variant variant, Connection connection)
			throws SQLException, ObjectNotFoundException {
		Long variantId = variant.getId();
		try (PreparedStatement statement = connection
				.prepareStatement(getQueryGroup().getQuery(GET_RELATION_BY_VARIANT_QUERY))) {
			statement.setLong(1, variantId);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			try (ResultSet resultSet = statement.executeQuery()) {
				// Validate results
				if (!resultSet.next()) {
					throw new ObjectNotFoundException("Object not found.");
				}
				List<VariantRelation> relations = variant.getRelations();
				do {
					relations.add(VariantRelation.getById(resultSet.getInt("rel_id")));
				} while (resultSet.next());
				return;
			}
		}
	}

	private static void storeVariantRelations(Variant variant, Connection connection) throws SQLException {
		if (variant.getRelations().isEmpty())
			return;

		Long variantId = variant.getId();
		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery(STORE_RELATION_QUERY))) {
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
				statement.setString(1, variantName.getPunycode());
				statement.setLong(2, variantId);
				logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
				statement.executeUpdate();
			}
		}
	}

	private static void setVariantNames(Variant variant, Connection connection)
			throws SQLException, ObjectNotFoundException {
		try (PreparedStatement statement = connection
				.prepareStatement(getQueryGroup().getQuery(GET_NAMES_BY_VARIANT_QUERY))) {
			statement.setLong(1, variant.getId());
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next()) {
				throw new ObjectNotFoundException("Object not found.");
			}
			List<VariantName> variantNames = variant.getVariantNames();
			do {
				VariantName variantName = new VariantName();
				variantName.setLdhName(resultSet.getString("vna_ldh_name"));
				variantNames.add(variantName);
			} while (resultSet.next());
		}
	}

}
