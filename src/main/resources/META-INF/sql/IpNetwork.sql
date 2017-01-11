#getByIPv4
SELECT * FROM rdap.ip_network WHERE ip_version_id = 4 AND ine_cidr <= ? AND ine_start_address_down <= ? AND ine_end_address_down >= ? ORDER BY ine_cidr DESC;

#getByIPv6
SELECT * FROM rdap.ip_network WHERE ip_version_id = 6 AND ine_cidr <= ? AND ine_start_address_up <= ? AND ine_start_address_down <= ? AND ine_end_address_up >= ? AND ine_end_address_down >= ? ORDER BY ine_cidr DESC;

#storeToDatabase
INSERT INTO rdap.ip_network VALUES (null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);

#updateInDatabase
UPDATE rdap.ip_network SET ine_handle = ?, ine_start_address_up = ?, ine_start_address_down = ?, ine_end_address_up = ?, ine_end_address_down = ?, ine_name = ?, ine_type = ?, ine_port43 = ?, ccd_id = ?, ip_version = ?, ine_parent_handle = ?, ine_cidr = ? WHERE ine_id = ?;

#getByEntityId
SELECT ipn.* FROM rdap.ip_network ipn JOIN rdap.ip_network_entity_roles ent ON ent.ine_id = ipn.ine_id WHERE ent.ent_id = ?;

#getByDomainId
SELECT ipn.* FROM rdap.ip_network ipn JOIN rdap.domain_networks dom ON dom.ine_id = ipn.ine_id WHERE dom.dom_id = ?;

#getByHandle
SELECT * FROM rdap.ip_network ipn WHERE ipn.ine_handle = ?;


#existByIPv4
SELECT EXISTS(SELECT 1 FROM rdap.ip_network WHERE ip_version_id = 4 AND ine_cidr <= ? AND ine_start_address_down <= ? AND ine_end_address_down >= ?);

#existByIPv6
SELECT EXISTS(SELECT 1  FROM rdap.ip_network WHERE ip_version_id = 6 AND ine_cidr <= ? AND ine_start_address_up <= ? AND ine_start_address_down <= ? AND ine_end_address_up >= ? AND ine_end_address_down >= ? );
