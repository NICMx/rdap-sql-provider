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

import mx.nic.rdap.core.db.Remark;
import mx.nic.rdap.db.QueryGroup;
import mx.nic.rdap.db.exception.ObjectNotFoundException;
import mx.nic.rdap.db.exception.RequiredValueNotFoundException;
import mx.nic.rdap.db.objects.RemarkDAO;

/**
 * Model for the {@link Remark} Object
 * 
 */
public class RemarkModel {

	private static final Logger logger = Logger.getLogger(RemarkModel.class.getName());

	private static final String QUERY_GROUP = "Remark";

	private static final String NAMESERVER_STORE_QUERY = "storeNameserverRemarksToDatabase";
	private static final String DOMAIN_STORE_QUERY = "storeDomainRemarksToDatabase";
	private static final String ENTITY_STORE_QUERY = "storeEntityRemarksToDatabase";
	private static final String AUTNUM_STORE_QUERY = "storeAutnumRemarksToDatabase";
	private static final String IP_NETWORK_STORE_QUERY = "storeIpNetworkRemarksToDatabase";

	private static final String NAMESERVER_GET_QUERY = "getByNameserverId";
	private static final String DOMAIN_GET_QUERY = "getByDomainId";
	private static final String ENTITY_GET_QUERY = "getByEntityId";
	private static final String AUTNUM_GET_QUERY = "getByAutnumId";
	private static final String IP_NETWORK_GET_QUERY = "getByIpNetworkId";

	protected static QueryGroup queryGroup = null;

	static {
		try {
			RemarkModel.queryGroup = new QueryGroup(QUERY_GROUP);
		} catch (IOException e) {
			throw new RuntimeException("Error loading query group");
		}
	}

	public static long storeToDatabase(Remark remark, Connection connection)
			throws SQLException, RequiredValueNotFoundException {

		// The Remark's id is autoincremental, Statement.RETURN_GENERATED_KEYS
		// give us the id generated for the object stored
		try (PreparedStatement statement = connection.prepareStatement(queryGroup.getQuery("storeToDatabase"),
				Statement.RETURN_GENERATED_KEYS)) {
			((RemarkDAO) remark).storeToDatabase(statement);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			result.next();
			// The id of the remark inserted
			Long remarkInsertedId = result.getLong(1);
			remark.setId(remarkInsertedId);
			RemarkDescriptionModel.storeAllToDatabase(remark.getDescriptions(), remarkInsertedId, connection);
			LinkModel.storeRemarkLinksToDatabase(remark.getLinks(), remarkInsertedId, connection);
			return remarkInsertedId;
		}
	}

	private static void storeRelationRemarksToDatabase(List<Remark> remarks, Long id, Connection connection,
			String queryId) throws SQLException, RequiredValueNotFoundException {
		if (remarks.isEmpty())
			return;

		String query = queryGroup.getQuery(queryId);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			for (Remark remark : remarks) {
				Long remarkId = RemarkModel.storeToDatabase(remark, connection);
				statement.setLong(1, id);
				statement.setLong(2, remarkId);
				logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
				statement.executeUpdate();
			}
		}
	}

	public static void storeNameserverRemarksToDatabase(List<Remark> remarks, Long nameserverId, Connection connection)
			throws SQLException, RequiredValueNotFoundException {
		storeRelationRemarksToDatabase(remarks, nameserverId, connection, NAMESERVER_STORE_QUERY);
	}

	public static void storeDomainRemarksToDatabase(List<Remark> remarks, Long domainId, Connection connection)
			throws SQLException, RequiredValueNotFoundException {
		storeRelationRemarksToDatabase(remarks, domainId, connection, DOMAIN_STORE_QUERY);
	}

	public static void storeAutnumRemarksToDatabase(List<Remark> remarks, Long autnumId, Connection connection)
			throws SQLException, RequiredValueNotFoundException {
		storeRelationRemarksToDatabase(remarks, autnumId, connection, AUTNUM_STORE_QUERY);
	}

	public static void storeEntityRemarksToDatabase(List<Remark> remarks, Long entityId, Connection connection)
			throws SQLException, RequiredValueNotFoundException {
		storeRelationRemarksToDatabase(remarks, entityId, connection, ENTITY_STORE_QUERY);
	}

	public static void storeIpNetworkRemarksToDatabase(List<Remark> remarks, Long ipNetworkId, Connection connection)
			throws SQLException, RequiredValueNotFoundException {
		storeRelationRemarksToDatabase(remarks, ipNetworkId, connection, IP_NETWORK_STORE_QUERY);
	}

	private static List<Remark> getByRelationId(Long id, Connection connection, String queryId)
			throws SQLException, ObjectNotFoundException {
		String query = queryGroup.getQuery(queryId);
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, id);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			try (ResultSet resultSet = statement.executeQuery();) {
				return processResultSet(resultSet, connection);
			}
		}
	}

	public static List<Remark> getByNameserverId(Long nameserverId, Connection connection)
			throws SQLException, ObjectNotFoundException {
		return getByRelationId(nameserverId, connection, NAMESERVER_GET_QUERY);
	}

	public static List<Remark> getByDomainId(Long domainId, Connection connection)
			throws SQLException, ObjectNotFoundException {
		return getByRelationId(domainId, connection, DOMAIN_GET_QUERY);
	}

	public static List<Remark> getByEntityId(Long entityId, Connection connection)
			throws SQLException, ObjectNotFoundException {
		return getByRelationId(entityId, connection, ENTITY_GET_QUERY);
	}

	public static List<Remark> getByAutnumId(Long autnumId, Connection connection)
			throws SQLException, ObjectNotFoundException {
		return getByRelationId(autnumId, connection, AUTNUM_GET_QUERY);
	}

	public static List<Remark> getByIpNetworkId(Long ipNetworkId, Connection connection)
			throws SQLException, ObjectNotFoundException {
		return getByRelationId(ipNetworkId, connection, IP_NETWORK_GET_QUERY);
	}

	public static List<Remark> getAll(Connection connection) throws SQLException, ObjectNotFoundException {
		try (PreparedStatement statement = connection.prepareStatement(queryGroup.getQuery("getAll"));
				ResultSet resultSet = statement.executeQuery();) {
			return processResultSet(resultSet, connection);
		}
	}

	private static List<Remark> processResultSet(ResultSet resultSet, Connection connection)
			throws SQLException, ObjectNotFoundException {
		if (!resultSet.next()) {
			return Collections.emptyList();
		}
		List<Remark> remarks = new ArrayList<Remark>();
		do {
			RemarkDAO remark = new RemarkDAO(resultSet);
			// load the remark descriptions of the remark
			remark.setDescriptions(RemarkDescriptionModel.findByRemarkId(remark.getId(), connection));
			// Load the remark's links
			remark.getLinks().addAll(LinkModel.getByRemarkId(remark.getId(), connection));
			remarks.add(remark);
		} while (resultSet.next());
		return remarks;
	}

}
