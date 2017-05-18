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

import mx.nic.rdap.core.db.Remark;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.SQLProviderConfiguration;
import mx.nic.rdap.sql.exception.ObjectNotFoundException;
import mx.nic.rdap.sql.objects.RemarkDbObj;

/**
 * Model for the {@link Remark} Object
 * 
 */
public class RemarkModel {

	private static final Logger logger = Logger.getLogger(RemarkModel.class.getName());

	private static final String QUERY_GROUP = "Remark";

	private static final String NAMESERVER_GET_QUERY = "getByNameserverId";
	private static final String DOMAIN_GET_QUERY = "getByDomainId";
	private static final String ENTITY_GET_QUERY = "getByEntityId";
	private static final String AUTNUM_GET_QUERY = "getByAutnumId";
	private static final String IP_NETWORK_GET_QUERY = "getByIpNetworkId";

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

	private static List<Remark> getByRelationId(Long id, Connection connection, String queryId) throws SQLException {
		String query = getQueryGroup().getQuery(queryId);
		if (SQLProviderConfiguration.isUserSQL() && query == null) {
			return Collections.emptyList();
		}
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, id);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			try (ResultSet resultSet = statement.executeQuery();) {
				return processResultSet(resultSet, connection);
			}
		}
	}

	public static List<Remark> getByNameserverId(Long nameserverId, Connection connection) throws SQLException {
		return getByRelationId(nameserverId, connection, NAMESERVER_GET_QUERY);
	}

	public static List<Remark> getByDomainId(Long domainId, Connection connection) throws SQLException {
		return getByRelationId(domainId, connection, DOMAIN_GET_QUERY);
	}

	public static List<Remark> getByEntityId(Long entityId, Connection connection) throws SQLException {
		return getByRelationId(entityId, connection, ENTITY_GET_QUERY);
	}

	public static List<Remark> getByAutnumId(Long autnumId, Connection connection) throws SQLException {
		return getByRelationId(autnumId, connection, AUTNUM_GET_QUERY);
	}

	public static List<Remark> getByIpNetworkId(Long ipNetworkId, Connection connection) throws SQLException {
		return getByRelationId(ipNetworkId, connection, IP_NETWORK_GET_QUERY);
	}

	/**
	 * As a contract, never returns <code>null</code> nor throws
	 * {@link ObjectNotFoundException}. This is because remarks are never
	 * mandatory, and the callers should not worry about their nonexistence.
	 */
	private static List<Remark> processResultSet(ResultSet resultSet, Connection connection) throws SQLException {
		if (!resultSet.next()) {
			return Collections.emptyList();
		}

		List<Remark> remarks = new ArrayList<Remark>();
		do {
			RemarkDbObj remark = new RemarkDbObj(resultSet);

			try {
				remark.getDescriptions().addAll(RemarkDescriptionModel.findByRemarkId(remark.getId(), connection));
			} catch (ObjectNotFoundException e) {
				// Descriptions are supposed to be mandatory; looks like we're
				// dealing with a corrupted database.
				// It's not our fault though, so just log it and continue.
				logger.log(Level.WARNING, "Some remark lacks descriptions.", e);
				continue;
			}

			remark.getLinks().addAll(LinkModel.getByRemarkId(remark.getId(), connection));
			remarks.add(remark);
		} while (resultSet.next());
		return remarks;
	}

}
