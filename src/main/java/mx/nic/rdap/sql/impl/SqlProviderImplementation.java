package mx.nic.rdap.sql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import mx.nic.rdap.db.exception.InitializationException;
import mx.nic.rdap.db.spi.AutnumDAO;
import mx.nic.rdap.db.spi.DataAccessImplementation;
import mx.nic.rdap.db.spi.DomainDAO;
import mx.nic.rdap.db.spi.EntityDAO;
import mx.nic.rdap.db.spi.IpNetworkDAO;
import mx.nic.rdap.db.spi.NameserverDAO;
import mx.nic.rdap.db.spi.RdapUserDAO;
import mx.nic.rdap.sql.DatabaseSession;
import mx.nic.rdap.sql.SchemaConfiguration;
import mx.nic.rdap.sql.exception.ObjectNotFoundException;
import mx.nic.rdap.sql.model.CountryCodeModel;
import mx.nic.rdap.sql.model.ZoneModel;

public class SqlProviderImplementation implements DataAccessImplementation {

	@Override
	public void init(Properties properties) throws InitializationException {
		DatabaseSession.initRdapConnection(properties);
		SchemaConfiguration.init(properties);

		try (Connection connection = DatabaseSession.getRdapConnection()) {
			CountryCodeModel.loadAllFromDatabase(connection);
		} catch (SQLException e) {
			throw new InitializationException("Trouble loading country codes from the DB.", e);
		}

		try (Connection connection = DatabaseSession.getRdapConnection()) {
			ZoneModel.loadAllFromDatabase(connection);
		} catch (SQLException e) {
			throw new InitializationException("Trouble loading zones from the DB.", e);
		}

		try {
			ZoneModel.validateConfiguredZones();
		} catch (InitializationException e) {
			throw e;
		} catch (ObjectNotFoundException e) {
			throw new InitializationException("Trouble found while validating zones.", e);
		}
	}

	@Override
	public AutnumDAO getAutnumDAO() {
		return new AutnumDAOImpl();
	}

	@Override
	public DomainDAO getDomainDAO() {
		return new DomainDAOImpl();
	}

	@Override
	public EntityDAO getEntityDAO() {
		return new EntityDAOImpl();
	}

	@Override
	public IpNetworkDAO getIpNetworkDAO() {
		return new IpNetworkDAOImpl();
	}

	@Override
	public NameserverDAO getNameserverDAO() {
		return new NameserverDAOImpl();
	}

	@Override
	public RdapUserDAO getRdapUserDAO() {
		return new RdapUserDAOImpl();
	}

}
