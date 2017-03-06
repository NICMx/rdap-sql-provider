#storeToDatabase
INSERT INTO {schema}.remark VALUES (null,?,?,?);

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

#storeNameserverRemarksToDatabase
INSERT INTO {schema}.nameserver_remarks VALUES(?,?);

#storeDomainRemarksToDatabase
INSERT INTO {schema}.domain_remarks VALUES (?,?);

#storeEntityRemarksToDatabase
INSERT INTO {schema}.entity_remarks VALUES (?, ?);

#storeAutnumRemarksToDatabase
INSERT INTO {schema}.asn_remarks VALUES (?,?);

#storeIpNetworkRemarksToDatabase
INSERT INTO {schema}.ip_network_remarks VALUES (?, ?);

#getAll
SELECT * FROM {schema}.remark ORDER BY 1 ASC;

#deleteEntityRemarksRelation
DELETE FROM {schema}.entity_remarks WHERE rem_id IN (?);

#deleteNameserverRemarksRelation
DELETE FROM {schema}.nameserver_remarks WHERE rem_id IN (?);

#deleteDomainRemarksRelation
DELETE FROM {schema}.domain_remarks WHERE rem_id IN (?);

#deleteAutnumRemarksRelation
DELETE FROM {schema}.asn_remarks WHERE rem_id IN (?);

#deleteIpNetworkRemarksRelation
DELETE FROM {schema}.ip_network_remarks WHERE rem_id IN (?);

#deleteRemarksById
DELETE FROM {schema}.remark WHERE rem_id IN (?);