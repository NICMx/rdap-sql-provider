#getByDomainId
SELECT * FROM {schema}.variant v WHERE v.dom_id=?;

#getAll
SELECT * FROM {schema}.variant;

#getVariantRelationsByVariantId
SELECT rel_id FROM {schema}.variant_relation vr WHERE vr.var_id=?;

#getVariantNamesByVariantId
SELECT vna_ldh_name FROM {schema}.variant_name vn WHERE vn.var_id=?;
