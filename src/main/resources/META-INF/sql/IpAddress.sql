#getByNameserverId
SELECT * FROM rdap.ip_address iad WHERE iad.nse_id=?;

#storeToDatabase
INSERT INTO rdap.ip_address  VALUES (null,?,?,IFNULL(?=4,INET_ATON(?),INET6_ATON(?)));

#getAll
SELECT * FROM rdap.ip_address ORDER BY 1 ASC;

#deleteByNameserverId
delete from rdap.ip_address where nse_id=?;