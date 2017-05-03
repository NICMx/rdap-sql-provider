#getByHandle
SELECT * FROM {schema}.entity e WHERE e.ent_handle = ?;

#getByDomain
SELECT ent.*, dom.rol_id FROM {schema}.entity ent JOIN {schema}.domain_entity_roles dom ON dom.ent_id=ent.ent_id WHERE dom.dom_id=?;

#getAll
SELECT * FROM {schema}.entity ORDER BY 1 ASC;

#getEntitysEntitiesQuery
SELECT DISTINCT (ent.ent_id),  ent.ent_handle, ent.ent_port43 FROM {schema}.entity ent JOIN {schema}.entity_entity_roles rol ON rol.ent_id = ent.ent_id WHERE rol.main_ent_id = ?;

#getDomainsEntitiesQuery
SELECT DISTINCT (ent.ent_id),  ent.ent_handle, ent.ent_port43 FROM {schema}.entity ent JOIN {schema}.domain_entity_roles rol ON rol.ent_id = ent.ent_id WHERE rol.dom_id = ?;

#getNameserversEntitiesQuery
SELECT DISTINCT (ent.ent_id),  ent.ent_handle, ent.ent_port43 FROM {schema}.entity ent JOIN {schema}.nameserver_entity_roles rol ON rol.ent_id = ent.ent_id WHERE rol.nse_id = ?;

#getAutnumEntitiesQuery
SELECT DISTINCT (ent.ent_id),  ent.ent_handle, ent.ent_port43 FROM {schema}.entity ent JOIN {schema}.asn_entity_roles rol ON rol.ent_id = ent.ent_id WHERE rol.asn_id = ?;

#getIpNetworkEntitiesQuery
SELECT DISTINCT (ent.ent_id),  ent.ent_handle, ent.ent_port43 FROM {schema}.entity ent JOIN {schema}.ip_network_entity_roles rol ON rol.ent_id = ent.ent_id WHERE rol.ine_id = ?;

#getIdByHandle
SELECT ent_id FROM {schema}.entity ent WHERE ent.ent_handle = ?;

#searchByPartialHandle
SELECT * FROM {schema}.entity e WHERE e.ent_handle LIKE ? ORDER BY 1 LIMIT ?;

#searchByPartialName
SELECT DISTINCT (ent.ent_id),  ent.ent_handle, ent.ent_port43 FROM {schema}.entity ent JOIN {schema}.entity_contact eco ON eco.ent_id=ent.ent_id JOIN {schema}.vcard vca ON vca.vca_id=eco.vca_id WHERE vca.vca_name LIKE ? ORDER BY 1 LIMIT ?;

#getByName
SELECT * FROM {schema}.entity ent JOIN {schema}.entity_contact eco ON eco.ent_id=ent.ent_id JOIN {schema}.vcard vca ON vca.vca_id=eco.vca_id WHERE vca.vca_name = ?;

#searchByHandle
SELECT * FROM {schema}.entity e WHERE e.ent_handle = ? ORDER BY 1 LIMIT ?;

#searchByName
SELECT * FROM {schema}.entity ent JOIN {schema}.entity_contact eco ON eco.ent_id=ent.ent_id JOIN {schema}.vcard vca ON vca.vca_id=eco.vca_id WHERE vca.vca_name = ? ORDER BY 1 LIMIT ?;

#existByPartialName
SELECT EXISTS(SELECT 1 FROM {schema}.entity ent JOIN {schema}.entity_contact eco ON eco.ent_id=ent.ent_id JOIN {schema}.vcard vca ON vca.vca_id=eco.vca_id WHERE vca.vca_name LIKE ?);

#existByName
SELECT EXISTS(SELECT 1 FROM {schema}.entity ent JOIN {schema}.entity_contact eco ON eco.ent_id=ent.ent_id JOIN {schema}.vcard vca ON vca.vca_id=eco.vca_id WHERE vca.vca_name = ?);

#searchByRegexHandle
SELECT * FROM {schema}.entity e WHERE e.ent_handle REGEXP ? ORDER BY 1 LIMIT ?;

#searchByRegexName
SELECT DISTINCT (ent.ent_id),  ent.ent_handle, ent.ent_port43 FROM {schema}.entity ent JOIN {schema}.entity_contact eco ON eco.ent_id=ent.ent_id JOIN {schema}.vcard vca ON vca.vca_id=eco.vca_id WHERE vca.vca_name REGEXP ? ORDER BY 1 LIMIT ?;