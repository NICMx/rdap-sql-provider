#storeToDatabase
INSERT INTO rdap.domain(dom_handle,dom_ldh_name,dom_unicode_name,dom_port43,zone_id) VALUES (?,?,?,?,?);

#updateInDatabase
UPDATE rdap.domain SET dom_ldh_name=?,dom_unicode_name=?,dom_port43=?,zone_id=? WHERE dom_id=?;

#getByHandle
SELECT * FROM rdap.domain WHERE dom_handle=?;

#getByLdhName
SELECT * FROM rdap.domain WHERE (dom_ldh_name=? OR dom_unicode_name=?) AND zone_id = ?;

#existByLdhName
SELECT EXISTS(SELECT 1 FROM rdap.domain WHERE (dom_ldh_name=? OR dom_unicode_name=?) AND zone_id = ?);

#getDomainById
SELECT * FROM rdap.domain WHERE dom_id=?;

#searchByPartialNameWZone
SELECT domain.* FROM rdap.domain WHERE (domain.dom_ldh_name LIKE ? OR domain.dom_unicode_name LIKE ? ) AND domain.zone_id = ? ORDER BY 1 LIMIT ?;

#searchByNameWZone
SELECT domain.* FROM rdap.domain WHERE (dom_ldh_name=? OR dom_unicode_name=?) AND domain.zone_id = ? ORDER BY 1 LIMIT ?;

#searchByPartialNameWPartialZone
SELECT DISTINCT(d.dom_id), d.dom_handle, d.dom_ldh_name, d.dom_port43, d.zone_id, d.dom_unicode_name FROM rdap.domain d JOIN rdap.zone z on d.zone_id = z.zone_id AND z.zone_id IN (?) WHERE (d.dom_ldh_name LIKE ? OR d.dom_unicode_name LIKE ?) AND z.zone_name like ? LIMIT ?;

#searchByNameWPartialZone
SELECT DISTINCT(d.dom_id), d.dom_handle, d.dom_ldh_name, d.dom_port43, d.zone_id, d.dom_unicode_name FROM rdap.domain d JOIN rdap.zone z on d.zone_id = z.zone_id AND z.zone_id IN (?) WHERE (d.dom_ldh_name = ? OR d.dom_unicode_name = ?) AND z.zone_name like ? LIMIT ?;

#searchByNameWOutZone
SELECT d.* FROM rdap.domain d WHERE d.zone_id IN (?)  AND (d.dom_ldh_name = ? OR d.dom_unicode_name = ?) ORDER BY 1 LIMIT ?;

#searchByPartialNameWOutZone
SELECT d.* FROM rdap.domain d WHERE d.zone_id IN (?) AND (d.dom_ldh_name LIKE ? OR d.dom_unicode_name LIKE ?) ORDER BY 1 LIMIT ?;

#searchByNsLdhName
SELECT DISTINCT (dom.dom_id), dom.dom_ldh_name, dom.dom_handle, dom.dom_port43, dom.zone_id, dom.dom_unicode_name FROM rdap.domain dom JOIN rdap.domain_nameservers dom_ns ON dom_ns.dom_id = dom.dom_id JOIN rdap.nameserver ns ON ns.nse_id = dom_ns.nse_id WHERE  (ns.nse_ldh_name LIKE ? OR ns.nse_unicode_name LIKE ?) ORDER BY 1 LIMIT ?;

#searchByNsIp
SELECT DISTINCT (dom.dom_id), dom.dom_ldh_name, dom.dom_handle, dom.dom_port43, dom.zone_id, dom.dom_unicode_name FROM rdap.domain dom JOIN rdap.domain_nameservers dom_ns ON dom_ns.dom_id = dom.dom_id JOIN rdap.nameserver ns ON ns.nse_id = dom_ns.nse_id JOIN rdap.ip_address ip	ON ip.nse_id = ns.nse_id WHERE IF(?=4, INET_ATON(?),INET6_ATON(?)) = ip.iad_value ORDER BY 1 LIMIT ?;

