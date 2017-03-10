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

import mx.nic.rdap.core.catalog.Rol;
import mx.nic.rdap.core.catalog.Status;
import mx.nic.rdap.core.db.Autnum;
import mx.nic.rdap.core.db.Entity;
import mx.nic.rdap.core.db.Event;
import mx.nic.rdap.core.db.IpNetwork;
import mx.nic.rdap.core.db.PublicId;
import mx.nic.rdap.core.db.VCard;
import mx.nic.rdap.db.QueryGroup;
import mx.nic.rdap.db.exception.ObjectNotFoundException;
import mx.nic.rdap.db.exception.RequiredValueNotFoundException;
import mx.nic.rdap.db.objects.EntityDbObj;
import mx.nic.rdap.db.struct.SearchResultStruct;

/**
 * Model for the {@link Entity} Object
 * 
 */
public class EntityModel {

	private final static Logger logger = Logger.getLogger(EntityModel.class.getName());

	private final static String QUERY_GROUP = "Entity";

	protected static QueryGroup queryGroup = null;

	private final static String STORE_QUERY = "storeToDatabase";
	private final static String GET_ID_BY_HANDLE_QUERY = "getIdByHandle";
	private final static String GET_BY_ID_QUERY = "getById";
	private final static String GET_BY_HANDLE_QUERY = "getByHandle";

	private final static String SEARCH_BY_PARTIAL_HANDLE_QUERY = "searchByPartialHandle";
	private final static String SEARCH_BY_HANDLE_QUERY = "searchByHandle";
	private final static String SEARCH_BY_PARTIAL_NAME_QUERY = "searchByPartialName";
	private final static String SEARCH_BY_NAME_QUERY = "searchByName";

	private final static String GET_ENTITY_ENTITY_QUERY = "getEntitysEntitiesQuery";
	private final static String GET_DOMAIN_ENTITY_QUERY = "getDomainsEntitiesQuery";
	private final static String GET_NS_ENTITY_QUERY = "getNameserversEntitiesQuery";
	private final static String GET_AUTNUM_ENTITY_QUERY = "getAutnumEntitiesQuery";
	private final static String GET_IP_NETWORK_ENTITY_QUERY = "getIpNetworkEntitiesQuery";

	private final static String SEARCH_BY_HANDLE_REGEX_QUERY = "searchByRegexHandle";
	private final static String SEARCH_BY_NAME_REGEX_QUERY = "searchByRegexName";

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

