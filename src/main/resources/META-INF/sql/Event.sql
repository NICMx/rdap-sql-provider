#storeToDatabase
INSERT INTO rdap.event VALUES(null,?,?,?);

#getByNameServerId
SELECT eve.eve_id,eve.eac_id,eve.eve_actor,eve.eve_date FROM rdap.event eve JOIN rdap.nameserver_events nse ON nse.eve_id=eve.eve_id WHERE nse.nse_id=?;

#getByDsDataId
SELECT eve.eve_id,eve.eac_id,eve.eve_actor,eve.eve_date FROM rdap.event eve JOIN rdap.ds_events dse ON dse.eve_id=eve.eve_id WHERE dse.dsd_id=?;

#getByDomainId
SELECT eve.eve_id,eve.eac_id,eve.eve_actor,eve.eve_date FROM rdap.event eve JOIN rdap.domain_events dome ON dome.eve_id=eve.eve_id WHERE dome.dom_id=?;

#getByEntityId
SELECT eve.eve_id,eve.eac_id,eve.eve_actor,eve.eve_date FROM rdap.event eve JOIN rdap.entity_events ent ON ent.eve_id=eve.eve_id WHERE ent.ent_id=?;

#getByAutnumId
SELECT eve.eve_id, eve.eac_id, eve.eve_actor, eve.eve_date FROM rdap.event eve JOIN rdap.asn_events asn ON asn.eve_id=eve.eve_od WHERE asn.asn_id=?;

#getByIpNetworkId
SELECT eve.eve_id, eve.eac_id, eve.eve_actor, eve.eve_date FROM rdap.event eve JOIN rdap.ip_network_events ine ON ine.eve_id=eve.eve_id WHERE ine.ine_id=?;

#storeNameserverEventsToDatabase
INSERT INTO rdap.nameserver_events values (?,?);

#storeDomainEventsToDatabase
INSERT INTO rdap.domain_events VALUES (?,?);

#storeDsDataEventsToDatabase
INSERT INTO rdap.ds_events values (?,?);

#storeEntityEventsToDatabase
INSERT INTO rdap.entity_events values (?,?);

#storeAutnumEventsToDatabase
INSERT INTO rdap.asn_events VALUES (?,?);

#storeIpNetworkEventsToDatabase
INSERT INTO rdap.ip_network_events VALUES (?,?);

#getAll
SELECT * FROM rdap.event;

#deleteEventById
DELETE FROM rdap.event WHERE eve_id IN (?);

#deleteNameserverEventsRelation
DELETE FROM rdap.nameserver_events WHERE eve_id IN (?);

#deleteEntityEventsRelation
DELETE FROM rdap.entity_events WHERE eve_id IN (?);

#deleteDsEventsRelation
DELETE FROM rdap.ds_events WHERE eve_id IN (?);

#deleteDomainEventsRelation
DELETE FROM rdap.domain_events WHERE eve_id IN (?);

#deleteAutnumEventsRelation
DELETE FROM rdap.asn_events WHERE eve_id IN (?);

#deleteIpNetworkEventsRelation
DELETE FROM rdap.ip_network_events WHERE eve_id IN (?);
