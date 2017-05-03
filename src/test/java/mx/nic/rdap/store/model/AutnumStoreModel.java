package mx.nic.rdap.store.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.db.Autnum;
import mx.nic.rdap.core.db.Entity;
import mx.nic.rdap.sql.QueryGroup;
import mx.nic.rdap.sql.exception.IncompleteObjectException;
import mx.nic.rdap.sql.model.CountryCodeModel;
import mx.nic.rdap.sql.model.EntityModel;

/**
 * Model for the {@link Autnum} Object
 * 
 */
public class AutnumStoreModel {

	private final static Logger logger = Logger.getLogger(Autnum.class.getName());

	private final static String QUERY_GROUP = "AutnumStore";

	private static QueryGroup queryGroup = null;

	private static final String STORE_QUERY = "storeToDatabase";

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

	/**
	 * Stores Object autnum to database
	 * 
	 */
	public static Long storeToDatabase(Autnum autnum, Connection connection) throws SQLException {

		isValidForStore(autnum);

		Long autnumId;
		String query = getQueryGroup().getQuery(STORE_QUERY);
		try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			fillPreparedStatement(statement, autnum);
			logger.log(Level.INFO, "Executing query:" + statement.toString());
			statement.executeUpdate();
			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.next();
			autnumId = resultSet.getLong(1);
			autnum.setId(autnumId);
		}
		StatusStoreModel.storeAutnumStatusToDatabase(autnum.getStatus(), autnumId, connection);
		RemarkStoreModel.storeAutnumRemarksToDatabase(autnum.getRemarks(), autnumId, connection);
		LinkStoreModel.storeAutnumLinksToDatabase(autnum.getLinks(), autnumId, connection);
		EventStoreModel.storeAutnumEventsToDatabase(autnum.getEvents(), autnumId, connection);
		for (Entity ent : autnum.getEntities()) {
			Long entId = EntityModel.getIdByHandle(ent.getHandle(), connection);
			if (entId == null) {
				throw new NullPointerException(
						"Entity: " + ent.getHandle() + "was not inserted previously to the database");
			}
			ent.setId(entId);
		}
		RoleStoreModel.storeAutnumEntityRoles(autnum.getEntities(), autnumId, connection);

		return autnumId;
	}

	private static void isValidForStore(Autnum autnum) throws IncompleteObjectException {
		if (autnum.getHandle() == null || autnum.getHandle().isEmpty())
			throw new IncompleteObjectException("handle", "Autnum");
		if (autnum.getStartAutnum() == null)
			throw new IncompleteObjectException("startAutnum", "Autnum");
		if (autnum.getEndAutnum() == null)
			throw new IncompleteObjectException("endAutnum", "Autnum");
		if (autnum.getStartAutnum() > autnum.getEndAutnum()) {
			throw new RuntimeException("Starting ASN is greater than final ASN");
		}
	}

	private static void fillPreparedStatement(PreparedStatement preparedStatement, Autnum autnum) throws SQLException {
		preparedStatement.setString(1, autnum.getHandle());
		preparedStatement.setLong(2, autnum.getStartAutnum());
		preparedStatement.setLong(3, autnum.getEndAutnum());
		preparedStatement.setString(4, autnum.getName());
		preparedStatement.setString(5, autnum.getType());
		preparedStatement.setString(6, autnum.getPort43());
		preparedStatement.setInt(7, CountryCodeModel.getIdByCountryName(autnum.getCountryCode()));
	}

}