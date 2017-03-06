#storeToDatabase
INSERT INTO {schema}.zone VALUES(null,?);

#getByZoneId
SELECT * FROM {schema}.zone WHERE zone_id=?;

#getByZoneName
SELECT * FROM  {schema}.zone WHERE zone_name=?;

#getAll
SELECT * FROM {schema}.zone;
