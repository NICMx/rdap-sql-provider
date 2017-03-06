#storeToDatabase
INSERT INTO {schema}.remark_description VALUES (?,?,?);

#getByRemarkId
SELECT * FROM {schema}.remark_description rem_desc WHERE rem_desc.rem_id=? ORDER BY rem_desc.rde_order ASC;

#deleteRemarkDescriptionByRemarkId
DELETE FROM {schema}.remark_description WHERE rem_id=?;