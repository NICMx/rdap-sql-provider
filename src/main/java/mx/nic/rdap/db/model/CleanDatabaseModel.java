package mx.nic.rdap.db.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.db.QueryGroup;

/**
 * Model which cleans the RDAP database
 */
public class CleanDatabaseModel {

	private final static Logger logger = Logger.getLogger(CleanDatabaseModel.class.getName());

	private final static String QUERY_GROUP = "Delete";

	private static QueryGroup queryGroup = null;

	static {
		try {
			queryGroup = new QueryGroup(QUERY_GROUP);
		} catch (IOException e) {
			throw new RuntimeException("Error loading query group");
		}
	}

	public static void cleanServerDatabase(Boolean migrateUser, Connection rdapConnection) throws SQLException {
		logger.log(Level.INFO, "******CLEANING RDAP DATABASE******");
		SortedSet<String> keys = new TreeSet<String>(queryGroup.getQueries().keySet());// Order
		// the
		// querys
		for (String queryName : keys) {
			try (PreparedStatement statement = rdapConnection.prepareStatement(queryGroup.getQuery(queryName));) {
				logger.log(Level.INFO, "Excuting QUERY" + queryName);
				statement.executeUpdate();
			}

		}
		if (migrateUser) {
			RdapUserModel.cleanRdapUserDatabase(rdapConnection);
		}
		logger.log(Level.INFO, "******CLEANING RDAP DATABASE SUCCESSFUL******");
	}

}
