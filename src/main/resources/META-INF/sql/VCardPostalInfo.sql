#storeToDatabase
INSERT INTO {schema}.vcard_postal_info VALUES (null, ?, ?, ?, ?, ?, ?, ?, ?, ?);

#getAll
SELECT * FROM {schema}.vcard_postal_info ORDER BY 1 ASC;

#getByVCardId
SELECT * FROM {schema}.vcard_postal_info vpi WHERE vpi.vca_id = ?;

#deleteByVCardId
DELETE FROM {schema}.vcard_postal_info WHERE vca_id = ?;