	public static long storeToDatabase(Entity entity, Connection connection)
			throws SQLException, RequiredValueNotFoundException {
		Long entityId = getIdByHandle(entity.getHandle(), connection);
		if (entityId != null) {
			entity.setId(entityId);
			return entityId;
		}

		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery(STORE_QUERY),
				Statement.RETURN_GENERATED_KEYS);) {
			((EntityDbObj) entity).storeToDatabase(statement);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();

			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.next();
			entityId = resultSet.getLong(1);
			entity.setId(entityId);
		}

		storeNestedObjects(entity, connection);

		return entityId;
	}

	private static void isValidForStore(Entity entity) throws RequiredValueNotFoundException {
		if (entity.getHandle() == null || entity.getHandle().isEmpty())
			throw new RequiredValueNotFoundException("handle", "Entity");
	}

	private static void storeNestedObjects(Entity entity, Connection connection)
			throws SQLException, RequiredValueNotFoundException {
		isValidForStore(entity);
		storeVcardList(entity, connection);

		PublicIdModel.storePublicIdByEntity(entity.getPublicIds(), entity.getId(), connection);
		StatusModel.storeEntityStatusToDatabase(entity.getStatus(), entity.getId(), connection);
		RemarkModel.storeEntityRemarksToDatabase(entity.getRemarks(), entity.getId(), connection);
		LinkModel.storeEntityLinksToDatabase(entity.getLinks(), entity.getId(), connection);
		EventModel.storeEntityEventsToDatabase(entity.getEvents(), entity.getId(), connection);
		for (Entity ent : entity.getEntities()) {
			storeToDatabase(ent, connection);
		}
		RolModel.storeEntityEntityRoles(entity.getEntities(), entity.getId(), connection);

		if (!entity.getRoles().isEmpty() && !entity.getEntities().isEmpty())
			RolModel.storeMainEntityRol(entity.getEntities(), entity, connection);
	}

	private static void storeVcardList(Entity entity, Connection connection) throws SQLException {
		List<VCard> vCardList = entity.getVCardList();
		if (!vCardList.isEmpty()) {
			for (VCard vCard : vCardList) {
				VCardModel.storeToDatabase(vCard, connection);
			}
			VCardModel.storeRegistrarContactToDatabase(vCardList, entity.getId(), connection);
		}
	}

	public static Entity getById(Long entityId, Connection connection) throws SQLException, ObjectNotFoundException {
		Entity entResult = null;
		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery(GET_BY_ID_QUERY));) {
			statement.setLong(1, entityId);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			ResultSet resultSet = statement.executeQuery();
			entResult = processResultSet(resultSet, connection);
		}

		loadNestedObjects(entResult, connection);
		return entResult;
	}

	public static EntityDbObj getByHandle(String entityHandle, Connection connection)
			throws SQLException, ObjectNotFoundException {
		EntityDbObj entResult = null;
		try (PreparedStatement statement = connection
				.prepareStatement(getQueryGroup().getQuery(GET_BY_HANDLE_QUERY));) {
			statement.setString(1, entityHandle);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			ResultSet resultSet = statement.executeQuery();
			entResult = processResultSet(resultSet, connection);
		}

		loadNestedObjects(entResult, connection);
		return entResult;
	}

	private static void loadNestedObjects(Entity entity, Connection connection) throws SQLException {

		Long entityId = entity.getId();
		try {
			List<VCard> vCardList = VCardModel.getByEntityId(entityId, connection);
			entity.getVCardList().addAll(vCardList);
		} catch (ObjectNotFoundException e) {
			// Do nothing, vcard is not required
		}

		// Retrieve the status
		entity.getStatus().addAll(StatusModel.getByEntityId(entityId, connection));

		// Retrieve the links
		entity.getLinks().addAll(LinkModel.getByEntityId(entityId, connection));

		// Retrive the remarks
		try {
			entity.getRemarks().addAll(RemarkModel.getByEntityId(entityId, connection));
		} catch (ObjectNotFoundException onfe) {
			// Do nothing, remarks is not required
		}

		// Retrieve the events
		entity.getEvents().addAll(EventModel.getByEntityId(entityId, connection));

		// Retrieve the public ids
		entity.getPublicIds().addAll(PublicIdModel.getByEntity(entityId, connection));

		// Retrieve the entities
		try {
			List<Entity> entitiesByEntityId = getEntitiesByEntityId(entityId, connection);
			entity.getEntities().addAll(entitiesByEntityId);
			entity.getRoles().addAll(RolModel.getMainEntityRol(entitiesByEntityId, entity, connection));
		} catch (ObjectNotFoundException onfe) {
			// Do nothing, entities is not required
		}

		// retrieve the networks
		List<IpNetwork> networks = IpNetworkModel.getByEntityId(entityId, connection);
		entity.getIpNetworks().addAll(networks);

		// retrieve the autnums
		List<Autnum> autnums = AutnumModel.getByEntityId(entityId, connection);
		entity.getAutnums().addAll(autnums);
	}

	private static EntityDbObj processResultSet(ResultSet resultSet, Connection connection)
			throws SQLException, ObjectNotFoundException {
		if (!resultSet.next()) {
			throw new ObjectNotFoundException("Object not found");
		}

		EntityDbObj entity = new EntityDbObj();
		entity.loadFromDatabase(resultSet);

		return entity;
	}

	public static List<Entity> getEntitiesByEntityId(Long entityId, Connection connection) throws SQLException {
		List<Entity> entitiesById = getEntitiesById(entityId, connection, GET_ENTITY_ENTITY_QUERY);
		for (Entity ent : entitiesById) {
			List<Rol> entityEntityRol = RolModel.getEntityEntityRol(entityId, ent.getId(), connection);
			ent.getRoles().addAll(entityEntityRol);
		}

		return entitiesById;
	}

	public static List<Entity> getEntitiesByDomainId(Long domainId, Connection connection) throws SQLException {
		List<Entity> entitiesById = getEntitiesById(domainId, connection, GET_DOMAIN_ENTITY_QUERY);
		for (Entity ent : entitiesById) {
			List<Rol> entityEntityRol = RolModel.getDomainEntityRol(domainId, ent.getId(), connection);
			ent.getRoles().addAll(entityEntityRol);
		}
		return entitiesById;
	}

	public static List<Entity> getEntitiesByNameserverId(Long nameserverId, Connection connection) throws SQLException {
		List<Entity> entitiesById = getEntitiesById(nameserverId, connection, GET_NS_ENTITY_QUERY);
		for (Entity ent : entitiesById) {
			List<Rol> entityEntityRol = RolModel.getNameserverEntityRol(nameserverId, ent.getId(), connection);
			ent.getRoles().addAll(entityEntityRol);
		}
		return entitiesById;
	}

	public static List<Entity> getEntitiesByAutnumId(Long autnumId, Connection connection) throws SQLException {
		List<Entity> entitiesById = getEntitiesById(autnumId, connection, GET_AUTNUM_ENTITY_QUERY);
		for (Entity ent : entitiesById) {
			List<Rol> entityEntityRol = RolModel.getAutnumEntityRol(autnumId, ent.getId(), connection);
			ent.getRoles().addAll(entityEntityRol);
		}
		return entitiesById;
	}

	public static List<Entity> getEntitiesByIpNetworkId(Long ipNetworkId, Connection connection) throws SQLException {
		List<Entity> entitiesById = getEntitiesById(ipNetworkId, connection, GET_IP_NETWORK_ENTITY_QUERY);
		for (Entity ent : entitiesById) {
			List<Rol> entityEntityRol = RolModel.getIpNetworkEntityRol(ipNetworkId, ent.getId(), connection);
			ent.getRoles().addAll(entityEntityRol);
		}
		return entitiesById;
	}

	private static List<Entity> getEntitiesById(Long id, Connection connection, String getQueryId) throws SQLException {
		String query = getQueryGroup().getQuery(getQueryId);
		List<Entity> result = null;
		try (PreparedStatement statement = connection.prepareStatement(query);) {
			statement.setLong(1, id);
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			ResultSet rs = statement.executeQuery();
			if (!rs.next()) {
				return Collections.emptyList();
			}
			result = new ArrayList<>();

			do {
				EntityDbObj dao = new EntityDbObj();
				dao.loadFromDatabase(rs);
				result.add(dao);
			} while (rs.next());
		}

		setNestedSimpleObjects(result, connection);

		return result;
	}

	private static void setNestedSimpleObjects(List<Entity> entities, Connection connection) throws SQLException {

		for (Entity entity : entities) {
			Long entityId = entity.getId();
			try {
				List<VCard> vCardList = VCardModel.getByEntityId(entityId, connection);
				entity.getVCardList().addAll(vCardList);
			} catch (ObjectNotFoundException e) {
				// Could not have a VCard.
			}

			List<Status> statusList = StatusModel.getByEntityId(entityId, connection);
			entity.getStatus().addAll(statusList);

			List<PublicId> pidList = PublicIdModel.getByEntity(entityId, connection);
			entity.getPublicIds().addAll(pidList);

			List<Event> eventList = EventModel.getByEntityId(entityId, connection);
			entity.getEvents().addAll(eventList);
		}

		return;
	}

	public static SearchResultStruct<Entity> searchByHandle(String handle, Integer resultLimit, Connection connection)
			throws SQLException, ObjectNotFoundException {
		String query = null;
		if (handle.contains("*")) {
			query = getQueryGroup().getQuery(SEARCH_BY_PARTIAL_HANDLE_QUERY);
			handle = handle.replace("*", "%");
		} else {
			query = getQueryGroup().getQuery(SEARCH_BY_HANDLE_QUERY);
		}

		return searchBy(handle, resultLimit, connection, query);
	}

	public static SearchResultStruct<Entity> searchByVCardName(String vcardName, Integer resultLimit,
			Connection connection) throws SQLException, ObjectNotFoundException {
		String query = null;
		if (vcardName.contains("*")) {
			vcardName = vcardName.replace("*", "%");
			query = getQueryGroup().getQuery(SEARCH_BY_PARTIAL_NAME_QUERY);
		} else {
			query = getQueryGroup().getQuery(SEARCH_BY_NAME_QUERY);
		}

		return searchBy(vcardName, resultLimit, connection, query);
	}

	public static SearchResultStruct<Entity> searchByRegexHandle(String regexHandle, Integer resultLimit,
			Connection connection) throws SQLException, ObjectNotFoundException {
		return searchBy(regexHandle, resultLimit, connection, getQueryGroup().getQuery(SEARCH_BY_HANDLE_REGEX_QUERY));
	}

	public static SearchResultStruct<Entity> searchByRegexName(String regexName, Integer resultLimit,
			Connection connection) throws SQLException, ObjectNotFoundException {
		return searchBy(regexName, resultLimit, connection, getQueryGroup().getQuery(SEARCH_BY_NAME_REGEX_QUERY));
	}

	private static SearchResultStruct<Entity> searchBy(String criteria, Integer resultLimit, Connection connection,
			String query) throws SQLException, ObjectNotFoundException {
		SearchResultStruct<Entity> result = new SearchResultStruct<Entity>();
		// Hack to know is there is more domains that the limit, used for
		// notices
		resultLimit = resultLimit + 1;
		List<EntityDbObj> entities = new ArrayList<EntityDbObj>();

		try (PreparedStatement statement = connection.prepareStatement(query);) {
			statement.setString(1, criteria);
			statement.setInt(2, resultLimit);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			ResultSet rs = statement.executeQuery();

			if (!rs.next()) {
				throw new ObjectNotFoundException("Object not found.");
			}

			do {
				EntityDbObj ent = new EntityDbObj();
				ent.loadFromDatabase(rs);
				entities.add(ent);
			} while (rs.next());
			resultLimit = resultLimit - 1;// Back to the original limit
			if (entities.size() > resultLimit) {
				result.setResultSetWasLimitedByUserConfiguration(true);
				entities.remove(entities.size() - 1);
			}
			for (EntityDbObj ent : entities) {
				loadNestedObjects(ent, connection);
			}
			result.setSearchResultsLimitForUser(resultLimit);
			result.getResults().addAll(entities);
			return result;
		}

	}

	public static void validateParentEntities(List<Entity> entities, Connection connection) throws SQLException {
		for (Entity ent : entities) {
			Long entId = EntityModel.getIdByHandle(ent.getHandle(), connection);
			if (entId == null) {
				throw new NullPointerException(
						"Entity: " + ent.getHandle() + " was not inserted previously to the database");
			}
			ent.setId(entId);
		}
	}

}
