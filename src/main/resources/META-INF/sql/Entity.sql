#storeToDatabase
INSERT INTO rdap.entity VALUES (null, ?, ?);

#getById
SELECT * FROM rdap.entity e WHERE e.ent_id = ?;

#getByHandle
SELECT * FROM rdap.entity e WHERE e.ent_handle = ?;

#getByDomain
SELECT ent.*, dom.rol_id FROM rdap.entity ent JOIN rdap.domain_entity_roles dom ON dom.ent_id=ent.ent_id WHERE dom.dom_id=?;

#getAll
SELECT * FROM rdap.entity ORDER BY 1 ASC;

#getEntitysEntitiesQuery
SELECT DISTINCT (ent.ent_id),  ent.ent_handle, ent.ent_port43 FROM rdap.entity ent JOIN rdap.entity_entity_roles rol ON rol.ent_id = ent.ent_id WHERE rol.main_ent_id = ?;

#getDomainsEntitiesQuery
SELECT DISTINCT (ent.ent_id),  ent.ent_handle, ent.ent_port43 FROM rdap.entity ent JOIN rdap.domain_entity_roles rol ON rol.ent_id = ent.ent_id WHERE rol.dom_id = ?;

#getNameserversEntitiesQuery
SELECT DISTINCT (ent.ent_id),  ent.ent_handle, ent.ent_port43 FROM rdap.entity ent JOIN rdap.nameserver_entity_roles rol ON rol.ent_id = ent.ent_id WHERE rol.nse_id = ?;

#getAutnumEntitiesQuery
SELECT DISTINCT (ent.ent_id),  ent.ent_handle, ent.ent_port43 FROM rdap.entity ent JOIN rdap.asn_entity_roles rol ON rol.ent_id WHERE rol.asn_id = ?;

#getIpNetworkEntitiesQuery
SELECT DISTINCT (ent.ent_id),  ent.ent_handle, ent.ent_port43 FROM rdap.entity ent JOIN rdap.ip_network_entity_roles rol ON rol.ent_id = ent.ent_id WHERE rol.ine_id = ?;

#getIdByHandle
SELECT ent_id FROM rdap.entity ent WHERE ent.ent_handle = ?;

#searchByPartialHandle
SELECT * FROM rdap.entity e WHERE e.ent_handle LIKE ? ORDER BY 1 LIMIT ?;

#searchByPartialName
SELECT DISTINCT (ent.ent_id),  ent.ent_handle, ent.ent_port43 FROM rdap.entity ent JOIN rdap.entity_contact eco ON eco.ent_id=ent.ent_id JOIN rdap.vcard vca ON vca.vca_id=eco.vca_id WHERE vca.vca_name LIKE ? ORDER BY 1 LIMIT ?;

#getByName
SELECT * FROM rdap.entity ent JOIN rdap.entity_contact eco ON eco.ent_id=ent.ent_id JOIN rdap.vcard vca ON vca.vca_id=eco.vca_id WHERE vca.vca_name = ?;

#searchByHandle
SELECT * FROM rdap.entity e WHERE e.ent_handle = ? ORDER BY 1 LIMIT ?;

#searchByName
SELECT * FROM rdap.entity ent JOIN rdap.entity_contact eco ON eco.ent_id=ent.ent_id JOIN rdap.vcard vca ON vca.vca_id=eco.vca_id WHERE vca.vca_name = ? ORDER BY 1 LIMIT ?;

#updateInDatabase
UPDATE rdap.entity SET ent_port43=? WHERE ent_id=?;

#existByHandle
SELECT EXISTS(SELECT 1 FROM rdap.entity e WHERE e.ent_handle = ?);

#existByPartialName
SELECT EXISTS(SELECT 1 FROM rdap.entity ent JOIN rdap.entity_contact eco ON eco.ent_id=ent.ent_id JOIN rdap.vcard vca ON vca.vca_id=eco.vca_id WHERE vca.vca_name LIKE ?);

#existByName
SELECT EXISTS(SELECT 1 FROM rdap.entity ent JOIN rdap.entity_contact eco ON eco.ent_id=ent.ent_id JOIN rdap.vcard vca ON vca.vca_id=eco.vca_id WHERE vca.vca_name = ?);

#existByPartialHandle
SELECT EXISTS(SELECT 1  FROM rdap.entity e WHERE e.ent_handle LIKE ?);

#searchByRegexHandle
SELECT * FROM rdap.entity e WHERE e.ent_handle REGEXP ? ORDER BY 1 LIMIT ?;

#searchByRegexName
SELECT DISTINCT (ent.ent_id),  ent.ent_handle, ent.ent_port43 FROM rdap.entity ent JOIN rdap.entity_contact eco ON eco.ent_id=ent.ent_id JOIN rdap.vcard vca ON vca.vca_id=eco.vca_id WHERE vca.vca_name REGEXP ? ORDER BY 1 LIMIT ?;