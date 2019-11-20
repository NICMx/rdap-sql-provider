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

import mx.nic.rdap.core.catalog.Role;
import mx.nic.rdap.core.catalog.Status;
import mx.nic.rdap.core.db.Autnum;
import mx.nic.rdap.core.db.Entity;
import mx.nic.rdap.core.db.Event;
import mx.nic.rdap.core.db.IpNetwork;
import mx.nic.rdap.core.db.PublicId;
import mx.nic.rdap.core.db.UserConsent;
import mx.nic.rdap.core.db.UserConsentGlobal;
import mx.nic.rdap.core.db.UserConsentByAttribute;
import mx.nic.rdap.core.db.VCard;
import mx.nic.rdap.db.exception.http.NotImplementedException;
import mx.nic.rdap.db.struct.SearchResultStruct;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.SQLProviderConfiguration;
import mx.nic.rdap.sql.objects.EntityDbObj;

/**
 * Model for the {@link Entity} Object
 * 
 */
public class EntityModel {

	private final static Logger logger = Logger.getLogger(EntityModel.class.getName());

	private final static String QUERY_GROUP = "Entity";

	private static QueryGroup queryGroup = null;

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
	
	private final static String GET_USER_GLOBAL_CONSENT = "userGlobalConsent";
	private final static String GET_USER_CONSENT_BY_ATTR = "userConsentByAttribute";
	
	private static String GET_CONSENT;

	public static void loadQueryGroup(String schema) {
		try {
			QueryGroup qG = new QueryGroup(QUERY_GROUP, schema);
			setQueryGroup(qG);
		} catch (IOException e) {
			throw new RuntimeException("Error loading query group");
		}
		
		switch (SQLProviderConfiguration.getUserConsentType()) {
		case GLOBAL:
			GET_CONSENT = GET_USER_GLOBAL_CONSENT;
			break;
		case ATTRIBUTES:
			GET_CONSENT = GET_USER_CONSENT_BY_ATTR;
			break;
		case NONE:
		default:
			GET_CONSENT = null;
			// nothing to do.
			break;
		}
		
	}

	private static void setQueryGroup(QueryGroup qG) {
		queryGroup = qG;
	}

	private static QueryGroup getQueryGroup() {
		return queryGroup;
	}

	public static EntityDbObj getByHandle(String entityHandle, Connection connection)
			throws SQLException, NotImplementedException {
		EntityDbObj entResult = null;
		String query = getQueryGroup().getQuery(GET_BY_HANDLE_QUERY);
		QueryGroup.userImplemented(query);

		try (PreparedStatement statement = connection.prepareStatement(query);) {
			statement.setString(1, entityHandle);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			ResultSet resultSet = statement.executeQuery();
			entResult = processResultSet(resultSet, connection);
		}

		if (entResult == null) {
			return null;
		}

		loadNestedObjects(entResult, connection);
		return entResult;
	}

	private static void loadNestedObjects(Entity entity, Connection connection) throws SQLException {

		Long entityId = entity.getId();

		entity.getVCardList().addAll(VCardModel.getByEntityId(entityId, connection));

		// Retrieve the status
		entity.getStatus().addAll(StatusModel.getByEntityId(entityId, connection));

		// Retrieve the links
		entity.getLinks().addAll(LinkModel.getByEntityId(entityId, connection));

		// Retrive the remarks
		entity.getRemarks().addAll(RemarkModel.getByEntityId(entityId, connection));

		// Retrieve the events
		entity.getEvents().addAll(EventModel.getByEntityId(entityId, connection));

		// Retrieve the public ids
		entity.getPublicIds().addAll(PublicIdModel.getByEntity(entityId, connection));

		// Retrieve the entities
		List<Entity> entitiesByEntityId = getEntitiesByEntityId(entityId, connection);
		entity.getEntities().addAll(entitiesByEntityId);

		// retrieve the networks
		List<IpNetwork> networks = IpNetworkModel.getByEntityId(entityId, connection);
		entity.getIpNetworks().addAll(networks);

		// retrieve the autnums
		List<Autnum> autnums = AutnumModel.getByEntityId(entityId, connection);
		entity.getAutnums().addAll(autnums);

		// retrieve the entity roles
		List<Role> mainEntityRole = RoleModel.getMainEntityRole(entityId, connection);
		entity.getRoles().addAll(mainEntityRole);
		
		// retrieve the consent for this object;
		entity.setConsent(getConsentById(entityId, connection));
	}
	
