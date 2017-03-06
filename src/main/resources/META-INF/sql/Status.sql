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

#storeNameserverStatusToDatabase
INSERT INTO {schema}.nameserver_status VALUES (?,?);

#storeDomainStatusToDatabase
INSERT INTO {schema}.domain_status VALUES (?,?);

#storeEntityStatusToDatabase
INSERT INTO {schema}.entity_status VALUES (?,?);

#storeRegistrarStatusToDatabase
INSERT INTO {schema}.registrar_status VALUES (?,?);

#storeAutnumStatusToDatabase
INSERT INTO {schema}.asn_status VALUES (?,?);

#storeIpNetworkStatusToDatabase
INSERT INTO {schema}.ip_network_status VALUES (?, ?);


#deleteEntityStatusRelation
DELETE FROM {schema}.entity_status  WHERE ent_id=?;

#deleteNameserverStatusRelation
DELETE FROM {schema}.nameserver_status  WHERE nse_id=?;

#deleteDomainStatusRelation
DELETE FROM {schema}.domain_status  WHERE dom_id=?;

#deleteAutnumStatusRelation
DELETE FROM {schema}.asn_status  WHERE asn_id=?;

#deleteIpNetworkStatusRelation
DELETE FROM {schema}.ip_network_status  WHERE ine_id=?;
