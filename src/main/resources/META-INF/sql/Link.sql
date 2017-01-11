#storeToDatabase
INSERT INTO rdap.link VALUES(null,?,?,?,?,?,?,?);

#getAll
SELECT lin.* FROM rdap.link lin ORDER BY 1 ASC;

#getByNameServerId
SELECT lin.* FROM rdap.link lin JOIN rdap.nameserver_links nse ON nse.lin_id=lin.lin_id WHERE nse.nse_id=?;

#getByEventId
SELECT lin.* FROM rdap.link lin JOIN rdap.event_links eve ON eve.lin_id=lin.lin_id WHERE eve.eve_id=?;

#getByDsDataId
SELECT lin.* FROM rdap.link lin JOIN rdap.ds_links dsd ON dsd.lin_id=lin.lin_id WHERE dsd.dsd_id=?;

#getByDomainId
SELECT lin.* FROM rdap.link lin JOIN rdap.domain_links dom ON dom.lin_id=lin.lin_id WHERE dom.dom_id=?;

#getByEntityId
SELECT lin.* FROM rdap.link lin JOIN rdap.entity_links ent ON ent.lin_id=lin.lin_id WHERE ent.ent_id=?;

#getByAutnumId
SELECT lin.* FROM rdap.link lin JOIN rdap.asn_links asn ON asn.lin_id=lin.lin_id WHERE asn.asn_id = ?;

#getByIpNetworkId
SELECT lin.* FROM rdap.link lin JOIN rdap.ip_network_links ine ON ine.lin_id=lin.lin_id WHERE ine.ine_id=?;

#storeNameserverLinksToDatabase
INSERT INTO rdap.nameserver_links VALUES(?,?);

#storeEventLinksToDatabase
INSERT INTO rdap.event_links VALUES(?,?);

#storeRemarkLinksToDatabase
INSERT INTO rdap.remark_links VALUES(?,?);

#getByRemarkId
SELECT lin.* FROM rdap.link lin JOIN rdap.remark_links rem ON rem.lin_id=lin.lin_id WHERE rem.rem_id=?;

#storeDsDataLinksToDatabase
INSERT INTO rdap.ds_links VALUES(?,?);

#storeDomainLinksToDatabase
INSERT INTO rdap.domain_links VALUES (?,?);

#storeEntityLinksToDatabase
INSERT INTO rdap.entity_links VALUES(?,?);

#storeAutnumLinksToDatabase
INSERT INTO rdap.asn_links VALUES(?,?);

#storeIpNetworkLinksToDatabase
INSERT INTO rdap.ip_network_links VALUES (?, ?);

#deleteEntityLinksRelation
DELETE FROM rdap.entity_links WHERE lin_id IN (?);

#deleteNameserverLinksRelation
DELETE FROM rdap.nameserver_links WHERE lin_id IN (?);

#deleteDSLinksRelation
DELETE FROM rdap.ds_links WHERE lin_id IN (?);

#deleteDomainLinksRelation
DELETE FROM rdap.domain_links WHERE lin_id IN (?);

#deleteIpNetworkLinksRelation
DELETE FROM rdap.ip_network_links WHERE lin_id IN (?);

#deleteAutnumLinksRelation
DELETE FROM rdap.asn_links WHERE lin_id IN (?);

#deleteEventLinksRelation
DELETE FROM rdap.event_links WHERE lin_id IN (?);

#deleteRemarkLinksRelation
DELETE FROM rdap.remark_links WHERE lin_id IN (?);

#deleteLinksById
DELETE FROM rdap.link WHERE lin_id IN (?);
