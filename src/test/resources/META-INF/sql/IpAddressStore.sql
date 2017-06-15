
#storeToDatabase
INSERT INTO {schema}.ip_address  VALUES (null,?,?,IF(?=4,INET_ATON(?),INET6_ATON(?)));

