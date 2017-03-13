#storeToDatabase
INSERT INTO {schema}.key_data VALUES (null, ?, ?, ?, ?, ?);

#getBySecureDns
SELECT * FROM {schema}.key_data kd WHERE kd.sdns_id = ?;

#deleteFromDatabase
DELETE FROM {schema}.key_data WHERE sdns_id=?;