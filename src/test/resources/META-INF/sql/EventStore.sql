#storeToDatabase
INSERT INTO {schema}.event VALUES(null,?,?,?);

#storeNameserverEventsToDatabase
INSERT INTO {schema}.nameserver_events values (?,?);

#storeDomainEventsToDatabase
INSERT INTO {schema}.domain_events VALUES (?,?);

#storeDsDataEventsToDatabase
INSERT INTO {schema}.ds_events values (?,?);

#storeEntityEventsToDatabase
INSERT INTO {schema}.entity_events values (?,?);

#storeAutnumEventsToDatabase
INSERT INTO {schema}.asn_events VALUES (?,?);

#storeIpNetworkEventsToDatabase
INSERT INTO {schema}.ip_network_events VALUES (?,?);

#storeKeyDataEventsToDatabase
INSERT INTO {schema}.key_events VALUES (?,?);

