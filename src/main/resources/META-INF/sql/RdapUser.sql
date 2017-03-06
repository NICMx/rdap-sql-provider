#storeToDatabase
INSERT INTO {schema}.rdap_user VALUES(null,?,?,?);

#getByName
SELECT * FROM {schema}.rdap_user rus WHERE rus.rus_name=?;

#deleteAllRdapUserRoles
DELETE  FROM {schema}.rdap_user_role;

#deleteAllRdapUsers
DELETE  FROM {schema}.rdap_user;

#updateInDatabase
UPDATE {schema}.rdap_user SET rus_pass=?, rus_max_search_results=? WHERE rus_id =?;