#getByNameserverId
SELECT iad.iad_id,iad.nse_id,iad.iad_type, IF(iad.iad_type=4,INET_NTOA(iad.iad_value),INET6_NTOA(iad.iad_value)) as iad_value from {schema}.ip_address iad WHERE iad.nse_id=?;

