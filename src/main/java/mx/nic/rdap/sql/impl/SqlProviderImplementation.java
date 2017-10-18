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

	private static final String NAMESERVER_AS_DOMAIN_ATTRIBUTE_KEY = "nameserver_as_domain_attribute";

	private boolean useNsAsAttribute;

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
		if (useNsAsAttributeString != null && !useNsAsAttributeString.trim().isEmpty()) {
			useNsAsAttributeString = useNsAsAttributeString.trim();
			if (useNsAsAttributeString.equalsIgnoreCase("true")) {
				useNsAsAttribute = true;
			} else if (useNsAsAttributeString.equalsIgnoreCase("false")) {
				useNsAsAttribute = false;
			} else {
				throw new InitializationException("Property '" + NAMESERVER_AS_DOMAIN_ATTRIBUTE_KEY
						+ "' has an invalid value '" + useNsAsAttributeString + "', must 'true' or 'false'.");
			}
		} else {
			throw new InitializationException(
					"Property '" + NAMESERVER_AS_DOMAIN_ATTRIBUTE_KEY + "' must be declared and configured.");
		}
	}

	@Override
	public AutnumDAO getAutnumDAO() {
		return new AutnumDAOImpl();
	}

	@Override
	public DomainDAO getDomainDAO() {
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
		if (useNsAsAttribute) {
			return null;
		}

		return new NameserverDAOImpl();
	}

	@Override
	public RdapUserDAO getRdapUserDAO() {
		return new RdapUserDAOImpl();
	}

}
