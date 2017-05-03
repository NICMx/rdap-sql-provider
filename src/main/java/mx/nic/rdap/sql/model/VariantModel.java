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

import mx.nic.rdap.core.catalog.VariantRelation;
import mx.nic.rdap.core.db.Variant;
import mx.nic.rdap.core.db.VariantName;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.exception.ObjectNotFoundException;
import mx.nic.rdap.sql.objects.VariantDbObj;

/**
 * Model for the {@link Variant} Object
 * 
 */
public class VariantModel {

	private final static Logger logger = Logger.getLogger(VariantModel.class.getName());

	private final static String QUERY_GROUP = "Variant";
	private static final String GET_BY_DOMAIN_QUERY = "getByDomainId";
	private static final String GET_RELATION_BY_VARIANT_QUERY = "getVariantRelationsByVariantId";
	private static final String GET_NAMES_BY_VARIANT_QUERY = "getVariantNamesByVariantId";

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

	public static List<Variant> getByDomainId(Long domainId, Connection connection) throws SQLException {
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

				try {
					setVariantNames(variant, connection);
				} catch (ObjectNotFoundException e) {
					logger.log(Level.WARNING, "Variant " + variant.getId() + " has no names.", e);
					continue;
				}

				try {
					setVariantRelations(variant, connection);
				} catch (ObjectNotFoundException e) {
					logger.log(Level.WARNING, "Variant " + variant.getId() + " has no relations.", e);
					continue;
				}

				variants.add(variant);
			} while (resultSet.next());
		}

		return variants;
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
