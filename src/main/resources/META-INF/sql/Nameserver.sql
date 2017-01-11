#storeToDatabase
INSERT INTO rdap.nameserver VALUES(null,?,?,?,?);

#updateInDatabase
UPDATE rdap.nameserver SET nse_ldh_name=?, nse_unicode_name=?, nse_port43=? WHERE nse_id=?;

#getByHandle
SELECT * FROM rdap.nameserver WHERE nse_handle=?;

#storeDomainNameserversToDatabase
INSERT INTO rdap.domain_nameservers VALUES(?,?);

#findByName
SELECT * FROM rdap.nameserver nse WHERE nse.nse_ldh_name=? OR nse.nse_unicode_name = ?;

#getByDomainId
SELECT nse.* FROM rdap.nameserver nse JOIN rdap.domain_nameservers dom ON dom.nse_id=nse.nse_id WHERE dom.dom_id=?;

#searchByPartialName
SELECT DISTINCT(nse.nse_id), nse.nse_handle,nse.nse_ldh_name, nse.nse_port43, nse.nse_unicode_name FROM rdap.nameserver nse WHERE nse.nse_ldh_name LIKE ? OR nse.nse_unicode_name LIKE ? ORDER BY 1 LIMIT ?;

#searchByName
SELECT DISTINCT(nse.nse_id), nse.nse_handle,nse.nse_ldh_name, nse.nse_port43, nse.nse_unicode_name FROM rdap.nameserver nse WHERE nse.nse_ldh_name=? OR nse.nse_unicode_name=? ORDER BY 1 LIMIT ?;

#searchByIp4
SELECT DISTINCT(nse.nse_id), nse.nse_handle,nse.nse_ldh_name, nse.nse_port43, nse.nse_unicode_name FROM rdap.nameserver nse join rdap.ip_address ipa on ipa.nse_id=nse.nse_id WHERE ipa.iad_value=INET_ATON(?) ORDER BY 1 LIMIT ?;

#searchByIp6
SELECT DISTINCT(nse.nse_id), nse.nse_handle,nse.nse_ldh_name, nse.nse_port43, nse.nse_unicode_name FROM rdap.nameserver nse join rdap.ip_address ipa on ipa.nse_id=nse.nse_id WHERE ipa.iad_value=INET6_ATON(?) ORDER BY 1 LIMIT ?;

#getAll
SELECT * FROM rdap.nameserver nse;

#deleteDomainNameserversRelation
DELETE FROM rdap.domain_nameservers WHERE dom_id=?;

#existByName
SELECT EXISTS(SELECT 1 FROM rdap.nameserver nse WHERE nse.nse_ldh_name=? OR nse.nse_unicode_name=?);

#existByPartialName
SELECT EXISTS(SELECT 1  FROM rdap.nameserver nse WHERE nse.nse_ldh_name LIKE ? OR nse.nse_unicode_name LIKE ?);

#existByIp4
SELECT EXISTS(SELECT 1 FROM rdap.nameserver nse join rdap.ip_address ipa on ipa.nse_id=nse.nse_id WHERE ipa.iad_value=INET_ATON(?));

#existByIp6
SELECT EXISTS(SELECT 1 FROM rdap.nameserver nse join rdap.ip_address ipa on ipa.nse_id=nse.nse_id WHERE ipa.iad_value=INET6_ATON(?));

#searchByRegexName
SELECT DISTINCT(nse.nse_id), nse.nse_handle,nse.nse_ldh_name, nse.nse_port43, nse.nse_unicode_name FROM rdap.nameserver nse WHERE nse.nse_ldh_name REGEXP ? OR nse.nse_unicode_name REGEXP ? ORDER BY 1 LIMIT ?;
