package mx.nic.rdap.sql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

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

	private static final String NAMESERVER_AS_DOMAIN_ATTRIBUTE_KEY = "nameserver_as_domain_attribute";
	private static final Logger logger = Logger.getLogger(SqlProviderImplementation.class.getName());

	private Boolean useNsAsAttribute;

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
			ZoneModel.validateConfiguredZones(properties);
		} catch (InitializationException e) {
			throw e;
		} catch (ObjectNotFoundException e) {
			throw new InitializationException("Trouble found while validating zones.", e);
		}

		String useNsAsAttributeString = properties.getProperty(NAMESERVER_AS_DOMAIN_ATTRIBUTE_KEY);
		if (useNsAsAttributeString != null) {
			useNsAsAttribute = Boolean.parseBoolean(useNsAsAttributeString);
		} else {
			logger.info("Note: The key '" + NAMESERVER_AS_DOMAIN_ATTRIBUTE_KEY
					+ "' is not present in the data access configuration. "
					+ "Domain and Nameserver requests will be rejected.");
		}
	}

	@Override
	public AutnumDAO getAutnumDAO() {
		return new AutnumDAOImpl();
	}

	@Override
	public DomainDAO getDomainDAO() {
		if (useNsAsAttribute == null) {
			return null;
		}

		return new DomainDAOImpl(useNsAsAttribute);
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
		// Asking for NS when they are being stored as attributes does not seem
		// to make sense.
		if (useNsAsAttribute == null || useNsAsAttribute) {
			return null;
		}

		return new NameserverDAOImpl();
	}

	@Override
	public RdapUserDAO getRdapUserDAO() {
		return new RdapUserDAOImpl();
	}

}
