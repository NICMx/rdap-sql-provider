package mx.nic.rdap.store.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.db.RemarkDescription;
import mx.nic.rdap.sql.QueryGroup;

/**
 * Model for the {@link RemarkDescription} object
 * 
 */
public class RemarkDescriptionStoreModel {

	private final static Logger logger = Logger.getLogger(RemarkDescriptionStoreModel.class.getName());

	private final static String QUERY_GROUP = "RemarkDescriptionStore";

	private static final String INSERT_QUERY = "storeToDatabase";

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

	public static void storeAllToDatabase(List<RemarkDescription> descriptions, Long remarkInsertedId,
			Connection connection) throws SQLException {
		for (RemarkDescription remarkDescription : descriptions) {
			remarkDescription.setRemarkId(remarkInsertedId);
			RemarkDescriptionStoreModel.storeToDatabase(remarkDescription, connection);
		}
	}

	private static void storeToDatabase(RemarkDescription remarkDescription, Connection connection)
			throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery(INSERT_QUERY))) {
			fillPreparedStatement(statement, remarkDescription);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();
		}
	}

	private static void fillPreparedStatement(PreparedStatement preparedStatement, RemarkDescription description)
			throws SQLException {
		preparedStatement.setInt(1, description.getOrder());
		preparedStatement.setLong(2, description.getRemarkId());
		preparedStatement.setString(3, description.getDescription());
	}

}
