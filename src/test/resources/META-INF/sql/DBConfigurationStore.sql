#getProperty
SELECT con_name, con_value, con_previous_value, con_description, con_create_time, con_last_update FROM {schema}.configuration conf WHERE conf.con_name = ?;

#getAll
SELECT con_name, con_value, con_previous_value, con_description, con_create_time, con_last_update FROM {schema}.configuration;

#updateProperty
UPDATE {schema}.configuration SET con_value = ? , con_previous_value = ?, con_last_update = ? WHERE con_name = ?;
