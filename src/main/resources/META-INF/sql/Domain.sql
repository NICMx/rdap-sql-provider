#getByDomainName
SELECT dom_id, dom_handle, dom_unicode_name, dom_port43, zone_id FROM {schema}.domain WHERE dom_unicode_name=? AND zone_id = ?;

#searchByPartialNameWZone
SELECT domain.dom_id, domain.dom_handle, domain.dom_unicode_name, domain.dom_port43, domain.zone_id FROM {schema}.domain WHERE domain.dom_unicode_name LIKE ? AND domain.zone_id = ? ORDER BY 1 LIMIT ?;

#searchByNameWZone
SELECT domain.dom_id, domain.dom_handle, domain.dom_unicode_name, domain.dom_port43, domain.zone_id FROM {schema}.domain WHERE dom_unicode_name=? AND domain.zone_id = ? ORDER BY 1 LIMIT ?;

#searchByPartialNameWPartialZone
SELECT DISTINCT(d.dom_id), d.dom_handle, d.dom_port43, d.zone_id, d.dom_unicode_name FROM {schema}.domain d JOIN {schema}.zone z on d.zone_id = z.zone_id AND z.zone_id IN (?) WHERE d.dom_unicode_name LIKE ? AND z.zone_name like ? LIMIT ?;

#searchByNameWPartialZone
SELECT DISTINCT(d.dom_id), d.dom_handle, d.dom_port43, d.zone_id, d.dom_unicode_name FROM {schema}.domain d JOIN {schema}.zone z on d.zone_id = z.zone_id AND z.zone_id IN (?) WHERE d.dom_unicode_name = ? AND z.zone_name like ? LIMIT ?;

#searchByNameWOutZone
SELECT d.dom_id, d.dom_handle, d.dom_unicode_name, d.dom_port43, d.zone_id FROM {schema}.domain d WHERE d.zone_id IN (?)  AND d.dom_unicode_name = ? ORDER BY 1 LIMIT ?;

#searchByPartialNameWOutZone
SELECT d.dom_id, d.dom_handle, d.dom_unicode_name, d.dom_port43, d.zone_id FROM {schema}.domain d WHERE d.zone_id IN (?) AND d.dom_unicode_name LIKE ? ORDER BY 1 LIMIT ?;

#searchByNsName
SELECT DISTINCT (dom.dom_id), dom.dom_handle, dom.dom_port43, dom.zone_id, dom.dom_unicode_name FROM {schema}.domain dom JOIN {schema}.domain_nameservers dom_ns ON dom_ns.dom_id = dom.dom_id JOIN {schema}.nameserver ns ON ns.nse_id = dom_ns.nse_id WHERE ns.nse_unicode_name LIKE ? ORDER BY 1 LIMIT ?;

#searchByNsIp
SELECT DISTINCT (dom.dom_id), dom.dom_handle, dom.dom_port43, dom.zone_id, dom.dom_unicode_name FROM {schema}.domain dom JOIN {schema}.domain_nameservers dom_ns ON dom_ns.dom_id = dom.dom_id JOIN {schema}.nameserver ns ON ns.nse_id = dom_ns.nse_id JOIN {schema}.ip_address ip	ON ip.nse_id = ns.nse_id WHERE IF(?=4, INET_ATON(?),INET6_ATON(?)) = ip.iad_value ORDER BY 1 LIMIT ?;

#searchByRegexNameWithZone
SELECT DISTINCT(d.dom_id), d.dom_handle,d.dom_port43, d.zone_id, d.dom_unicode_name FROM {schema}.domain d JOIN {schema}.zone z on d.zone_id = z.zone_id AND z.zone_id IN (?) WHERE d.dom_unicode_name REGEXP ?) AND z.zone_name REGEXP ? LIMIT ?;

#searchByRegexNameWithOutZone
SELECT DISTINCT(d.dom_id), d.dom_handle,d.dom_port43, d.zone_id, d.dom_unicode_name FROM {schema}.domain d WHERE d.zone_id IN (?) AND d.dom_unicode_name REGEXP ? ORDER BY 1 LIMIT ?;

#searchByRegexNsName
SELECT DISTINCT (dom.dom_id), dom.dom_handle, dom.dom_port43, dom.zone_id, dom.dom_unicode_name FROM {schema}.domain dom JOIN {schema}.domain_nameservers dom_ns ON dom_ns.dom_id = dom.dom_id JOIN {schema}.nameserver ns ON ns.nse_id = dom_ns.nse_id WHERE ns.nse_unicode_name REGEXP ? ORDER BY 1 LIMIT ?;
