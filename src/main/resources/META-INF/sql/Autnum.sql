#getByRange
SELECT * FROM {schema}.autonomous_system_number asn WHERE asn.asn_start_autnum <= ? AND asn.asn_end_autnum >= ?;

#getAutnumByEntity
SELECT asn.* FROM {schema}.autonomous_system_number asn JOIN {schema}.asn_entity_roles ent ON ent.asn_id = asn.asn_id WHERE ent.ent_id = ?;
