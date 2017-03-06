#storeToDatabase
INSERT INTO {schema}.vcard VALUES (null, ?, ?, ?, ?, ?, ?, ?, ?);

#storeEntityContact
INSERT INTO {schema}.entity_contact VALUES (?, ?);

#getById
SELECT * FROM {schema}.vcard v WHERE v.vca_id = ?;

#getAll
SELECT * FROM {schema}.vcard ORDER BY 1 ASC;

#getByEntityId
SELECT vca.* FROM {schema}.vcard vca JOIN {schema}.entity_contact eco ON eco.vca_id = vca.vca_id WHERE eco.ent_id = ?;

#deleteById
DELETE FROM {schema}.vcard WHERE vca_id IN (?);

#deleteRegistrarContact
DELETE FROM {schema}.entity_contact WHERE ent_id=?;