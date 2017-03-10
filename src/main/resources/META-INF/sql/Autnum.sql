#storeToDatabase
INSERT INTO {schema}.autonomous_system_number VALUES (null,?,?,?,?,?,?,?);

#updateInDatabase
UPDATE {schema}.autonomous_system_number SET asn_start_autnum=?, asn_end_autnum=?,asn_name=?, asn_type=?, asn_port43=?, ccd_id=?  WHERE asn_id=?;

#getAutnumById
SELECT * FROM {schema}.autonomous_system_number asn WHERE asn.asn_id = ?; 

#getByRange
SELECT * FROM {schema}.autonomous_system_number asn WHERE asn.asn_start_autnum <= ? AND asn.asn_end_autnum >= ?;

#getAutnumByHandle
SELECT * FROM {schema}.autonomous_system_number asn WHERE asn.asn_handle = ?;

#getAutnumByEntity
SELECT asn.* FROM {schema}.autonomous_system_number asn JOIN {schema}.asn_entity_roles ent ON ent.asn_id = asn.asn_id WHERE ent.ent_id = ?;
