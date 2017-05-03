#getByNameServerId
SELECT sta_id FROM {schema}.nameserver_status WHERE nse_id=?;

#getByDomainId
SELECT sta_id FROM {schema}.domain_status WHERE dom_id=?;

#getByEntityId
SELECT sta_id FROM {schema}.entity_status WHERE ent_id=?;

#getByRegistrarId
SELECT sta_id FROM {schema}.registrar_status WHERE rar_id=?;

#getByAutnumid
SELECT sta_id FROM {schema}.asn_status WHERE asn_id=?;

#getByIpNetworkId
SELECT sta_id FROM {schema}.ip_network_status WHERE ine_id=?;

