#getByNameserverId
SELECT rem.* FROM {schema}.remark rem JOIN {schema}.nameserver_remarks nse ON nse.rem_id=rem.rem_id WHERE nse.nse_id=?;

#getByDomainId
SELECT rem.* FROM {schema}.remark rem JOIN {schema}.domain_remarks dom ON dom.rem_id=rem.rem_id WHERE dom.dom_id=?;

#getByEntityId
SELECT rem.* FROM {schema}.remark rem JOIN {schema}.entity_remarks ent ON ent.rem_id=rem.rem_id WHERE ent.ent_id=?;

#getByAutnumId
SELECT rem.* FROM {schema}.remark rem JOIN {schema}.asn_remarks asn ON asn.rem_id=rem.rem_id WHERE asn.asn_id=?;

#getByIpNetworkId
SELECT rem.* FROM {schema}.remark rem JOIN {schema}.ip_network_remarks ine ON ine.rem_id=rem.rem_id WHERE ine.ine_id=?;

#getAll
SELECT * FROM {schema}.remark ORDER BY 1 ASC;

