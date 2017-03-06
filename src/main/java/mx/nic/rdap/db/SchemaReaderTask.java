package mx.nic.rdap.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.nic.rdap.db.exception.ObjectNotFoundException;
import mx.nic.rdap.db.model.DBConfigurationModel;

public class SchemaReaderTask extends TimerTask {

	Logger logger = Logger.getLogger(SchemaReaderTask.class.getName());

	private String lastSchemaName;

	public SchemaReaderTask(String schemaName) {
		this.lastSchemaName = schemaName;
	}

	@Override
	public void run() {
		String schemaFromDb;
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			schemaFromDb = DBConfigurationModel.getSchemaSetting(connection);
		} catch (ObjectNotFoundException | SQLException e) {
			// FIXME what kind of exception should be throw.
			throw new RuntimeException(e);
		}

		// compare lastSchema from recent schema.
		if (lastSchemaName.equals(schemaFromDb)) {
			return;
		}

		// if the schemas are different set the new schema
		logger.log(Level.INFO, "Establishing a new schema '" + schemaFromDb + "' for the sql queries.");
		lastSchemaName = schemaFromDb;

		SchemaConfiguration.reInitAllModels(schemaFromDb);
	}

}
