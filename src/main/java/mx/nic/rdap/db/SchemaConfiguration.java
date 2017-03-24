package mx.nic.rdap.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import mx.nic.rdap.db.exception.ObjectNotFoundException;
import mx.nic.rdap.db.model.AutnumModel;
import mx.nic.rdap.db.model.CleanDatabaseModel;
import mx.nic.rdap.db.model.CountryCodeModel;
import mx.nic.rdap.db.model.DBConfigurationModel;
import mx.nic.rdap.db.model.DomainModel;
import mx.nic.rdap.db.model.DsDataModel;
import mx.nic.rdap.db.model.EntityModel;
import mx.nic.rdap.db.model.EventModel;
import mx.nic.rdap.db.model.IpAddressModel;
import mx.nic.rdap.db.model.IpNetworkModel;
import mx.nic.rdap.db.model.KeyDataModel;
import mx.nic.rdap.db.model.LinkModel;
import mx.nic.rdap.db.model.NameserverModel;
import mx.nic.rdap.db.model.PublicIdModel;
import mx.nic.rdap.db.model.RdapUserModel;
import mx.nic.rdap.db.model.RdapUserRoleModel;
import mx.nic.rdap.db.model.RemarkDescriptionModel;
import mx.nic.rdap.db.model.RemarkModel;
import mx.nic.rdap.db.model.RoleModel;
import mx.nic.rdap.db.model.SecureDNSModel;
import mx.nic.rdap.db.model.StatusModel;
import mx.nic.rdap.db.model.VCardModel;
import mx.nic.rdap.db.model.VCardPostalInfoModel;
import mx.nic.rdap.db.model.VariantModel;
import mx.nic.rdap.db.model.ZoneModel;

public class SchemaConfiguration {

	private static Properties serverProperties;

	private static boolean isTimerActive = false;

	public static void init(Properties properties) {
		serverProperties = properties;

		String schema;

		SQLProviderConfiguration.initForServer(properties);
		// get schema from configuration file.
		schema = SQLProviderConfiguration.getDefaultSchema();

		if (SQLProviderConfiguration.isDynamicSchemaOn()) {
			// load schema to configuration model
			DBConfigurationModel.loadQueryGroup(schema);
			// get an schema from the db configuration.
			try (Connection connection = DatabaseSession.getRdapConnection()) {
				schema = DBConfigurationModel.getSchemaSetting(connection);
			} catch (ObjectNotFoundException | SQLException e) {
				throw new RuntimeException(e);
			}

			createTimerSchema(schema);
		}
		initAllModels(schema);
	}

	/**
	 * Creates a timer to check schema updates.
	 * 
	 * @param initialDbSchema
	 *            The initial database schema
	 */
	public static synchronized void createTimerSchema(String initialDbSchema) {
		// If for some reason this function is called two or more times, we
		// don't want to create lot of timers.
		if (isTimerActive) {
			return;
		}
		SchemaReaderTask task = new SchemaReaderTask(initialDbSchema);
		Timer timer = new Timer("SchemaReaderThread", true);
		long seconds = SQLProviderConfiguration.getSchemaTimer();
		timer.schedule(task, TimeUnit.SECONDS.toMillis(seconds), TimeUnit.SECONDS.toMillis(seconds));
		isTimerActive = true;
	}

	public static void reInitAllModels(String schema) {
		initAllModels(schema);
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			CountryCodeModel.loadAllFromDatabase(connection);
		} catch (SQLException e) {
			throw new RuntimeException("Trouble loading country codes from the DB.", e);
		}

		try (Connection connection = DatabaseSession.getRdapConnection()) {
			ZoneModel.loadAllFromDatabase(connection);
		} catch (SQLException e) {
			throw new RuntimeException("Trouble loading zones from the DB.", e);
		}

		try {
			ZoneModel.validateConfiguratedZones(serverProperties);
		} catch (ObjectNotFoundException e) {
			throw new RuntimeException("Trouble found while validating zones.", e);
		}
	}

	public static void initAllModels(String schema) {
		AutnumModel.loadQueryGroup(schema);
		CleanDatabaseModel.loadQueryGroup(schema);
		CountryCodeModel.loadQueryGroup(schema);
		DBConfigurationModel.loadQueryGroup(schema);
		DomainModel.loadQueryGroup(schema);
		DsDataModel.loadQueryGroup(schema);
		KeyDataModel.loadQueryGroup(schema);
		EntityModel.loadQueryGroup(schema);
		EventModel.loadQueryGroup(schema);
		IpAddressModel.loadQueryGroup(schema);
		IpNetworkModel.loadQueryGroup(schema);
		LinkModel.loadQueryGroup(schema);
		NameserverModel.loadQueryGroup(schema);
		PublicIdModel.loadQueryGroup(schema);
		RdapUserModel.loadQueryGroup(schema);
		RdapUserRoleModel.loadQueryGroup(schema);
		RemarkModel.loadQueryGroup(schema);
		RemarkDescriptionModel.loadQueryGroup(schema);
		RoleModel.loadQueryGroup(schema);
		SecureDNSModel.loadQueryGroup(schema);
		StatusModel.loadQueryGroup(schema);
		VariantModel.loadQueryGroup(schema);
		VCardModel.loadQueryGroup(schema);
		VCardPostalInfoModel.loadQueryGroup(schema);
		ZoneModel.loadQueryGroup(schema);
	}
}
