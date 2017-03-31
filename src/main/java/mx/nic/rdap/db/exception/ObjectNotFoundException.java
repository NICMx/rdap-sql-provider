package mx.nic.rdap.db.exception;

import java.sql.SQLException;

import mx.nic.rdap.db.exception.http.NotFoundException;

/**
 * "An object was mandatory but it could not be found."
 * <p>
 * While a {@link NotFoundException} is supposed to become a 404, an
 * "ObjectNotFoundException" actually means that there is something wrong with
 * the database. Something was SUPPOSED to be there (by RFC constraints for
 * example) but wasn't. This exception needs to be catched and dealt with before
 * reaching the server's code, which is the reason why it doesn't extend from
 * {@link RdapDataAccessException}, and therefore cannot be thrown from the API.
 * <p>
 * Also, the reason why this doesn't extend from {@link SQLException} is because
 * it often has to be catched really early and we don't want to accidentally mix
 * them.
 * 
 * @see NotFoundException
 */
public class ObjectNotFoundException extends Exception {

	/** Blah blah bah needless Java nonsense. */
	private static final long serialVersionUID = 1L;

	public ObjectNotFoundException(String msg) {
		super(msg);
	}

	public ObjectNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
