#storeToDatabase
INSERT INTO {schema}.link VALUES(null,?,?,?,?,?,?,?);

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

#storeNameserverLinksToDatabase
INSERT INTO {schema}.nameserver_links VALUES(?,?);

#storeEventLinksToDatabase
INSERT INTO {schema}.event_links VALUES(?,?);

#storeRemarkLinksToDatabase
INSERT INTO {schema}.remark_links VALUES(?,?);

#getByRemarkId
SELECT lin.* FROM {schema}.link lin JOIN {schema}.remark_links rem ON rem.lin_id=lin.lin_id WHERE rem.rem_id=?;

#storeDsDataLinksToDatabase
INSERT INTO {schema}.ds_links VALUES(?,?);

#storeDomainLinksToDatabase
INSERT INTO {schema}.domain_links VALUES (?,?);

#storeEntityLinksToDatabase
INSERT INTO {schema}.entity_links VALUES(?,?);

#storeAutnumLinksToDatabase
INSERT INTO {schema}.asn_links VALUES(?,?);

#storeIpNetworkLinksToDatabase
INSERT INTO {schema}.ip_network_links VALUES (?, ?);

#deleteEntityLinksRelation
DELETE FROM {schema}.entity_links WHERE lin_id IN (?);

#deleteNameserverLinksRelation
DELETE FROM {schema}.nameserver_links WHERE lin_id IN (?);

#deleteDSLinksRelation
DELETE FROM {schema}.ds_links WHERE lin_id IN (?);

#deleteDomainLinksRelation
DELETE FROM {schema}.domain_links WHERE lin_id IN (?);

#deleteIpNetworkLinksRelation
DELETE FROM {schema}.ip_network_links WHERE lin_id IN (?);

#deleteAutnumLinksRelation
DELETE FROM {schema}.asn_links WHERE lin_id IN (?);

#deleteEventLinksRelation
DELETE FROM {schema}.event_links WHERE lin_id IN (?);

#deleteRemarkLinksRelation
DELETE FROM {schema}.remark_links WHERE lin_id IN (?);

#deleteLinksById
DELETE FROM {schema}.link WHERE lin_id IN (?);
