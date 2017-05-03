#getByRemarkId
SELECT * FROM {schema}.remark_description rem_desc WHERE rem_desc.rem_id=? ORDER BY rem_desc.rde_order ASC;

