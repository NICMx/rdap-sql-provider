#getProperty
SELECT * FROM {schema}.configuration conf WHERE conf.con_name = ?;

#getAll
SELECT * FROM {schema}.configuration;
