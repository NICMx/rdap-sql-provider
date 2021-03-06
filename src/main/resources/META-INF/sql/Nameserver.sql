#findByName
SELECT nse_id, nse_handle, nse_port43, nse_unicode_name FROM {schema}.nameserver nse WHERE nse.nse_unicode_name = ?;

#findByHandle
SELECT nse_id, nse_handle, nse_port43, nse_unicode_name FROM {schema}.nameserver nse WHERE nse.nse_handle=?;

#countByName
SELECT COUNT(nse_id) FROM {schema}.nameserver nse WHERE nse.nse_unicode_name = ?;

#getByDomainId
SELECT nse.nse_id, nse.nse_handle, nse.nse_port43, nse.nse_unicode_name FROM {schema}.nameserver nse JOIN {schema}.domain_nameservers dom ON dom.nse_id=nse.nse_id WHERE dom.dom_id=?;

#searchByPartialName
SELECT DISTINCT(nse.nse_id), nse.nse_handle, nse.nse_port43, nse.nse_unicode_name FROM {schema}.nameserver nse WHERE nse.nse_unicode_name LIKE ? ORDER BY 1 LIMIT ?;

#searchByName
SELECT DISTINCT(nse.nse_id), nse.nse_handle, nse.nse_port43, nse.nse_unicode_name FROM {schema}.nameserver nse WHERE nse.nse_unicode_name=? ORDER BY 1 LIMIT ?;

#searchByIp4
SELECT DISTINCT(nse.nse_id), nse.nse_handle, nse.nse_port43, nse.nse_unicode_name FROM {schema}.nameserver nse join {schema}.ip_address ipa on ipa.nse_id=nse.nse_id WHERE ipa.iad_value=INET_ATON(?) ORDER BY 1 LIMIT ?;

#searchByIp6
SELECT DISTINCT(nse.nse_id), nse.nse_handle, nse.nse_port43, nse.nse_unicode_name FROM {schema}.nameserver nse join {schema}.ip_address ipa on ipa.nse_id=nse.nse_id WHERE ipa.iad_value=INET6_ATON(?) ORDER BY 1 LIMIT ?;

#searchByRegexName
SELECT DISTINCT(nse.nse_id), nse.nse_handle, nse.nse_port43, nse.nse_unicode_name FROM {schema}.nameserver nse WHERE nse.nse_unicode_name REGEXP ? ORDER BY 1 LIMIT ?;
