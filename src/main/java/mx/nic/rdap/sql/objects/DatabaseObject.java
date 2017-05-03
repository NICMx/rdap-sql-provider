package mx.nic.rdap.sql.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An object that can be store in the Database
 * 
 */
public interface DatabaseObject {
	/**
	 * Load the information coming from the database in an instance of the
	 * object
	 * 
	 * @param resultSet
	 *            ResultSet from where all information is obtained
	 * 
	 */
	public void loadFromDatabase(ResultSet resultSet) throws SQLException;

}
