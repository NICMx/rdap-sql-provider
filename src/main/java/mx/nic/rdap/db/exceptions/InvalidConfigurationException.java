package mx.nic.rdap.db.exceptions;

/**
 * Exception thrown when any configuration property is invalid.
 */
public class InvalidConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 5272104179530116951L;

	public InvalidConfigurationException() {
		super();
	}

	public InvalidConfigurationException(String message) {
		super(message);
	}

	public InvalidConfigurationException(Throwable cause) {
		super(cause);
	}

	public InvalidConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}
}
