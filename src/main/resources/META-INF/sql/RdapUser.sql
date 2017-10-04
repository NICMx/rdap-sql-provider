#getByName
SELECT rus_name, rus_pass, rus_max_search_results FROM {schema}.rdap_user rus WHERE rus.rus_name=?;
