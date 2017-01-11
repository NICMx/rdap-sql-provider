#storeToDatabase
INSERT INTO rdap.secure_dns VALUES (null, ?, ?, ?, ?);

#getByDomain
SELECT * FROM rdap.secure_dns s WHERE s.dom_id = ?;

#deleteFromDatabase
DELETE FROM rdap.secure_dns WHERE dom_id=?;