package mx.nic.rdap.sql.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import mx.nic.rdap.core.catalog.EventAction;
import mx.nic.rdap.core.db.Event;

/**
 * Data access class for the {@link Event} object
 */
public class EventDbObj extends Event implements DatabaseObject {

	/**
	 * Default Constructor
	 */
	public EventDbObj() {
		super();
	}

	/**
	 * Constructs an Event using a {@link ResultSet}
	 */
	public EventDbObj(ResultSet resultSet) throws SQLException {
		super();
		this.loadFromDatabase(resultSet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mx.nic.rdap.core.db.DatabaseObject#loadFromDatabase(java.sql.ResultSet)
	 */
	@Override
	public void loadFromDatabase(ResultSet resultSet) throws SQLException {
		this.setId(resultSet.getLong("eve_id"));
		this.setEventAction(EventAction.getById(resultSet.getInt("eac_id")));
		this.setEventActor(resultSet.getString("eve_actor"));
		this.setEventDate(new Date(resultSet.getTimestamp("eve_date").getTime()));

	}

}
