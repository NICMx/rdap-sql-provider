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

import mx.nic.rdap.core.db.Entity;
import mx.nic.rdap.core.db.VCard;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.exception.IncompleteObjectException;

/**
 * Model for the {@link Entity} Object
 * 
 */
public class EntityStoreModel {

	private final static Logger logger = Logger.getLogger(EntityStoreModel.class.getName());

	private final static String QUERY_GROUP = "EntityStore";

	private static QueryGroup queryGroup = null;

	private final static String STORE_QUERY = "storeToDatabase";
	private final static String GET_ID_BY_HANDLE_QUERY = "getIdByHandle";

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

	public static Long getIdByHandle(String entityHandle, Connection connection) throws SQLException {
		String query = getQueryGroup().getQuery(GET_ID_BY_HANDLE_QUERY);
		Long entId = null;
		try (PreparedStatement statement = connection.prepareStatement(query);) {
			statement.setString(1, entityHandle);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			ResultSet rs = statement.executeQuery();
			if (!rs.next()) {
				return null;
			}
			long long1 = rs.getLong("ent_id");
			if (!rs.wasNull()) {
				entId = long1;
			}
		}

		return entId;
	}

	public static void validateParentEntities(List<Entity> entities, Connection connection) throws SQLException {
		for (Entity ent : entities) {
			Long entId = EntityStoreModel.getIdByHandle(ent.getHandle(), connection);
			if (entId == null) {
				throw new NullPointerException(
						"Entity: " + ent.getHandle() + " was not inserted previously to the database");
			}
			ent.setId(entId);
		}
	}

	public static long storeToDatabase(Entity entity, Connection connection) throws SQLException {
		return storeToDatabase(entity, connection, true);
	}

	private static long storeToDatabase(Entity entity, Connection connection, boolean isParentEntity)
			throws SQLException {
		Long entityId = EntityStoreModel.getIdByHandle(entity.getHandle(), connection);
		if (entityId != null) {
			entity.setId(entityId);
			return entityId;
		}

		String query = getQueryGroup().getQuery(STORE_QUERY);

		try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
			fillPreparedStatement(statement, entity);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();

			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.next();
			entityId = resultSet.getLong(1);
			entity.setId(entityId);
		}

		if (isParentEntity && !entity.getRoles().isEmpty()) {
			RoleStoreModel.storeMainEntityRol(entity, connection);
		}
		storeNestedObjects(entity, connection);

		return entityId;
	}

	private static void isValidForStore(Entity entity) throws IncompleteObjectException {
		if (entity.getHandle() == null || entity.getHandle().isEmpty())
			throw new IncompleteObjectException("handle", "Entity");
	}

	private static void storeNestedObjects(Entity entity, Connection connection) throws SQLException {
		isValidForStore(entity);
		storeVcardList(entity, connection);

		PublicIdStoreModel.storePublicIdByEntity(entity.getPublicIds(), entity.getId(), connection);
		StatusStoreModel.storeEntityStatusToDatabase(entity.getStatus(), entity.getId(), connection);
		RemarkStoreModel.storeEntityRemarksToDatabase(entity.getRemarks(), entity.getId(), connection);
		LinkStoreModel.storeEntityLinksToDatabase(entity.getLinks(), entity.getId(), connection);
		EventStoreModel.storeEntityEventsToDatabase(entity.getEvents(), entity.getId(), connection);
		for (Entity ent : entity.getEntities()) {
			storeToDatabase(ent, connection, false);
		}
		RoleStoreModel.storeEntityEntityRoles(entity.getEntities(), entity.getId(), connection);

	}

	private static void storeVcardList(Entity entity, Connection connection) throws SQLException {
		List<VCard> vCardList = entity.getVCardList();
		if (!vCardList.isEmpty()) {
			for (VCard vCard : vCardList) {
				VCardStoreModel.storeToDatabase(vCard, connection);
			}
			VCardStoreModel.storeRegistrarContactToDatabase(vCardList, entity.getId(), connection);
		}
	}

	private static void fillPreparedStatement(PreparedStatement preparedStatement, Entity entity) throws SQLException {
		preparedStatement.setString(1, entity.getHandle());
		preparedStatement.setString(2, entity.getPort43());
	}

}
