#getByIPv4
SELECT ine_id, ine_handle, ine_parent_handle, ip_version_id, ine_start_address_down, ine_end_address_down, ine_start_address_up, ine_end_address_up, ine_name, ine_type, ccd_id, ine_port43, ine_cidr FROM {schema}.ip_network WHERE ip_version_id = 4 AND ine_cidr <= ? AND ine_start_address_down <= ? AND ine_end_address_down >= ? ORDER BY ine_cidr DESC;

#getByIPv6
SELECT ine_id, ine_handle, ine_parent_handle, ip_version_id, ine_start_address_down, ine_end_address_down, ine_start_address_up, ine_end_address_up, ine_name, ine_type, ccd_id, ine_port43, ine_cidr FROM {schema}.ip_network WHERE ip_version_id = 6 AND ine_cidr <= ? AND ine_start_address_up <= ? AND ine_start_address_down <= ? AND ine_end_address_up >= ? AND ine_end_address_down >= ? ORDER BY ine_cidr DESC;

#getByEntityId
SELECT ipn.ine_id, ipn.ine_handle, ipn.ine_parent_handle, ipn.ip_version_id, ipn.ine_start_address_down, ipn.ine_end_address_down, ipn.ine_start_address_up, ipn.ine_end_address_up, ipn.ine_name, ipn.ine_type, ipn.ccd_id, ipn.ine_port43, ipn.ine_cidr FROM {schema}.ip_network ipn JOIN {schema}.ip_network_entity_roles ent ON ent.ine_id = ipn.ine_id WHERE ent.ent_id = ?;

#getByDomainId
SELECT ipn.ine_id, ipn.ine_handle, ipn.ine_parent_handle, ipn.ip_version_id, ipn.ine_start_address_down, ipn.ine_end_address_down, ipn.ine_start_address_up, ipn.ine_end_address_up, ipn.ine_name, ipn.ine_type, ipn.ccd_id, ipn.ine_port43, ipn.ine_cidr FROM {schema}.ip_network ipn JOIN {schema}.domain_networks dom ON dom.ine_id = ipn.ine_id WHERE dom.dom_id = ?;

#existByIPv4
SELECT EXISTS(SELECT 1 FROM {schema}.ip_network WHERE ip_version_id = 4 AND ine_cidr <= ? AND ine_start_address_down <= ? AND ine_end_address_down >= ?);

#existByIPv6
SELECT EXISTS(SELECT 1  FROM {schema}.ip_network WHERE ip_version_id = 6 AND ine_cidr <= ? AND ine_start_address_up <= ? AND ine_start_address_down <= ? AND ine_end_address_up >= ? AND ine_end_address_down >= ? );
