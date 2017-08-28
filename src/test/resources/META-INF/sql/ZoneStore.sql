#storeToDatabase
INSERT INTO {schema}.zone VALUES(null,?);

#getByZoneName
SELECT zone_id, zone_name FROM  {schema}.zone WHERE zone_name=?;

