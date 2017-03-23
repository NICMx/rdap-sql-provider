#storeToDatabase
INSERT INTO {schema}.variant VALUES (null,?,?);

#getByDomainId
SELECT * FROM {schema}.variant v WHERE v.dom_id=?;

#getAll
SELECT * FROM {schema}.variant;

#storeVariantRelation
INSERT INTO {schema}.variant_relation VALUES (?,?);

#getVariantRelationsByVariantId
SELECT rel_id FROM {schema}.variant_relation vr WHERE vr.var_id=?;

#storeVariantNames
INSERT INTO {schema}.variant_name VALUES (?,?);

#getVariantNamesByVariantId
SELECT vna_ldh_name FROM {schema}.variant_name vn WHERE vn.var_id=?;

#deleteFromDatabase
DELETE FROM {schema}.variant WHERE dom_id=?;

#deleteVariantRelation
DELETE FROM {schema}.variant_relation WHERE var_id IN (?);

#deleteVariantNames
DELETE FROM {schema}.variant_name WHERE var_id IN (?);
