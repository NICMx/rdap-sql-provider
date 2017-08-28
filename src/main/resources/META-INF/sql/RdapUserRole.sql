#getByUserName
SELECT rus_name, rur_name FROM {schema}.rdap_user_role rur WHERE rur.rus_name=?;

