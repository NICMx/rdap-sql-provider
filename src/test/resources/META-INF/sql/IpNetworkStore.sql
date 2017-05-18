
#storeToDatabase
INSERT INTO {schema}.ip_network VALUES (null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);

#getByHandle
SELECT * FROM {schema}.ip_network ipn WHERE ipn.ine_handle = ?;