#storeToDatabase
INSERT INTO {schema}.event VALUES(null,?,?,?);

#getByNameServerId
SELECT eve.eve_id,eve.eac_id,eve.eve_actor,eve.eve_date FROM {schema}.event eve JOIN {schema}.nameserver_events nse ON nse.eve_id=eve.eve_id WHERE nse.nse_id=?;

#getByDsDataId
SELECT eve.eve_id,eve.eac_id,eve.eve_actor,eve.eve_date FROM {schema}.event eve JOIN {schema}.ds_events dse ON dse.eve_id=eve.eve_id WHERE dse.dsd_id=?;

#getByDomainId
SELECT eve.eve_id,eve.eac_id,eve.eve_actor,eve.eve_date FROM {schema}.event eve JOIN {schema}.domain_events dome ON dome.eve_id=eve.eve_id WHERE dome.dom_id=?;

#getByEntityId
SELECT eve.eve_id,eve.eac_id,eve.eve_actor,eve.eve_date FROM {schema}.event eve JOIN {schema}.entity_events ent ON ent.eve_id=eve.eve_id WHERE ent.ent_id=?;

#getByAutnumId
SELECT eve.eve_id, eve.eac_id, eve.eve_actor, eve.eve_date FROM {schema}.event eve JOIN {schema}.asn_events asn ON asn.eve_id=eve.eve_od WHERE asn.asn_id=?;

#getByIpNetworkId
SELECT eve.eve_id, eve.eac_id, eve.eve_actor, eve.eve_date FROM {schema}.event eve JOIN {schema}.ip_network_events ine ON ine.eve_id=eve.eve_id WHERE ine.ine_id=?;

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

#getAll
SELECT * FROM {schema}.event;

#deleteEventById
DELETE FROM {schema}.event WHERE eve_id IN (?);

#deleteNameserverEventsRelation
DELETE FROM {schema}.nameserver_events WHERE eve_id IN (?);

#deleteEntityEventsRelation
DELETE FROM {schema}.entity_events WHERE eve_id IN (?);

#deleteDsEventsRelation
DELETE FROM {schema}.ds_events WHERE eve_id IN (?);

#deleteDomainEventsRelation
DELETE FROM {schema}.domain_events WHERE eve_id IN (?);

#deleteAutnumEventsRelation
DELETE FROM {schema}.asn_events WHERE eve_id IN (?);

#deleteIpNetworkEventsRelation
DELETE FROM {schema}.ip_network_events WHERE eve_id IN (?);
