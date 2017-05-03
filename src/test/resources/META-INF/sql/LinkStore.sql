#storeToDatabase
INSERT INTO {schema}.link VALUES(null,?,?,?,?,?,?);

#storeNameserverLinksToDatabase
INSERT INTO {schema}.nameserver_links VALUES(?,?);

#storeEventLinksToDatabase
INSERT INTO {schema}.event_links VALUES(?,?);

#storeRemarkLinksToDatabase
INSERT INTO {schema}.remark_links VALUES(?,?);

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

#storeKeyDataLinksToDatabase
INSERT INTO {schema}.key_links VALUES(?,?);

#storeLinkHreflangs
INSERT INTO {schema}.link_lang VALUES (?,?);
