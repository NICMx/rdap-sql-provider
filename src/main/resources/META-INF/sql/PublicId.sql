
#storeToDatabase
INSERT INTO {schema}.public_id VALUES (null,?,?);

#storeDomainPublicIdsToDatabase
INSERT INTO {schema}.domain_public_ids VALUES (?,?);

#storeEntityPublicIdsToDatabase
INSERT INTO {schema}.entity_public_ids VALUES (?,?);

#getAll
SELECT * FROM {schema}.public_id;

#getByDomain
SELECT pid.* FROM {schema}.public_id pid INNER JOIN {schema}.domain_public_ids dom ON pid.pid_id = dom.pid_id WHERE dom.dom_id=?;

#getByEntity
SELECT pid.* FROM {schema}.public_id pid INNER JOIN {schema}.entity_public_ids ent ON pid.pid_id = ent.pid_id WHERE ent.ent_id=?;

#deleteById
DELETE FROM {schema}.public_id WHERE pid_id IN (?);

#deleteEntityPublicId
DELETE FROM {schema}.entity_public_ids WHERE ent_id = ?;

#deleteDomainPublicId
DELETE FROM {schema}.domain_public_ids WHERE dom_id = ?;