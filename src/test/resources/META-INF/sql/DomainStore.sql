#storeToDatabase
INSERT INTO {schema}.domain(dom_handle,dom_unicode_name,dom_port43,zone_id) VALUES (?,?,?,?);

#storeDomainIpNetworkRelation
INSERT INTO {schema}.domain_networks VALUES (?, ?);

