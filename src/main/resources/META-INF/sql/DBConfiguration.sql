#getProperty
SELECT * FROM {schema}.configuration conf WHERE conf.con_name = ?;

#getAll
SELECT * FROM {schema}.configuration;

#updateProperty
UPDATE {schema}.configuration SET con_value = ? , con_previous_value = ?, con_last_update = ? WHERE con_name = ?;