package mx.nic.rdap.db.exception;

import java.sql.SQLException;

/**
 * An object cannot be stored in the database because it has missing fields.
 */
public class IncompleteObjectException extends SQLException {

	/** Shut up, Java... */
	private static final long serialVersionUID = 1L;

	public IncompleteObjectException(String attributeName, String className) {
		super("Missing required value: " + attributeName + " in Class: " + className);
	}

}
