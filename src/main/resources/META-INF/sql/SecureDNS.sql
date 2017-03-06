#storeToDatabase
INSERT INTO {schema}.secure_dns VALUES (null, ?, ?, ?, ?);

#getByDomain
SELECT * FROM {schema}.secure_dns s WHERE s.dom_id = ?;

#deleteFromDatabase
DELETE FROM {schema}.secure_dns WHERE dom_id=?;