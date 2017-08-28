#getByRange
SELECT asn_id, asn_handle, asn_start_autnum, asn_end_autnum, asn_name, asn_type, asn_port43, ccd_id FROM {schema}.autonomous_system_number asn WHERE asn.asn_start_autnum <= ? AND asn.asn_end_autnum >= ?;

#getAutnumByEntity
SELECT asn.asn_id, asn.asn_handle, asn.asn_start_autnum, asn.asn_end_autnum, asn.asn_name, asn.asn_type, asn.asn_port43, asn.ccd_id FROM {schema}.autonomous_system_number asn JOIN {schema}.asn_entity_roles ent ON ent.asn_id = asn.asn_id WHERE ent.ent_id = ?;
