#getByHandle
SELECT * FROM {schema}.nameserver WHERE nse_handle=?;

#storeToDatabase
INSERT INTO {schema}.nameserver VALUES(null,?,?,?,?);

#storeDomainNameserversToDatabase
INSERT INTO {schema}.domain_nameservers VALUES(?,?);

