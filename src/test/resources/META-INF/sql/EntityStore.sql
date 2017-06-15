#storeToDatabase
INSERT INTO {schema}.entity VALUES (null, ?, ?);

#getIdByHandle
SELECT ent_id FROM {schema}.entity ent WHERE ent.ent_handle = ?;