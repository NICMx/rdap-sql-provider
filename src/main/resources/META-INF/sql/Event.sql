#getByNameServerId
SELECT eve.eve_id,eve.eac_id,eve.eve_actor,eve.eve_date FROM {schema}.event eve JOIN {schema}.nameserver_events nse ON nse.eve_id=eve.eve_id WHERE nse.nse_id=?;

#getByDsDataId
SELECT eve.eve_id,eve.eac_id,eve.eve_actor,eve.eve_date FROM {schema}.event eve JOIN {schema}.ds_events dse ON dse.eve_id=eve.eve_id WHERE dse.dsd_id=?;

#getByDomainId
SELECT eve.eve_id,eve.eac_id,eve.eve_actor,eve.eve_date FROM {schema}.event eve JOIN {schema}.domain_events dome ON dome.eve_id=eve.eve_id WHERE dome.dom_id=?;

#getByEntityId
SELECT eve.eve_id,eve.eac_id,eve.eve_actor,eve.eve_date FROM {schema}.event eve JOIN {schema}.entity_events ent ON ent.eve_id=eve.eve_id WHERE ent.ent_id=?;

#getByAutnumId
SELECT eve.eve_id, eve.eac_id, eve.eve_actor, eve.eve_date FROM {schema}.event eve JOIN {schema}.asn_events asn ON asn.eve_id=eve.eve_id WHERE asn.asn_id=?;

#getByIpNetworkId
SELECT eve.eve_id, eve.eac_id, eve.eve_actor, eve.eve_date FROM {schema}.event eve JOIN {schema}.ip_network_events ine ON ine.eve_id=eve.eve_id WHERE ine.ine_id=?;

#getByKeyDataId
SELECT eve.eve_id,eve.eac_id,eve.eve_actor,eve.eve_date FROM {schema}.event eve JOIN {schema}.key_events kde ON kde.eve_id=eve.eve_id WHERE kde.kd_id=?;

#getAll
SELECT eve_id, eac_id, eve_actor, eve_date FROM {schema}.event;