	private static UserConsent getConsentById(long entityId, Connection connection) throws SQLException {
		if (GET_CONSENT == null) {
			return null;
		}
		
		String query = getQueryGroup().getQuery(GET_CONSENT);
		if (SQLProviderConfiguration.isUserSQL() && query == null) {
			return null;
		}

		UserConsent result = null;
		try (PreparedStatement statement = connection.prepareStatement(query);) {
			statement.setLong(1, entityId);
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			ResultSet rs = statement.executeQuery();
			if (!rs.next()) {
				return null;
			}
			
			// TODO handle result;
			//result
			switch (SQLProviderConfiguration.getUserConsentType()) {
			case GLOBAL:
				UserConsentGlobal userConsent = new UserConsentGlobal();
				userConsent.setGlobalConsent(rs.getBoolean("ugc_consent"));
				result = userConsent;
				break;
			case ATTRIBUTES:
				UserConsentByAttribute consentByAttr = new UserConsentByAttribute();
				
				consentByAttr.setHandle(rs.getBoolean("uca_handle"));
				consentByAttr.setName(rs.getBoolean("uca_name"));
				consentByAttr.setCompanyName(rs.getBoolean("uca_companyName"));
				consentByAttr.setCompanyURL(rs.getBoolean("uca_companyURL"));
				consentByAttr.setEmail(rs.getBoolean("uca_email"));
				consentByAttr.setVoice(rs.getBoolean("uca_voice"));
				consentByAttr.setCellphone(rs.getBoolean("uca_cellphone"));
				consentByAttr.setFax(rs.getBoolean("uca_fax"));
				consentByAttr.setJobTitle(rs.getBoolean("uca_jobTitle"));
				consentByAttr.setContactUri(rs.getBoolean("uca_contactUri"));
				consentByAttr.setType(rs.getBoolean("uca_type"));
				consentByAttr.setCountry(rs.getBoolean("uca_country"));
				consentByAttr.setCountryCode(rs.getBoolean("uca_countryCode"));
				consentByAttr.setCity(rs.getBoolean("uca_city"));
				consentByAttr.setState(rs.getBoolean("uca_state"));
				consentByAttr.setStreet1(rs.getBoolean("uca_street1"));
				consentByAttr.setStreet2(rs.getBoolean("uca_street2"));
				consentByAttr.setStreet3(rs.getBoolean("uca_street3"));
				consentByAttr.setPostalCode(rs.getBoolean("uca_postalCode"));
				
				result = consentByAttr;
				break;
			default:
				// Maybe Programming error?
				break;
			}
		}

		return result;
	}

	private static EntityDbObj processResultSet(ResultSet resultSet, Connection connection) throws SQLException {
		if (!resultSet.next()) {
			return null;
		}

		return new EntityDbObj(resultSet);
	}


	private static List<Entity> getEntitiesByEntityId(Long entityId, Connection connection) throws SQLException {
		return getEntitiesByEntityId(entityId, connection, true);
	}
	
	private static List<Entity> getEntitiesByEntityId(Long entityId, Connection connection, boolean isFirstNested) throws SQLException {
		List<Entity> entitiesById = getEntitiesById(entityId, connection, GET_ENTITY_ENTITY_QUERY, isFirstNested);
		for (Entity ent : entitiesById) {
			List<Role> entityEntityRole = RoleModel.getEntityEntityRole(entityId, ent.getId(), connection);
			ent.getRoles().addAll(entityEntityRole);
		}

		return entitiesById;
	}

	public static List<Entity> getEntitiesByDomainId(Long domainId, Connection connection) throws SQLException {
		List<Entity> entitiesById = getEntitiesById(domainId, connection, GET_DOMAIN_ENTITY_QUERY);
		for (Entity ent : entitiesById) {
			List<Role> entityEntityRole = RoleModel.getDomainEntityRole(domainId, ent.getId(), connection);
			ent.getRoles().addAll(entityEntityRole);
		}
		return entitiesById;
	}

	public static List<Entity> getEntitiesByNameserverId(Long nameserverId, Connection connection) throws SQLException {
		List<Entity> entitiesById = getEntitiesById(nameserverId, connection, GET_NS_ENTITY_QUERY);
		for (Entity ent : entitiesById) {
			List<Role> entityEntityRole = RoleModel.getNameserverEntityRole(nameserverId, ent.getId(), connection);
			ent.getRoles().addAll(entityEntityRole);
		}
		return entitiesById;
	}

	public static List<Entity> getEntitiesByAutnumId(Long autnumId, Connection connection) throws SQLException {
		List<Entity> entitiesById = getEntitiesById(autnumId, connection, GET_AUTNUM_ENTITY_QUERY);
		for (Entity ent : entitiesById) {
			List<Role> entityEntityRole = RoleModel.getAutnumEntityRole(autnumId, ent.getId(), connection);
			ent.getRoles().addAll(entityEntityRole);
		}
		return entitiesById;
	}

