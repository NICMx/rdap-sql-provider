#01_delete_entity_contact
DELETE  FROM {schema}.entity_contact;

#02_delete_entity_entity_roles 
DELETE  FROM {schema}.entity_entity_roles;

#03_delete_entity_links
DELETE  FROM {schema}.entity_links;

#04_delete_entity_remarks
DELETE  FROM {schema}.entity_remarks;

#05_delete_entity_status
DELETE  FROM {schema}.entity_status;

#06_delete_entity_events
DELETE  FROM {schema}.entity_events;

#07_delete_vcard_postal_info
DELETE  FROM {schema}.vcard_postal_info;

#08_delete_vcard
DELETE  FROM {schema}.vcard;

#09_delete_entity_public_ids
DELETE  FROM {schema}.entity_public_ids;

#10_delete_nameserver_entity_roles 
DELETE  FROM {schema}.nameserver_entity_roles;

#11_delete_nameserver_links
DELETE  FROM {schema}.nameserver_links;

#12_delete_nameserver_remarks
DELETE  FROM {schema}.nameserver_remarks;

#13_delete_nameserver_status
DELETE  FROM {schema}.nameserver_status;

#14_delete_nameserver_events
DELETE  FROM {schema}.nameserver_events;

#15_delete_ip_address
DELETE  FROM {schema}.ip_address;

#16_delete_domain_nameservers
DELETE  FROM {schema}.domain_nameservers;

#17_delete_domain_entity_roles 
DELETE  FROM {schema}.domain_entity_roles;

#18_delete_domain_links
DELETE  FROM {schema}.domain_links;

#19_delete_domain_remarks
DELETE  FROM {schema}.domain_remarks;

#20_delete_domain_status
DELETE  FROM {schema}.domain_status;

#21_delete_domain_events
DELETE  FROM {schema}.domain_events;

#22_delete_domain_networks
DELETE  FROM {schema}.domain_networks;

#23_delete_domain_public_ids
DELETE  FROM {schema}.domain_public_ids;

#24_delete_ds_links
DELETE  FROM {schema}.ds_links;

#25_delete_ds_events
DELETE  FROM {schema}.ds_events;

#26_delete_ds_data
DELETE  FROM {schema}.ds_data;

#27_delete_variant_relation
DELETE  FROM {schema}.variant_relation;

#28_delete_variant_name
DELETE  FROM {schema}.variant_name;

#29_delete_variant
DELETE  FROM {schema}.variant;

#30_delete_secure_dns
DELETE  FROM {schema}.secure_dns;

#32_delete_asn_entity_roles
DELETE  FROM {schema}.asn_entity_roles;

#33_delete_asn_links
DELETE  FROM {schema}.asn_links;

#34_delete_asn_remarks
DELETE  FROM {schema}.asn_remarks;

#35_delete_asn_status
DELETE  FROM {schema}.asn_status;

#36_delete_asn_events
DELETE  FROM {schema}.asn_events;

#37_delete_ip_network_parent_relation
DELETE  FROM {schema}.ip_network_parent_relation;

#38_delete_ip_network_entity_roles
DELETE  FROM {schema}.ip_network_entity_roles;

#39_delete_ip_network_links
DELETE  FROM {schema}.ip_network_links;

#40_delete_ip_network_remarks
DELETE  FROM {schema}.ip_network_remarks;

#41_delete_ip_network_status
DELETE  FROM {schema}.ip_network_status;

#42_delete_ip_network_events
DELETE  FROM {schema}.ip_network_events;

#43_delete_event_links
DELETE  FROM {schema}.event_links;

#44_delete_remark_links
DELETE  FROM {schema}.remark_links;

#45_delete_link
DELETE  FROM {schema}.link;

#46_delete_event
DELETE  FROM {schema}.event;

#47_delete_public_id
DELETE  FROM {schema}.public_id;

#48_delete_remark_description
DELETE  FROM {schema}.remark_description;

#49_delete_remark
DELETE  FROM {schema}.remark;

#50_delete_entity
DELETE  FROM {schema}.entity;

#51_delete_nameserver
DELETE  FROM {schema}.nameserver;

#52_delete_domain
DELETE  FROM {schema}.domain;

#53_delete_ip_networks
DELETE  FROM {schema}.ip_network;

#54_delete_asn
DELETE  FROM {schema}.autonomous_system_number;

#55_delete_zone
DELETE  FROM {schema}.zone;