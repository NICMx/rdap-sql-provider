#findByName
SELECT * FROM {schema}.nameserver nse WHERE nse.nse_ldh_name=? OR nse.nse_unicode_name = ?;

#getByDomainId
SELECT nse.* FROM {schema}.nameserver nse JOIN {schema}.domain_nameservers dom ON dom.nse_id=nse.nse_id WHERE dom.dom_id=?;

#searchByPartialName
SELECT DISTINCT(nse.nse_id), nse.nse_handle,nse.nse_ldh_name, nse.nse_port43, nse.nse_unicode_name FROM {schema}.nameserver nse WHERE nse.nse_ldh_name LIKE ? OR nse.nse_unicode_name LIKE ? ORDER BY 1 LIMIT ?;

#searchByName
SELECT DISTINCT(nse.nse_id), nse.nse_handle,nse.nse_ldh_name, nse.nse_port43, nse.nse_unicode_name FROM {schema}.nameserver nse WHERE nse.nse_ldh_name=? OR nse.nse_unicode_name=? ORDER BY 1 LIMIT ?;

#searchByIp4
SELECT DISTINCT(nse.nse_id), nse.nse_handle,nse.nse_ldh_name, nse.nse_port43, nse.nse_unicode_name FROM {schema}.nameserver nse join {schema}.ip_address ipa on ipa.nse_id=nse.nse_id WHERE ipa.iad_value=? ORDER BY 1 LIMIT ?;

#searchByIp6
SELECT DISTINCT(nse.nse_id), nse.nse_handle,nse.nse_ldh_name, nse.nse_port43, nse.nse_unicode_name FROM {schema}.nameserver nse join {schema}.ip_address ipa on ipa.nse_id=nse.nse_id WHERE ipa.iad_value=? ORDER BY 1 LIMIT ?;

#searchByRegexName
SELECT DISTINCT(nse.nse_id), nse.nse_handle,nse.nse_ldh_name, nse.nse_port43, nse.nse_unicode_name FROM {schema}.nameserver nse WHERE nse.nse_ldh_name REGEXP ? OR nse.nse_unicode_name REGEXP ? ORDER BY 1 LIMIT ?;
