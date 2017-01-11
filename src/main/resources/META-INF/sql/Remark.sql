#storeToDatabase
INSERT INTO rdap.remark VALUES (null,?,?,?);

#getByNameserverId
SELECT rem.* FROM rdap.remark rem JOIN rdap.nameserver_remarks nse ON nse.rem_id=rem.rem_id WHERE nse.nse_id=?;

#getByDomainId
SELECT rem.* FROM rdap.remark rem JOIN rdap.domain_remarks dom ON dom.rem_id=rem.rem_id WHERE dom.dom_id=?;

#getByEntityId
SELECT rem.* FROM rdap.remark rem JOIN rdap.entity_remarks ent ON ent.rem_id=rem.rem_id WHERE ent.ent_id=?;

#getByAutnumId
SELECT rem.* FROM rdap.remark rem JOIN rdap.asn_remarks asn ON asn.rem_id=rem.rem_id WHERE asn.asn_id=?;

#getByIpNetworkId
SELECT rem.* FROM rdap.remark rem JOIN rdap.ip_network_remarks ine ON ine.rem_id=rem.rem_id WHERE ine.ine_id=?;

#storeNameserverRemarksToDatabase
INSERT INTO rdap.nameserver_remarks VALUES(?,?);

#storeDomainRemarksToDatabase
INSERT INTO rdap.domain_remarks VALUES (?,?);

#storeEntityRemarksToDatabase
INSERT INTO rdap.entity_remarks VALUES (?, ?);

#storeAutnumRemarksToDatabase
INSERT INTO rdap.asn_remarks VALUES (?,?);

#storeIpNetworkRemarksToDatabase
INSERT INTO rdap.ip_network_remarks VALUES (?, ?);

#getAll
SELECT * FROM rdap.remark ORDER BY 1 ASC;

#deleteEntityRemarksRelation
DELETE FROM rdap.entity_remarks WHERE rem_id IN (?);

#deleteNameserverRemarksRelation
DELETE FROM rdap.nameserver_remarks WHERE rem_id IN (?);

#deleteDomainRemarksRelation
DELETE FROM rdap.domain_remarks WHERE rem_id IN (?);

#deleteAutnumRemarksRelation
DELETE FROM rdap.asn_remarks WHERE rem_id IN (?);

#deleteIpNetworkRemarksRelation
DELETE FROM rdap.ip_network_remarks WHERE rem_id IN (?);

#deleteRemarksById
DELETE FROM rdap.remark WHERE rem_id IN (?);