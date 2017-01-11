package mx.nic.rdap.db.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import mx.nic.rdap.db.DBConnection;
import mx.nic.rdap.db.exception.ObjectNotFoundException;
import mx.nic.rdap.db.model.CountryCodeModel;
import mx.nic.rdap.db.model.ZoneModel;
import mx.nic.rdap.db.spi.InitializeSpi;

public class Initializator implements InitializeSpi {

	public Initializator() {
	}

	public void init(Properties properties) {
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
