#storeToDatabase
INSERT INTO {schema}.registrar(rar_id, rar_handle, rar_port43) VALUES (null, ?, ?);

#getById
SELECT * FROM {schema}.registrar r WHERE r.rar_id = ?;

#getByHandle
SELECT * FROM {schema}.registrar r WHERE r.rar_handle = ?;

#getSimpleRegistrarById
SELECT r.rar_id, r.rar_handle, r.rar_port43 FROM {schema}.registrar r WHERE r.rar_id = ?;

#getSimpleRegistrarByHandle
SELECT r.rar_id, r.rar_handle, r.rar_port43 FROM {schema}.registrar r WHERE r.rar_handle = ?;

#getRegistarIdByHandle
SELECT r.rar_id FROM {schema}.registrar r WHERE r.rar_handle = ?;

#getAll
SELECT * FROM {schema}.registrar ORDER BY 1 ASC;