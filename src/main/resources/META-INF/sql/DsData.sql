#getBySecureDns
SELECT dsd_id, dsd_keytag, dsd_algorithm, dsd_digest, dsd_digest_type, sdns_id FROM {schema}.ds_data ds WHERE ds.sdns_id = ?;