#existByNsLdhName
SELECT EXISTS(SELECT 1 FROM rdap.domain dom JOIN rdap.domain_nameservers dom_ns ON dom_ns.dom_id = dom.dom_id JOIN rdap.nameserver ns ON ns.nse_id = dom_ns.nse_id WHERE ( ns.nse_ldh_name LIKE ? OR ns.nse_unicode_name LIKE ?));

#existByNsIp
SELECT EXISTS(SELECT 1 FROM rdap.domain dom JOIN rdap.domain_nameservers dom_ns ON dom_ns.dom_id = dom.dom_id JOIN rdap.nameserver ns ON ns.nse_id = dom_ns.nse_id JOIN rdap.ip_address ip	ON ip.nse_id = ns.nse_id WHERE IF(?=4, INET_ATON(?),INET6_ATON(?)) = ip.iad_value);

#existByNameWZone
SELECT EXISTS(SELECT 1 FROM rdap.domain WHERE (domain.dom_ldh_name = ? OR domain.dom_unicode_name = ?) AND domain.zone_id = ? );

#existByPartialNameWZone
SELECT EXISTS(SELECT 1 FROM rdap.domain WHERE (domain.dom_ldh_name LIKE ? OR domain.dom_unicode_name LIKE = ?) AND domain.zone_id = ?);

#existByNameWPartialZone
SELECT EXISTS(SELECT 1 FROM rdap.domain d JOIN rdap.zone z on d.zone_id = z.zone_id AND z.zone_id IN (?) WHERE (d.dom_ldh_name = ? OR d.dom_unicode_name = ?) AND z.zone_name like ? );

#existByPartialNameWPartialZone
SELECT EXISTS(SELECT 1 FROM rdap.domain d JOIN rdap.zone z on d.zone_id = z.zone_id AND z.zone_id IN (?) WHERE (d.dom_ldh_name LIKE ? OR d.dom_unicode_name LIKE ?) AND z.zone_name like ? );

#existByPartialNameWOutZone
SELECT EXISTS(SELECT 1  FROM rdap.domain d WHERE d.zone_id IN (?) AND (d.dom_ldh_name LIKE ? OR d.dom_unicode_name LIKE ?));

#existByNameWOutZone
SELECT EXISTS(SELECT 1 FROM rdap.domain d WHERE d.zone_id IN (?)  AND (d.dom_ldh_name = ? OR d.dom_unicode_name = ?));


#storeDomainIpNetworkRelation
INSERT INTO rdap.domain_networks VALUES (?, ?);

#deleteDomainIpNetworkRelation
DELETE FROM rdap.domain_networks WHERE dom_id=?;

#searchByRegexNameWithZone
SELECT DISTINCT(d.dom_id), d.dom_handle, d.dom_ldh_name, d.dom_port43, d.zone_id, d.dom_unicode_name FROM rdap.domain d JOIN rdap.zone z on d.zone_id = z.zone_id AND z.zone_id IN (?) WHERE (d.dom_ldh_name REGEXP ? OR d.dom_unicode_name REGEXP ?) AND z.zone_name REGEXP ? LIMIT ?;

#searchByRegexNameWithOutZone
SELECT DISTINCT(d.dom_id), d.dom_handle, d.dom_ldh_name, d.dom_port43, d.zone_id, d.dom_unicode_name FROM rdap.domain d WHERE d.zone_id IN (?) AND (d.dom_ldh_name REGEXP ? OR d.dom_unicode_name REGEXP ?) ORDER BY 1 LIMIT ?;

#searchByRegexNsLdhName
SELECT DISTINCT (dom.dom_id), dom.dom_ldh_name, dom.dom_handle, dom.dom_port43, dom.zone_id, dom.dom_unicode_name FROM rdap.domain dom JOIN rdap.domain_nameservers dom_ns ON dom_ns.dom_id = dom.dom_id JOIN rdap.nameserver ns ON ns.nse_id = dom_ns.nse_id WHERE  (ns.nse_ldh_name REGEXP ? OR ns.nse_unicode_name REGEXP ?) ORDER BY 1 LIMIT ?;
