#getByUserName
SELECT * FROM {schema}.rdap_user_role rur WHERE rur.rus_name=?;

#storeToDatabase
INSERT INTO {schema}.rdap_user_role  VALUES (?,?);

#deleteFromDatabase
DELETE FROM {schema}.rdap_user_role WHERE rur.rur_name=?;