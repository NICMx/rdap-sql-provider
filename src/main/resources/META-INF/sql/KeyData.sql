#getBySecureDns
SELECT kd_id, kd_flags, kd_protocol, kd_public_key, kd_algorithm, sdns_id FROM {schema}.key_data kd WHERE kd.sdns_id = ?;