	public static List<Entity> getEntitiesByIpNetworkId(Long ipNetworkId, Connection connection) throws SQLException {
		List<Entity> entitiesById = getEntitiesById(ipNetworkId, connection, GET_IP_NETWORK_ENTITY_QUERY);
		for (Entity ent : entitiesById) {
			List<Role> entityEntityRole = RoleModel.getIpNetworkEntityRole(ipNetworkId, ent.getId(), connection);
			ent.getRoles().addAll(entityEntityRole);
		}
		return entitiesById;
	}

	private static List<Entity> getEntitiesById(Long id, Connection connection, String getQueryId) throws SQLException {
		return getEntitiesById(id, connection, getQueryId, true);
	}
	
	private static List<Entity> getEntitiesById(Long id, Connection connection, String getQueryId, boolean isFirstNested) throws SQLException {
		String query = getQueryGroup().getQuery(getQueryId);
		if (SQLProviderConfiguration.isUserSQL() && query == null) {
			return Collections.emptyList();
		}

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
				result.add(new EntityDbObj(rs));
			} while (rs.next());
		}

		setNestedSimpleObjects(result, connection, isFirstNested);

		return result;
	}

	private static void setNestedSimpleObjects(List<Entity> entities, Connection connection, boolean isFirstNested) throws SQLException {

		for (Entity entity : entities) {
			Long entityId = entity.getId();

			List<VCard> vCardList = VCardModel.getByEntityId(entityId, connection);
			entity.getVCardList().addAll(vCardList);

			List<Status> statusList = StatusModel.getByEntityId(entityId, connection);
			entity.getStatus().addAll(statusList);

			List<PublicId> pidList = PublicIdModel.getByEntity(entityId, connection);
			entity.getPublicIds().addAll(pidList);

			List<Event> eventList = EventModel.getByEntityId(entityId, connection);
			entity.getEvents().addAll(eventList);
			
			// retrieve the consent for this object;
			entity.setConsent(getConsentById(entityId, connection));
			
			if (isFirstNested) {
				List<Entity> nestedEntities = getEntitiesByEntityId(entityId, connection, false);
				entity.getEntities().addAll(nestedEntities);
			}
		}

		return;
	}

	public static SearchResultStruct<Entity> searchByHandle(String handle, int resultLimit, Connection connection)
			throws SQLException, NotImplementedException {
		String query = null;
		if (handle.contains("*")) {
			query = getQueryGroup().getQuery(SEARCH_BY_PARTIAL_HANDLE_QUERY);
			// Escape special chars for the "LIKE" sentence and consecutive wildcards are treated as one
			handle = handle.replaceAll("(\\%|\\_)", "\\\\$1").replaceAll("(\\*)+", "\\*").replace("*", "%");
		} else {
			query = getQueryGroup().getQuery(SEARCH_BY_HANDLE_QUERY);
		}

		return searchBy(handle, resultLimit, connection, query);
	}

	public static SearchResultStruct<Entity> searchByVCardName(String vcardName, int resultLimit, Connection connection)
			throws SQLException, NotImplementedException {
		String query = null;
		if (vcardName.contains("*")) {
			// Escape special chars for the "LIKE" sentence and consecutive wildcards are treated as one
			vcardName = vcardName.replaceAll("(\\%|\\_)", "\\\\$1").replaceAll("(\\*)+", "\\*").replace("*", "%");
			query = getQueryGroup().getQuery(SEARCH_BY_PARTIAL_NAME_QUERY);
		} else {
			query = getQueryGroup().getQuery(SEARCH_BY_NAME_QUERY);
		}

		return searchBy(vcardName, resultLimit, connection, query);
	}

	public static SearchResultStruct<Entity> searchByRegexHandle(String regexHandle, int resultLimit,
			Connection connection) throws SQLException, NotImplementedException {
		return searchBy(regexHandle, resultLimit, connection, getQueryGroup().getQuery(SEARCH_BY_HANDLE_REGEX_QUERY));
	}

	public static SearchResultStruct<Entity> searchByRegexName(String regexName, int resultLimit, Connection connection)
			throws SQLException, NotImplementedException {
		return searchBy(regexName, resultLimit, connection, getQueryGroup().getQuery(SEARCH_BY_NAME_REGEX_QUERY));
	}

	private static SearchResultStruct<Entity> searchBy(String criteria, int resultLimit, Connection connection,
			String query) throws SQLException, NotImplementedException {
		QueryGroup.userImplemented(query);

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
				return null;
			}

			do {
				entities.add(new EntityDbObj(rs));
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

}
