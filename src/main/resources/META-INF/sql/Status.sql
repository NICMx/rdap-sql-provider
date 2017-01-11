#getByNameServerId
SELECT sta_id FROM rdap.nameserver_status WHERE nse_id=?;


#getByDomainId
SELECT sta_id FROM rdap.domain_status WHERE dom_id=?;

#getByEntityId
SELECT sta_id FROM rdap.entity_status WHERE ent_id=?;

#getByRegistrarId
SELECT sta_id FROM rdap.registrar_status WHERE rar_id=?;

#getByAutnumid
SELECT sta_id FROM rdap.asn_status WHERE asn_id=?;

#getByIpNetworkId
SELECT sta_id FROM rdap.ip_network_status WHERE ine_id=?;

#storeNameserverStatusToDatabase
INSERT INTO rdap.nameserver_status VALUES (?,?);

#storeDomainStatusToDatabase
INSERT INTO rdap.domain_status VALUES (?,?);

#storeEntityStatusToDatabase
INSERT INTO rdap.entity_status VALUES (?,?);

#storeRegistrarStatusToDatabase
INSERT INTO rdap.registrar_status VALUES (?,?);

#storeAutnumStatusToDatabase
INSERT INTO rdap.asn_status VALUES (?,?);

#storeIpNetworkStatusToDatabase
INSERT INTO rdap.ip_network_status VALUES (?, ?);


#deleteEntityStatusRelation
DELETE FROM rdap.entity_status  WHERE ent_id=?;

#deleteNameserverStatusRelation
DELETE FROM rdap.nameserver_status  WHERE nse_id=?;

#deleteDomainStatusRelation
DELETE FROM rdap.domain_status  WHERE dom_id=?;

#deleteAutnumStatusRelation
DELETE FROM rdap.asn_status  WHERE asn_id=?;

#deleteIpNetworkStatusRelation
DELETE FROM rdap.ip_network_status  WHERE ine_id=?;
