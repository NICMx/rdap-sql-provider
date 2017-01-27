-- -----------------------------------------------------
-- Zones
-- -----------------------------------------------------
INSERT INTO rdap.zone VALUES (1,'com');
INSERT INTO rdap.zone VALUES (2,'com.mx');
INSERT INTO rdap.zone VALUES (3,'mx');
INSERT INTO rdap.zone VALUES (4,'lat');
INSERT INTO rdap.zone VALUES (5,'in-addr.arpa');

-- -----------------------------------------------------
-- Domains
-- -----------------------------------------------------
INSERT INTO rdap.domain(dom_id,dom_handle,dom_ldh_name,dom_unicode_name,dom_port43,zone_id) VALUES(1,'DOM1','whiterabbit',null,'whois.com',1);
INSERT INTO rdap.domain VALUES(2,'DOMCOM','goldfish',null,'whois.com',1);
INSERT INTO rdap.domain VALUES(3,'XXX2','reddog',null,'whois.com',1);
INSERT INTO rdap.domain VALUES(4,'1234','blackcat',null,'whois.com',1);
INSERT INTO rdap.domain VALUES(5,'ylb','yellowbird',null,'whois.com',1);

INSERT INTO rdap.domain VALUES(6,'DOM2','conejo_blanco',null,'whois.com.mx',2);
INSERT INTO rdap.domain VALUES(7,'DOMCOMMX','pez_dorado',null,'whois.com.mx',2);
INSERT INTO rdap.domain VALUES(8,'XXX3','perro_rojo',null,'whois.com.mx',2);
INSERT INTO rdap.domain VALUES(9,'1235','gato_negro',null,'whois.com.mx',2);
INSERT INTO rdap.domain VALUES(10,'pjra','pajaro_amarillo',null,'whois.com.mx',2);

INSERT INTO rdap.domain VALUES(11,'DOM3','conejo_blanco',null,'whois.mx',3);
INSERT INTO rdap.domain VALUES(12,'DOMMX','pez_dorado',null,'whois.mx',3);
INSERT INTO rdap.domain VALUES(13,'XXX4','perro_rojo',null,'whois.mx',3);
INSERT INTO rdap.domain VALUES(14,'1236','gato_negro',null,'whois.mx',3);
INSERT INTO rdap.domain VALUES(15,'','pajaro_amarillo',null,'whois.mx',3);

INSERT INTO rdap.domain VALUES(16,'DOM4','choco',null,'whois.lat',4);
INSERT INTO rdap.domain VALUES(17,'DOMLAT','moka',null,'whois.lat',4);
INSERT INTO rdap.domain VALUES(18,'XXX6','1.0.168.192',null,'whois.lat',5);
INSERT INTO rdap.domain VALUES(19,'1236','méxico','xn--mxico-bsa','whois.lat',4);
INSERT INTO rdap.domain VALUES(20,'xnxn','xn--elpjaroamarillo-pjb','elpájaroamarillo','whois.lat',4);

INSERT INTO rdap.entity VALUES(1,'mr_rabbit','whois.com');
INSERT INTO rdap.entity VALUES(2,'mr_fish','whois.com');
INSERT INTO rdap.entity VALUES(3,'mr_dog','whois.com');
INSERT INTO rdap.entity VALUES(4,'mr_cat','whois.com');
INSERT INTO rdap.entity VALUES(5,'mr_bird','whois.com');

INSERT INTO rdap.entity VALUES(6,'don_conejo','whois.mx');
INSERT INTO rdap.entity VALUES(7,'don_pez','whois.mx');
INSERT INTO rdap.entity VALUES(8,'don_perro','whois.mx');
INSERT INTO rdap.entity VALUES(9,'don_gato','whois.mx');
INSERT INTO rdap.entity VALUES(10,'don_pajaro','whois.mx');

INSERT INTO rdap.entity VALUES(11,'sr_conejo','whois.com.mx');
INSERT INTO rdap.entity VALUES(12,'sr_pez','whois.com.mx');
INSERT INTO rdap.entity VALUES(13,'sr_perro','whois.com.mx');
INSERT INTO rdap.entity VALUES(14,'sr_gato','whois.com.mx');
INSERT INTO rdap.entity VALUES(15,'sr_pajaro','whois.com.mx');

INSERT INTO rdap.entity VALUES(16,'cone','whois.lat');
INSERT INTO rdap.entity VALUES(17,'pez','whois.lat');
INSERT INTO rdap.entity VALUES(18,'perr','whois.lat');
INSERT INTO rdap.entity VALUES(19,'gat','whois.lat');
INSERT INTO rdap.entity VALUES(20,'paj','whois.lat');

INSERT INTO rdap.autonomous_system_number VALUES(1,'ASN1',1,10,'ASN-ONE-TEN','mx-type','whois.mx','484');
INSERT INTO rdap.autonomous_system_number VALUES(2,'ASN2',10,20,'ASN-TEN-TWELVE','mx-type','whois.mx','484');
INSERT INTO rdap.autonomous_system_number VALUES(3,'ASN3',20,30,'ASN-TWELVE-THIRTY','mx-type','whois.mx','484');
INSERT INTO rdap.autonomous_system_number VALUES(4,'ASN4',30,40,'ASN-THIRTY-FOURTY','mx-type','whois.mx','484');
INSERT INTO rdap.autonomous_system_number VALUES(5,'ASN5',40,50,'ASN-FOURTY-FIFTY','mx-type','whois.mx','484');

INSERT INTO rdap.autonomous_system_number VALUES(6,'ASN6',100,200,'ASN100','com-type','whois.com','826');
INSERT INTO rdap.autonomous_system_number VALUES(7,'ASN7',200,300,'ASN200','com-type','whois.com','826');
INSERT INTO rdap.autonomous_system_number VALUES(8,'ASN8',300,400,'ASN300','com-type','whois.com','826');
INSERT INTO rdap.autonomous_system_number VALUES(9,'ASN9',400,500,'ASN400','com-type','whois.com','826');
INSERT INTO rdap.autonomous_system_number VALUES(10,'ASN10',500,1000,'ASN500','com-type','whois.com','826');