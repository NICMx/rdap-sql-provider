#getByUserName
SELECT rar.rar_name, rar.rar_description FROM {schema}.rdap_access_role rar JOIN {schema}.rdap_user_role rur ON rur.rar_name = rar.rar_name WHERE rur.rus_name=?;
