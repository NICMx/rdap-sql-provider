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

import mx.nic.rdap.core.db.Autnum;
import mx.nic.rdap.db.exception.http.NotImplementedException;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.SQLProviderConfiguration;
import mx.nic.rdap.sql.objects.AutnumDbObj;

/**
 * Model for the {@link Autnum} Object
 * 
 */
public class AutnumModel {

	private final static Logger logger = Logger.getLogger(Autnum.class.getName());

	private final static String QUERY_GROUP = "Autnum";

	private static QueryGroup queryGroup = null;

	private static final String GET_BY_RANGE = "getByRange";
	private static final String GET_BY_ENTITY = "getAutnumByEntity";

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

	public static AutnumDbObj getByRange(long autnumValue, Connection connection)
			throws SQLException, NotImplementedException {
		String query = getQueryGroup().getQuery(GET_BY_RANGE);
		QueryGroup.userImplemented(query);

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, autnumValue);
			statement.setLong(2, autnumValue);
			logger.log(Level.INFO, "Executing query: " + statement.toString());
			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					return null;
				}
				AutnumDbObj autnum = new AutnumDbObj(resultSet);
				loadNestedObjects(autnum, connection);
				return autnum;
			}
		}
	}

	private static void loadNestedObjects(Autnum autnum, Connection connection) throws SQLException {
		Long autnumId = autnum.getId();

		autnum.getStatus().addAll(StatusModel.getByAutnumId(autnumId, connection));
		autnum.getRemarks().addAll(RemarkModel.getByAutnumId(autnumId, connection));
		autnum.getLinks().addAll(LinkModel.getByAutnumId(autnumId, connection));
		autnum.getEvents().addAll(EventModel.getByAutnumId(autnumId, connection));
		autnum.getEntities().addAll(EntityModel.getEntitiesByAutnumId(autnumId, connection));
	}

	public static List<Autnum> getByEntityId(Long entityId, Connection connection) throws SQLException {
		return getByRdapObjectId(entityId, connection, GET_BY_ENTITY);
	}

	private static List<Autnum> getByRdapObjectId(long id, Connection connection, String getQueryId)
			throws SQLException {
		String query = getQueryGroup().getQuery(getQueryId);

		if (SQLProviderConfiguration.isUserSQL() && query == null) {
			return Collections.emptyList();
		}

		List<Autnum> autnums = null;
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, id);
			logger.log(Level.INFO, "Executing QUERY: " + statement.toString());
			ResultSet rs = statement.executeQuery();
			if (!rs.next()) {
				return Collections.emptyList();
			}
			autnums = new ArrayList<Autnum>();

			do {
				AutnumDbObj autnum = new AutnumDbObj();
				autnum.loadFromDatabase(rs);
				autnums.add(autnum);
			} while (rs.next());
		}

		for (Autnum autnum : autnums) {
			loadSimpleNestedObjects(autnum, connection);
		}
		return autnums;
	}

	private static void loadSimpleNestedObjects(Autnum autnum, Connection connection) throws SQLException {
		Long ipNetworkId = autnum.getId();

		// Retrieve the events
		autnum.getEvents().addAll(EventModel.getByIpNetworkId(ipNetworkId, connection));

		// Retrieve the links
		autnum.getLinks().addAll(LinkModel.getByIpNetworkId(ipNetworkId, connection));

		// Retrieve the status
		autnum.getStatus().addAll(StatusModel.getByIpNetworkId(ipNetworkId, connection));

		// Retrieve the remarks
		autnum.getRemarks().addAll(RemarkModel.getByIpNetworkId(ipNetworkId, connection));
	}

}