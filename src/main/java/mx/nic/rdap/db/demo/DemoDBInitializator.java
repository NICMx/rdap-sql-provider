package mx.nic.rdap.db.demo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class DemoDBInitializator {

	public DemoDBInitializator() {
	}

	public static void init(Properties properties) {
		try {
			DummyDatabaseCreator.createDatabaseTables();
		} catch (SQLException | IOException e) {
			throw new RuntimeException("Error creating the dummy database", e);
		}

		try {
			DummyDatabaseCreator.insertCatalogs();
		} catch (SQLException | IOException e) {
			throw new RuntimeException("Error inserting catalogs", e);
		}
		try {
			DummyDatabaseCreator.insertDummyData();
		} catch (SQLException | IOException e) {
			throw new RuntimeException("Error inserting dummy data", e);
		}

	}
}