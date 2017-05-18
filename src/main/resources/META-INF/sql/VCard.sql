#getAll
SELECT * FROM {schema}.vcard ORDER BY 1 ASC;

#getByEntityId
SELECT vca.* FROM {schema}.vcard vca JOIN {schema}.entity_contact eco ON eco.vca_id = vca.vca_id WHERE eco.ent_id = ?;
