#getAll
SELECT lin.* FROM {schema}.link lin ORDER BY 1 ASC;

#getByNameServerId
SELECT lin.* FROM {schema}.link lin JOIN {schema}.nameserver_links nse ON nse.lin_id=lin.lin_id WHERE nse.nse_id=?;

#getByEventId
SELECT lin.* FROM {schema}.link lin JOIN {schema}.event_links eve ON eve.lin_id=lin.lin_id WHERE eve.eve_id=?;

#getByDsDataId
SELECT lin.* FROM {schema}.link lin JOIN {schema}.ds_links dsd ON dsd.lin_id=lin.lin_id WHERE dsd.dsd_id=?;

#getByDomainId
SELECT lin.* FROM {schema}.link lin JOIN {schema}.domain_links dom ON dom.lin_id=lin.lin_id WHERE dom.dom_id=?;

#getByEntityId
SELECT lin.* FROM {schema}.link lin JOIN {schema}.entity_links ent ON ent.lin_id=lin.lin_id WHERE ent.ent_id=?;

#getByAutnumId
SELECT lin.* FROM {schema}.link lin JOIN {schema}.asn_links asn ON asn.lin_id=lin.lin_id WHERE asn.asn_id = ?;

#getByIpNetworkId
SELECT lin.* FROM {schema}.link lin JOIN {schema}.ip_network_links ine ON ine.lin_id=lin.lin_id WHERE ine.ine_id=?;

#getByKeyDataId
SELECT lin.* FROM {schema}.link lin JOIN {schema}.key_links kdl ON kdl.lin_id=lin.lin_id WHERE kdl.kd_id=?;

#getByRemarkId
SELECT lin.* FROM {schema}.link lin JOIN {schema}.remark_links rem ON rem.lin_id=lin.lin_id WHERE rem.rem_id=?;

#getLinkHreflangs
SELECT lan_hreflang FROM {schema}.link_lang lan WHERE lan.lin_id = ?;

