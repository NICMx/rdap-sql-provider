#storeDomainsEntityRol
INSERT INTO rdap.domain_entity_roles VALUES (?, ?, ?);

#storeEntitiesEntityRol
INSERT INTO rdap.entity_entity_roles VALUES (?, ?, ?);

#storeNSEntityRol
INSERT INTO rdap.nameserver_entity_roles VALUES (?, ?, ?);

#storeAutnumEntityRol
INSERT INTO rdap.asn_entity_roles VALUES (?,?,?);

#storeIpNetworkEntityRol
INSERT INTO rdap.ip_network_entity_roles VALUES (?, ?, ?);

#getDomainRol
SELECT rol.rol_id FROM rdap.domain_entity_roles rol WHERE rol.dom_id = ? AND rol.ent_id = ?;

#getEntityRol
SELECT rol.rol_id FROM rdap.entity_entity_roles rol WHERE rol.main_ent_id = ? AND rol.ent_id = ?;

#getNSRol
SELECT rol.rol_id FROM rdap.nameserver_entity_roles rol WHERE rol.nse_id = ? AND rol.ent_id = ?;

#getAutnumRol
SELECT rol.rol_id FROM rdap.asn_entity_roles rol WHERE rol.asn_id = ? AND rol.ent_id = ?;

#getIpNetworkRol
SELECT rol.rol_id FROM rdap.ip_network_entity_roles rol WHERE rol.ine_id = ? AND rol.ent_id = ?;

#getMainEntityRol
SELECT DISTINCT rol.rol_id FROM rdap.entity_entity_roles rol WHERE rol.ent_id = ? AND rol.main_ent_id IN (?);

#deleteEntityEntityRoles
DELETE FROM rdap.entity_entity_roles  WHERE ent_id=?;

#deleteNameserverEntityRoles
DELETE FROM rdap.nameserver_entity_roles  WHERE nse_id=?;

#deleteDomainEntityRoles
DELETE FROM rdap.domain_entity_roles  WHERE dom_id=?;

#deleteAutnumEntityRoles
DELETE FROM rdap.asn_entity_roles  WHERE asn_id=?;

#deleteIpNetworkEntityRoles
DELETE FROM rdap.ip_network_entity_roles  WHERE ine_id=?;

#deleteMainEntityRelation
DELETE FROM rdap.entity_entity_roles  WHERE main_ent_id=?;