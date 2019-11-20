#getByHandle
SELECT ent_id, ent_handle, ent_port43 FROM {schema}.entity e WHERE e.ent_handle = ?;

#getByDomain
SELECT ent.ent_id, ent.ent_handle, ent.ent_port43, dom.rol_id FROM {schema}.entity ent JOIN {schema}.domain_entity_roles dom ON dom.ent_id=ent.ent_id WHERE dom.dom_id=?;

#getAll
SELECT ent_id, ent_handle, ent_port43 FROM {schema}.entity ORDER BY 1 ASC;

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

#searchByPartialHandle
SELECT ent_id, ent_handle, ent_port43 FROM {schema}.entity e WHERE e.ent_handle LIKE ? ORDER BY 1 LIMIT ?;

#searchByPartialName
SELECT DISTINCT (ent.ent_id),  ent.ent_handle, ent.ent_port43 FROM {schema}.entity ent JOIN {schema}.entity_contact eco ON eco.ent_id=ent.ent_id JOIN {schema}.vcard vca ON vca.vca_id=eco.vca_id WHERE vca.vca_name LIKE ? ORDER BY 1 LIMIT ?;

#getByName
SELECT ent.ent_id, ent.ent_handle, ent.ent_port43 FROM {schema}.entity ent JOIN {schema}.entity_contact eco ON eco.ent_id=ent.ent_id JOIN {schema}.vcard vca ON vca.vca_id=eco.vca_id WHERE vca.vca_name = ?;

#searchByHandle
SELECT e.ent_id, e.ent_handle, e.ent_port43 FROM {schema}.entity e WHERE e.ent_handle = ? ORDER BY 1 LIMIT ?;

#searchByName
SELECT ent.ent_id, ent.ent_handle, ent.ent_port43 FROM {schema}.entity ent JOIN {schema}.entity_contact eco ON eco.ent_id=ent.ent_id JOIN {schema}.vcard vca ON vca.vca_id=eco.vca_id WHERE vca.vca_name = ? ORDER BY 1 LIMIT ?;

#existByPartialName
SELECT EXISTS(SELECT 1 FROM {schema}.entity ent JOIN {schema}.entity_contact eco ON eco.ent_id=ent.ent_id JOIN {schema}.vcard vca ON vca.vca_id=eco.vca_id WHERE vca.vca_name LIKE ?);

#existByName
SELECT EXISTS(SELECT 1 FROM {schema}.entity ent JOIN {schema}.entity_contact eco ON eco.ent_id=ent.ent_id JOIN {schema}.vcard vca ON vca.vca_id=eco.vca_id WHERE vca.vca_name = ?);

#searchByRegexHandle
SELECT ent_id, ent_handle, ent_port43 FROM {schema}.entity e WHERE e.ent_handle REGEXP ? ORDER BY 1 LIMIT ?;

#searchByRegexName
SELECT DISTINCT (ent.ent_id),  ent.ent_handle, ent.ent_port43 FROM {schema}.entity ent JOIN {schema}.entity_contact eco ON eco.ent_id=ent.ent_id JOIN {schema}.vcard vca ON vca.vca_id=eco.vca_id WHERE vca.vca_name REGEXP ? ORDER BY 1 LIMIT ?;

#userGlobalConsent
SELECT ugc_consent FROM {schema}.user_global_consent ugc WHERE ugc.ent_id = ?;

#userConsentByAttribute
SELECT * FROM {schema}.user_consent_by_attributes uca WHERE uca.ent_id = ?;