
#storeToDatabase
INSERT INTO {schema}.ip_network VALUES (null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);

#getByHandle
SELECT ine_id, ine_handle, ine_parent_handle, ip_version_id, ine_start_address_down, ine_end_address_down, ine_start_address_up, ine_end_address_up, ine_name, ine_type, ccd_id, ine_port43, ine_cidr FROM {schema}.ip_network ipn WHERE ipn.ine_handle = ?;