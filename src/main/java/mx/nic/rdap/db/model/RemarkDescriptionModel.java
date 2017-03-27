package mx.nic.rdap.db.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.core.db.RemarkDescription;
import mx.nic.rdap.db.QueryGroup;
import mx.nic.rdap.db.exception.ObjectNotFoundException;
import mx.nic.rdap.db.objects.RemarkDescriptionDbObj;

/**
 * Model for the {@link RemarkDescription} object
 * 
 */
public class RemarkDescriptionModel {

	private final static Logger logger = Logger.getLogger(RemarkDescriptionModel.class.getName());

	private final static String QUERY_GROUP = "RemarkDescription";

	private static final String GET_QUERY = "getByRemarkId";
	private static final String INSERT_QUERY = "storeToDatabase";

	protected static QueryGroup queryGroup = null;

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
			RemarkDescriptionModel.storeToDatabase(remarkDescription, connection);
		}
	}

	public static void storeToDatabase(RemarkDescription remarkDescription, Connection connection) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery(INSERT_QUERY))) {
			((RemarkDescriptionDbObj) remarkDescription).storeToDatabase(statement);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			statement.executeUpdate();
		}
	}

	/**
	 * @throws ObjectNotFoundException
	 *             Remarks are supposed to contain descriptions; they are not
	 *             optional. Callers need to concern themselves with this
	 *             situation.
	 */
	public static List<RemarkDescription> findByRemarkId(Long id, Connection connection)
			throws SQLException, ObjectNotFoundException {
		try (PreparedStatement statement = connection.prepareStatement(getQueryGroup().getQuery(GET_QUERY))) {
			statement.setLong(1, id);
			logger.log(Level.INFO, "Executing QUERY:" + statement.toString());
			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					throw new ObjectNotFoundException("The remark whose id is " + id + " has no descriptions.");
				}
				List<RemarkDescription> remarks = new ArrayList<RemarkDescription>();
				do {
					RemarkDescriptionDbObj remarkDescription = new RemarkDescriptionDbObj(resultSet);
					remarks.add(remarkDescription);
				} while (resultSet.next());
				return remarks;
			}
		}
	}

}
