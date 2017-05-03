package mx.nic.rdap.store.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.db.Domain;
import mx.nic.rdap.core.db.DomainLabel;
import mx.nic.rdap.core.db.Entity;
import mx.nic.rdap.core.db.IpNetwork;
import mx.nic.rdap.core.db.Nameserver;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.exception.IncompleteObjectException;
import mx.nic.rdap.sql.model.EntityModel;
import mx.nic.rdap.sql.model.IpNetworkModel;
import mx.nic.rdap.sql.model.NameserverModel;
import mx.nic.rdap.sql.model.ZoneModel;
import mx.nic.rdap.sql.objects.DomainDbObj;
import mx.nic.rdap.sql.objects.IpNetworkDbObj;

/**
 * Model for the {@link Domain} Object
 * 
 */
public class DomainStoreModel {

	private final static Logger logger = Logger.getLogger(DomainStoreModel.class.getName());

	private final static String QUERY_GROUP = "DomainStore";

	private static QueryGroup queryGroup = null;
	private static final String STORE_QUERY = "storeToDatabase";

	private static final String STORE_IP_NETWORK_RELATION_QUERY = "storeDomainIpNetworkRelation";

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

	public static Long storeToDatabase(Domain domain, boolean useNameserverAsAttribute, Connection connection)
			throws SQLException {
		String query = getQueryGroup().getQuery(STORE_QUERY);
		Long domainId;
		isValidForStore((DomainDbObj) domain);
		try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			fillPreparedStatement(statement, domain);
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			statement.executeUpdate();
			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.next();
			domainId = resultSet.getLong(1);
			domain.setId(domainId);
		}

		storeNestedObjects(domain, useNameserverAsAttribute, connection);
		return domainId;
	}

	private static void storeNestedObjects(Domain domain, boolean useNameserverAsAttribute, Connection connection)
			throws SQLException {
		Long domainId = domain.getId();
		RemarkStoreModel.storeDomainRemarksToDatabase(domain.getRemarks(), domainId, connection);
		EventStoreModel.storeDomainEventsToDatabase(domain.getEvents(), domainId, connection);
		StatusStoreModel.storeDomainStatusToDatabase(domain.getStatus(), domainId, connection);
		LinkStoreModel.storeDomainLinksToDatabase(domain.getLinks(), domainId, connection);
		if (domain.getSecureDNS() != null) {
			domain.getSecureDNS().setDomainId(domainId);
			SecureDNSStoreModel.storeToDatabase(domain.getSecureDNS(), connection);
		}
		PublicIdStoreModel.storePublicIdByDomain(domain.getPublicIds(), domain.getId(), connection);
		VariantStoreModel.storeAllToDatabase(domain.getVariants(), domain.getId(), connection);

		if (domain.getNameServers().size() > 0) {
			if (useNameserverAsAttribute) {
				NameserverStoreModel.storeDomainNameserversAsAttributesToDatabase(domain.getNameServers(), domainId,
						connection);
			} else {
				storeDomainNameserversAsObjects(domain.getNameServers(), domainId, connection);
			}

		}
		storeDomainEntities(domain.getEntities(), domainId, connection);
		IpNetwork ipNetwork = domain.getIpNetwork();
		if (ipNetwork != null) {
			IpNetworkDbObj ipNetResult = IpNetworkModel.getByHandle(ipNetwork.getHandle(), connection);
			if (ipNetResult == null) {
				throw new NullPointerException(
						"IpNetwork: " + ipNetwork.getHandle() + "was not inserted previously to the database");
			}
			ipNetwork.setId(ipNetResult.getId());

			storeDomainIpNetworkRelationToDatabase(domainId, ipNetwork.getId(), connection);
		}
	}

	private static void storeDomainNameserversAsObjects(List<Nameserver> nameservers, Long domainId,
			Connection connection) throws SQLException {
		if (nameservers.size() > 0) {
			validateDomainNameservers(nameservers, connection);
			NameserverStoreModel.storeDomainNameserversToDatabase(nameservers, domainId, connection);
		}
	}

	private static void validateDomainNameservers(List<Nameserver> nameservers, Connection connection)
			throws SQLException {
		for (Nameserver ns : nameservers) {
			Long nsId = NameserverModel.getByHandle(ns.getHandle(), connection).getId();
			if (nsId == null) {
				throw new NullPointerException(
						"Nameserver: " + ns.getHandle() + "was not inserted previously to the database.");
			}
			ns.setId(nsId);
		}
	}

	private static void storeDomainEntities(List<Entity> entities, Long domainId, Connection connection)
			throws SQLException {
		if (entities.size() > 0) {
			EntityModel.validateParentEntities(entities, connection);
			RoleStoreModel.storeDomainEntityRoles(entities, domainId, connection);
		}

	}

	private static void isValidForStore(DomainDbObj domain) throws IncompleteObjectException {
		if (domain.getHandle() == null || domain.getHandle().isEmpty())
			throw new IncompleteObjectException("handle", "Domain");
		if (domain.getLdhName() == null || domain.getLdhName().isEmpty())
			throw new IncompleteObjectException("ldhName", "Domain");
	}

	private static void storeDomainIpNetworkRelationToDatabase(Long domainId, Long ipNetworkId, Connection connection)
			throws SQLException {
		String query = getQueryGroup().getQuery(STORE_IP_NETWORK_RELATION_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, domainId);
			statement.setLong(2, ipNetworkId);
			logger.log(Level.INFO, "Excuting QUERY:" + statement.toString());
			statement.executeUpdate();
		}
	}

	private static void fillPreparedStatement(PreparedStatement preparedStatement, Domain domain) throws SQLException {
		preparedStatement.setString(1, domain.getHandle());

		String domName = domain.getLdhName();
		String ldhName = DomainLabel.nameToASCII(domName);
		String unicodeName = DomainLabel.nameToUnicode(domName);

		if (ldhName.equals(unicodeName)) {
			preparedStatement.setString(2, ldhName);
			preparedStatement.setNull(3, Types.VARCHAR);
		} else {
			preparedStatement.setString(2, ldhName);
			preparedStatement.setString(3, unicodeName);
		}

		preparedStatement.setString(4, domain.getPort43());
		preparedStatement.setInt(5, ZoneModel.getIdByZoneName(domain.getZone()));

	}
}
