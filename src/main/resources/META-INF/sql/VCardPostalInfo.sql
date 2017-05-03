#getAll
SELECT * FROM {schema}.vcard_postal_info ORDER BY 1 ASC;

#getByVCardId
SELECT * FROM {schema}.vcard_postal_info vpi WHERE vpi.vca_id = ?;

