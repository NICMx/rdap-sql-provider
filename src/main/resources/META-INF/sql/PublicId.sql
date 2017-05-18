#getByDomain
SELECT pid.* FROM {schema}.public_id pid INNER JOIN {schema}.domain_public_ids dom ON pid.pid_id = dom.pid_id WHERE dom.dom_id=?;

#getByEntity
SELECT pid.* FROM {schema}.public_id pid INNER JOIN {schema}.entity_public_ids ent ON pid.pid_id = ent.pid_id WHERE ent.ent_id=?;

