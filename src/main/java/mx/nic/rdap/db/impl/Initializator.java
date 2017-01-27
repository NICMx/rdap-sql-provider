package mx.nic.rdap.db.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import mx.nic.rdap.db.DBConnection;
import mx.nic.rdap.db.DummyDatabaseCreator;
import mx.nic.rdap.db.exception.ObjectNotFoundException;
import mx.nic.rdap.db.model.CountryCodeModel;
import mx.nic.rdap.db.model.ZoneModel;
import mx.nic.rdap.db.spi.InitializeSpi;

public class Initializator implements InitializeSpi {

	public Initializator() {
	}

	public void init(Properties properties) {
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

		try (Connection connection = DBConnection.getConnection()) {
			CountryCodeModel.loadAllFromDatabase(connection);
		} catch (SQLException e) {
			throw new RuntimeException("Error loading countryCode ", e);
		}

		try (Connection connection = DBConnection.getConnection()) {
			ZoneModel.loadAllFromDatabase(connection);
		} catch (SQLException e) {
			throw new RuntimeException("Error loading zones ", e);
		}

		try {
			ZoneModel.validateConfiguratedZones(properties);
		} catch (ObjectNotFoundException e) {
			throw new RuntimeException("Error validating zones ", e);
		}
	}
}
