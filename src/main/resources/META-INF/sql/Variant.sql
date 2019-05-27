#getByDomainId
SELECT var_id, var_idn_table, dom_id FROM {schema}.variant v WHERE v.dom_id=?;

#getAll
SELECT var_id, var_idn_table, dom_id FROM {schema}.variant;

#getVariantRelationsByVariantId
SELECT rel_id FROM {schema}.variant_relation vr WHERE vr.var_id=?;

#getVariantNamesByVariantId
SELECT vna_unicode_name FROM {schema}.variant_name vn WHERE vn.var_id=?;
