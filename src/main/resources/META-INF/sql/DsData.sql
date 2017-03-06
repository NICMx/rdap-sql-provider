#storeToDatabase
INSERT INTO {schema}.ds_data VALUES (null, ?, ?, ?, ?, ?);

#getBySecureDns
SELECT * FROM {schema}.ds_data ds WHERE ds.sdns_id = ?;

#deleteFromDatabase
DELETE FROM {schema}.ds_data WHERE sdns_id=?;