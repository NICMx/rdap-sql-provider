package mx.nic.rdap.db.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.h2.tools.RunScript;

import mx.nic.rdap.sql.DatabaseSession;
import mx.nic.rdap.sql.QueryGroup;

/**
 *
 */
public class DummyDatabaseCreator {

	public static void createDatabaseTables() throws SQLException, IOException {
		try (Connection connection = DatabaseSession.getRdapConnection();
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						QueryGroup.class.getClassLoader().getResourceAsStream("META-INF/sql/Create.sql")))) {
			RunScript.execute(connection, reader);
		}
	}

	public static void insertCatalogs() throws SQLException, IOException {
		try (Connection connection = DatabaseSession.getRdapConnection();
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						QueryGroup.class.getClassLoader().getResourceAsStream("META-INF/sql/Catalogs.sql")))) {
			RunScript.execute(connection, reader);
		}
	}

	public static void insertDummyData() throws SQLException, IOException {
		Path userDummyData = Paths.get("webapp/META-INF/demodb_sql/DummyData.sql");

		if (Files.exists(userDummyData)) {
			Logger.getLogger(DummyDatabaseCreator.class.getName()).log(Level.INFO,
					"Attemptin to use Custom User dummy data: " + userDummyData.toAbsolutePath().toString());
			try (Connection connection = DatabaseSession.getRdapConnection();
					BufferedReader reader = Files.newBufferedReader(userDummyData);) {
				RunScript.execute(connection, reader);
			}
		} else {
			try (Connection connection = DatabaseSession.getRdapConnection();
					BufferedReader reader = new BufferedReader(new InputStreamReader(
							QueryGroup.class.getClassLoader().getResourceAsStream("META-INF/sql/DummyData.sql")))) {
				RunScript.execute(connection, reader);
			}
		}
	}
}