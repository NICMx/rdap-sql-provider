#getByUserName
SELECT * FROM rdap.rdap_user_role rur WHERE rur.rus_name=?;

#storeToDatabase
INSERT INTO rdap.rdap_user_role  VALUES (?,?);

#deleteFromDatabase
DELETE FROM rdap.rdap_user_role WHERE rur.rur_name=?;