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

import mx.nic.rdap.core.db.DomainLabel;
import mx.nic.rdap.core.db.Nameserver;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.exception.IncompleteObjectException;
import mx.nic.rdap.sql.model.EntityModel;

/**
 * Model for the {@link Nameserver} Object
 * 
 */
public class NameserverStoreModel {

	private final static Logger logger = Logger.getLogger(NameserverStoreModel.class.getName());

	private final static String QUERY_GROUP = "NameserverStore";

	private static QueryGroup queryGroup = null;

	private static final String STORE_QUERY = "storeToDatabase";

	private static final String DOMAIN_STORE_QUERY = "storeDomainNameserversToDatabase";

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

	private static void isValidForStore(Nameserver nameserver, boolean useNameserverAsAttribute)
			throws IncompleteObjectException {
		if (!useNameserverAsAttribute && (nameserver.getHandle() == null || nameserver.getHandle().isEmpty()))
			throw new IncompleteObjectException("handle", "Nameserver");
		if (nameserver.getLdhName() == null || nameserver.getLdhName().isEmpty())
			throw new IncompleteObjectException("ldhName", "Nameserver");
	}

	// Store as object
	public static void storeToDatabase(Nameserver nameserver, Connection connection) throws SQLException {
		storeToDatabase(nameserver, false, connection);
	}

	private static void storeToDatabase(Nameserver nameserver, boolean useNameserverAsAttribute, Connection connection)
			throws SQLException {
		isValidForStore(nameserver, useNameserverAsAttribute);
		String query = getQueryGroup().getQuery(STORE_QUERY);
		Long nameserverId = null;
		try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			fillPreparedStatement(statement, nameserver);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			result.next();
			nameserverId = result.getLong(1);// The id of the nameserver
												// inserted
			nameserver.setId(nameserverId);
		}
		storeNestedObjects(nameserver, connection);
	}

	private static void storeNestedObjects(Nameserver nameserver, Connection connection) throws SQLException {
		Long nameserverId = nameserver.getId();
		IpAddressStoreModel.storeToDatabase(nameserver.getIpAddresses(), nameserverId, connection);
		StatusStoreModel.storeNameserverStatusToDatabase(nameserver.getStatus(), nameserverId, connection);
		RemarkStoreModel.storeNameserverRemarksToDatabase(nameserver.getRemarks(), nameserverId, connection);
		LinkStoreModel.storeNameserverLinksToDatabase(nameserver.getLinks(), nameserverId, connection);
		EventStoreModel.storeNameserverEventsToDatabase(nameserver.getEvents(), nameserverId, connection);
		storeNameserverEntities(nameserver, connection);
	}

	private static void storeNameserverEntities(Nameserver nameserver, Connection connection) throws SQLException {
		if (nameserver.getEntities().size() > 0) {
			EntityModel.validateParentEntities(nameserver.getEntities(), connection);
			RoleStoreModel.storeNameserverEntityRoles(nameserver.getEntities(), nameserver.getId(), connection);
		}

	}

	/**
	 * Store a list of nameservers that belong from a domain
	 * 
	 */
	public static void storeDomainNameserversToDatabase(List<Nameserver> nameservers, Long domainId,
			Connection connection) throws SQLException {
		if (nameservers.isEmpty()) {
			return;
		}

		String query = getQueryGroup().getQuery(DOMAIN_STORE_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			Long nameserverId;
			for (Nameserver nameserver : nameservers) {
				statement.setLong(1, domainId);
				nameserverId = nameserver.getId();
				statement.setLong(2, nameserverId);
				logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
				statement.executeUpdate();
			}
		}
	}

	public static void storeDomainNameserversAsAttributesToDatabase(List<Nameserver> nameservers, Long domainId,
			Connection connection) throws SQLException {
		for (Nameserver ns : nameservers) {
			NameserverStoreModel.storeToDatabase(ns, true, connection);
		}
		storeDomainNameserversToDatabase(nameservers, domainId, connection);

	}

	private static void fillPreparedStatement(PreparedStatement preparedStatement, Nameserver ns) throws SQLException {
		preparedStatement.setString(1, ns.getHandle());

		String nsName = ns.getLdhName();
		String ldhName = DomainLabel.nameToASCII(nsName);
		String unicodeName = DomainLabel.nameToUnicode(nsName);
		if (ldhName.equals(unicodeName)) {
			preparedStatement.setString(2, ldhName);
			preparedStatement.setNull(3, Types.VARCHAR);
		} else {
			preparedStatement.setString(2, ldhName);
			preparedStatement.setString(3, unicodeName);
		}

		preparedStatement.setString(4, ns.getPort43());
	}
}