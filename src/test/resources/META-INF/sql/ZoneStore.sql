#storeToDatabase
INSERT INTO {schema}.zone VALUES(null,?);

#getByZoneName
SELECT * FROM  {schema}.zone WHERE zone_name=?;

