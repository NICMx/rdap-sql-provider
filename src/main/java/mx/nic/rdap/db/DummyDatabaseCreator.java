package mx.nic.rdap.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.IDN;
import java.sql.Connection;
import java.sql.SQLException;

import org.h2.tools.RunScript;

/**
 *
 */
public class DummyDatabaseCreator {

	public static void createDatabaseTables() throws SQLException, IOException {
		try (Connection connection = DBConnection.getConnection();
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						QueryGroup.class.getClassLoader().getResourceAsStream("META-INF/sql/Create.sql")))) {
			IDN.toASCII("");
			RunScript.execute(connection, reader);
		}
	}
	
	public static void insertCatalogs() throws SQLException, IOException {
		try (Connection connection = DBConnection.getConnection();
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						QueryGroup.class.getClassLoader().getResourceAsStream("META-INF/sql/Catalogs.sql")))) {
			RunScript.execute(connection, reader);
		}
	}
	
	public static void insertDummyData() throws SQLException, IOException {
		try (Connection connection = DBConnection.getConnection();
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						QueryGroup.class.getClassLoader().getResourceAsStream("META-INF/sql/DummyData.sql")))) {
			RunScript.execute(connection, reader);
		}
	}
}
