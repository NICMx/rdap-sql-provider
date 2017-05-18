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
