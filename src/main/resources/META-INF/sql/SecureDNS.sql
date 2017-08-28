#getByDomain
SELECT sdns_id, sdns_zone_signed, sdns_delegation_signed, sdns_max_sig_life, dom_id FROM {schema}.secure_dns s WHERE s.dom_id = ?;

