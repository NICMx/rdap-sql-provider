#getByHandle
SELECT nse_id, nse_handle, nse_port43, nse_unicode_name FROM {schema}.nameserver WHERE nse_handle=?;

#storeToDatabase
INSERT INTO {schema}.nameserver VALUES(null,?,?,?);

#storeDomainNameserversToDatabase
INSERT INTO {schema}.domain_nameservers VALUES(?,?);

