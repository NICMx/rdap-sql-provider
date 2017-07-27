#getMainEntityRole
SELECT rol.rol_id FROM {schema}.entity_role rol WHERE rol.ent_id = ?;

#getDomainRole
SELECT rol.rol_id FROM {schema}.domain_entity_roles rol WHERE rol.dom_id = ? AND rol.ent_id = ?;

#getEntityRole
SELECT rol.rol_id FROM {schema}.entity_entity_roles rol WHERE rol.main_ent_id = ? AND rol.ent_id = ?;

#getNSRole
SELECT rol.rol_id FROM {schema}.nameserver_entity_roles rol WHERE rol.nse_id = ? AND rol.ent_id = ?;

#getAutnumRole
SELECT rol.rol_id FROM {schema}.asn_entity_roles rol WHERE rol.asn_id = ? AND rol.ent_id = ?;

#getIpNetworkRole
SELECT rol.rol_id FROM {schema}.ip_network_entity_roles rol WHERE rol.ine_id = ? AND rol.ent_id = ?;

