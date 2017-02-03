-- -----------------------------------------------------
-- Catalogs
-- -----------------------------------------------------
-- Status
-- -----------------------------------------------------
	INSERT INTO rdap.status VALUES(1,'validated');
	INSERT INTO rdap.status VALUES(2,'renew prohibited');
	INSERT INTO rdap.status VALUES(3,'update prohibited');
	INSERT INTO rdap.status VALUES(4,'transfer prohibited');
	INSERT INTO rdap.status VALUES(5,'delete prohibited');
	INSERT INTO rdap.status VALUES(6,'proxy');
	INSERT INTO rdap.status VALUES(7,'private');
	INSERT INTO rdap.status VALUES(8,'removed');
	INSERT INTO rdap.status VALUES(9,'obscured');
	INSERT INTO rdap.status VALUES(10,'associated');
	INSERT INTO rdap.status VALUES(11,'active');
	INSERT INTO rdap.status VALUES(12,'inactive');
	INSERT INTO rdap.status VALUES(13,'locked');
	INSERT INTO rdap.status VALUES(14,'pending create');
	INSERT INTO rdap.status VALUES(15,'pending renew');
	INSERT INTO rdap.status VALUES(16,'pending transfer');
	INSERT INTO rdap.status VALUES(17,'pending update');
	INSERT INTO rdap.status VALUES(18,'pending delete');
	
-- -----------------------------------------------------
-- Event Actions
-- -----------------------------------------------------
	INSERT INTO rdap.event_action VALUES(1,'registration');
	INSERT INTO rdap.event_action VALUES(2,'reregistration');
	INSERT INTO rdap.event_action VALUES(3,'last changed');
	INSERT INTO rdap.event_action VALUES(4,'expiration');
	INSERT INTO rdap.event_action VALUES(5,'deletion');
	INSERT INTO rdap.event_action VALUES(6,'reinstantiation');
	INSERT INTO rdap.event_action VALUES(7,'transfer');
	INSERT INTO rdap.event_action VALUES(8,'locked');
	INSERT INTO rdap.event_action VALUES(9,'unlocked');
	INSERT INTO rdap.event_action VALUES(10,'last update of RDAP database');
	INSERT INTO rdap.event_action VALUES(11,'registrar expiration');
	INSERT INTO rdap.event_action VALUES(12,'enum validation expiration');

-- -----------------------------------------------------
-- Variant relation
-- -----------------------------------------------------
	INSERT INTO rdap.relation VALUES(1, 'registered');
	INSERT INTO rdap.relation VALUES(2, 'unregistered');
	INSERT INTO rdap.relation VALUES(3, 'registration restricted');
	INSERT INTO rdap.relation VALUES(4, 'open registration');
	INSERT INTO rdap.relation VALUES(5, 'conjoined');

-- -----------------------------------------------------
-- Roles
-- -----------------------------------------------------
	INSERT INTO rdap.roles VALUES (1,'registrant');
	INSERT INTO rdap.roles VALUES (2,'technical');
	INSERT INTO rdap.roles VALUES (3,'administrative');
	INSERT INTO rdap.roles VALUES (4,'abuse');
	INSERT INTO rdap.roles VALUES (5,'billing');
	INSERT INTO rdap.roles VALUES (6,'registrar');
	INSERT INTO rdap.roles VALUES (7,'reseller');
	INSERT INTO rdap.roles VALUES (8,'sponsor');
	INSERT INTO rdap.roles VALUES (9,'proxy');
	INSERT INTO rdap.roles VALUES (10,'notifications');
	INSERT INTO rdap.roles VALUES (11,'noc');

-- -----------------------------------------------------
-- Country Codes
-- -----------------------------------------------------
	INSERT INTO rdap.country_code VALUES (4,'AF');
	INSERT INTO rdap.country_code VALUES (248,'AX');
	INSERT INTO rdap.country_code VALUES (8,'AL');
	INSERT INTO rdap.country_code VALUES (12,'DZ');
	INSERT INTO rdap.country_code VALUES (16,'AS');
	INSERT INTO rdap.country_code VALUES (20,'AD');
	INSERT INTO rdap.country_code VALUES (24,'AO');
	INSERT INTO rdap.country_code VALUES (660,'AI');
	INSERT INTO rdap.country_code VALUES (10,'AQ');
	INSERT INTO rdap.country_code VALUES (28,'AG');
	INSERT INTO rdap.country_code VALUES (32,'AR');
	INSERT INTO rdap.country_code VALUES (51,'AM');
	INSERT INTO rdap.country_code VALUES (533,'AW');
	INSERT INTO rdap.country_code VALUES (36,'AU');
	INSERT INTO rdap.country_code VALUES (40,'AT');
	INSERT INTO rdap.country_code VALUES (31,'AZ');
	INSERT INTO rdap.country_code VALUES (44,'BS');
	INSERT INTO rdap.country_code VALUES (48,'BH');
	INSERT INTO rdap.country_code VALUES (50,'BD');
	INSERT INTO rdap.country_code VALUES (52,'BB');
	INSERT INTO rdap.country_code VALUES (112,'BY');
	INSERT INTO rdap.country_code VALUES (56,'BE');
	INSERT INTO rdap.country_code VALUES (84,'BZ');
	INSERT INTO rdap.country_code VALUES (204,'BJ');
	INSERT INTO rdap.country_code VALUES (60,'BM');
	INSERT INTO rdap.country_code VALUES (64,'BT');
	INSERT INTO rdap.country_code VALUES (68,'BO');
	INSERT INTO rdap.country_code VALUES (70,'BA');
	INSERT INTO rdap.country_code VALUES (72,'BW');
	INSERT INTO rdap.country_code VALUES (74,'BV');
	INSERT INTO rdap.country_code VALUES (76,'BR');
	INSERT INTO rdap.country_code VALUES (92,'VG');
	INSERT INTO rdap.country_code VALUES (86,'IO');
	INSERT INTO rdap.country_code VALUES (96,'BN');
	INSERT INTO rdap.country_code VALUES (100,'BG');
	INSERT INTO rdap.country_code VALUES (854,'BF');
	INSERT INTO rdap.country_code VALUES (108,'BI');
	INSERT INTO rdap.country_code VALUES (116,'KH');
	INSERT INTO rdap.country_code VALUES (120,'CM');
	INSERT INTO rdap.country_code VALUES (124,'CA');
	INSERT INTO rdap.country_code VALUES (132,'CV');
	INSERT INTO rdap.country_code VALUES (136,'KY');
	INSERT INTO rdap.country_code VALUES (140,'CF');
	INSERT INTO rdap.country_code VALUES (148,'TD');
	INSERT INTO rdap.country_code VALUES (152,'CL');
	INSERT INTO rdap.country_code VALUES (156,'CN');
	INSERT INTO rdap.country_code VALUES (344,'HK');
	INSERT INTO rdap.country_code VALUES (446,'MO');
	INSERT INTO rdap.country_code VALUES (162,'CX');
	INSERT INTO rdap.country_code VALUES (166,'CC');
	INSERT INTO rdap.country_code VALUES (170,'CO');
	INSERT INTO rdap.country_code VALUES (174,'KM');
	INSERT INTO rdap.country_code VALUES (178,'CG');
	INSERT INTO rdap.country_code VALUES (180,'CD');
	INSERT INTO rdap.country_code VALUES (184,'CK');
	INSERT INTO rdap.country_code VALUES (188,'CR');
	INSERT INTO rdap.country_code VALUES (384,'CI');
	INSERT INTO rdap.country_code VALUES (191,'HR');
	INSERT INTO rdap.country_code VALUES (192,'CU');
	INSERT INTO rdap.country_code VALUES (196,'CY');
	INSERT INTO rdap.country_code VALUES (203,'CZ');
	INSERT INTO rdap.country_code VALUES (208,'DK');
	INSERT INTO rdap.country_code VALUES (262,'DJ');
	INSERT INTO rdap.country_code VALUES (212,'DM');
	INSERT INTO rdap.country_code VALUES (214,'DO');
	INSERT INTO rdap.country_code VALUES (218,'EC');
	INSERT INTO rdap.country_code VALUES (818,'EG');
	INSERT INTO rdap.country_code VALUES (222,'SV');
	INSERT INTO rdap.country_code VALUES (226,'GQ');
	INSERT INTO rdap.country_code VALUES (232,'ER');
	INSERT INTO rdap.country_code VALUES (233,'EE');
	INSERT INTO rdap.country_code VALUES (231,'ET');
	INSERT INTO rdap.country_code VALUES (238,'FK');
	INSERT INTO rdap.country_code VALUES (234,'FO');
	INSERT INTO rdap.country_code VALUES (242,'FJ');
	INSERT INTO rdap.country_code VALUES (246,'FI');
	INSERT INTO rdap.country_code VALUES (250,'FR');
	INSERT INTO rdap.country_code VALUES (254,'GF');
	INSERT INTO rdap.country_code VALUES (258,'PF');
	INSERT INTO rdap.country_code VALUES (260,'TF');
	INSERT INTO rdap.country_code VALUES (266,'GA');
	INSERT INTO rdap.country_code VALUES (270,'GM');
	INSERT INTO rdap.country_code VALUES (268,'GE');
	INSERT INTO rdap.country_code VALUES (276,'DE');
	INSERT INTO rdap.country_code VALUES (288,'GH');
	INSERT INTO rdap.country_code VALUES (292,'GI');
	INSERT INTO rdap.country_code VALUES (300,'GR');
	INSERT INTO rdap.country_code VALUES (304,'GL');
	INSERT INTO rdap.country_code VALUES (308,'GD');
	INSERT INTO rdap.country_code VALUES (312,'GP');
	INSERT INTO rdap.country_code VALUES (316,'GU');
	INSERT INTO rdap.country_code VALUES (320,'GT');
	INSERT INTO rdap.country_code VALUES (831,'GG');
	INSERT INTO rdap.country_code VALUES (324,'GN');
	INSERT INTO rdap.country_code VALUES (624,'GW');
	INSERT INTO rdap.country_code VALUES (328,'GY');
	INSERT INTO rdap.country_code VALUES (332,'HT');
	INSERT INTO rdap.country_code VALUES (334,'HM');
	INSERT INTO rdap.country_code VALUES (336,'VA');
	INSERT INTO rdap.country_code VALUES (340,'HN');
	INSERT INTO rdap.country_code VALUES (348,'HU');
	INSERT INTO rdap.country_code VALUES (352,'IS');
	INSERT INTO rdap.country_code VALUES (356,'IN');
	INSERT INTO rdap.country_code VALUES (360,'ID');
	INSERT INTO rdap.country_code VALUES (364,'IR');
	INSERT INTO rdap.country_code VALUES (368,'IQ');
	INSERT INTO rdap.country_code VALUES (372,'IE');
	INSERT INTO rdap.country_code VALUES (833,'IM');
	INSERT INTO rdap.country_code VALUES (376,'IL');
	INSERT INTO rdap.country_code VALUES (380,'IT');
	INSERT INTO rdap.country_code VALUES (388,'JM');
	INSERT INTO rdap.country_code VALUES (392,'JP');
	INSERT INTO rdap.country_code VALUES (832,'JE');
	INSERT INTO rdap.country_code VALUES (400,'JO');
	INSERT INTO rdap.country_code VALUES (398,'KZ');
	INSERT INTO rdap.country_code VALUES (404,'KE');
	INSERT INTO rdap.country_code VALUES (296,'KI');
	INSERT INTO rdap.country_code VALUES (408,'KP');
	INSERT INTO rdap.country_code VALUES (410,'KR');
	INSERT INTO rdap.country_code VALUES (414,'KW');
	INSERT INTO rdap.country_code VALUES (417,'KG');
	INSERT INTO rdap.country_code VALUES (418,'LA');
	INSERT INTO rdap.country_code VALUES (428,'LV');
	INSERT INTO rdap.country_code VALUES (422,'LB');
	INSERT INTO rdap.country_code VALUES (426,'LS');
	INSERT INTO rdap.country_code VALUES (430,'LR');
	INSERT INTO rdap.country_code VALUES (434,'LY');
	INSERT INTO rdap.country_code VALUES (438,'LI');
	INSERT INTO rdap.country_code VALUES (440,'LT');
	INSERT INTO rdap.country_code VALUES (442,'LU');
	INSERT INTO rdap.country_code VALUES (807,'MK');
	INSERT INTO rdap.country_code VALUES (450,'MG');
	INSERT INTO rdap.country_code VALUES (454,'MW');
	INSERT INTO rdap.country_code VALUES (458,'MY');
	INSERT INTO rdap.country_code VALUES (462,'MV');
	INSERT INTO rdap.country_code VALUES (466,'ML');
	INSERT INTO rdap.country_code VALUES (470,'MT');
	INSERT INTO rdap.country_code VALUES (584,'MH');
	INSERT INTO rdap.country_code VALUES (474,'MQ');
	INSERT INTO rdap.country_code VALUES (478,'MR');
	INSERT INTO rdap.country_code VALUES (480,'MU');
	INSERT INTO rdap.country_code VALUES (175,'YT');
	INSERT INTO rdap.country_code VALUES (484,'MX');
	INSERT INTO rdap.country_code VALUES (583,'FM');
	INSERT INTO rdap.country_code VALUES (498,'MD');
	INSERT INTO rdap.country_code VALUES (492,'MC');
	INSERT INTO rdap.country_code VALUES (496,'MN');
	INSERT INTO rdap.country_code VALUES (499,'ME');
	INSERT INTO rdap.country_code VALUES (500,'MS');
	INSERT INTO rdap.country_code VALUES (504,'MA');
	INSERT INTO rdap.country_code VALUES (508,'MZ');
	INSERT INTO rdap.country_code VALUES (104,'MM');
	INSERT INTO rdap.country_code VALUES (516,'NA');
	INSERT INTO rdap.country_code VALUES (520,'NR');
	INSERT INTO rdap.country_code VALUES (524,'NP');
	INSERT INTO rdap.country_code VALUES (528,'NL');
	INSERT INTO rdap.country_code VALUES (530,'AN');
	INSERT INTO rdap.country_code VALUES (540,'NC');
	INSERT INTO rdap.country_code VALUES (554,'NZ');
	INSERT INTO rdap.country_code VALUES (558,'NI');
	INSERT INTO rdap.country_code VALUES (562,'NE');
	INSERT INTO rdap.country_code VALUES (566,'NG');
	INSERT INTO rdap.country_code VALUES (570,'NU');
	INSERT INTO rdap.country_code VALUES (574,'NF');
	INSERT INTO rdap.country_code VALUES (580,'MP');
	INSERT INTO rdap.country_code VALUES (578,'NO');
	INSERT INTO rdap.country_code VALUES (512,'OM');
	INSERT INTO rdap.country_code VALUES (586,'PK');
	INSERT INTO rdap.country_code VALUES (585,'PW');
	INSERT INTO rdap.country_code VALUES (275,'PS');
	INSERT INTO rdap.country_code VALUES (591,'PA');
	INSERT INTO rdap.country_code VALUES (598,'PG');
	INSERT INTO rdap.country_code VALUES (600,'PY');
	INSERT INTO rdap.country_code VALUES (604,'PE');
	INSERT INTO rdap.country_code VALUES (608,'PH');
	INSERT INTO rdap.country_code VALUES (612,'PN');
	INSERT INTO rdap.country_code VALUES (616,'PL');
	INSERT INTO rdap.country_code VALUES (620,'PT');
	INSERT INTO rdap.country_code VALUES (630,'PR');
	INSERT INTO rdap.country_code VALUES (634,'QA');
	INSERT INTO rdap.country_code VALUES (638,'RE');
	INSERT INTO rdap.country_code VALUES (642,'RO');
	INSERT INTO rdap.country_code VALUES (643,'RU');
	INSERT INTO rdap.country_code VALUES (646,'RW');
	INSERT INTO rdap.country_code VALUES (652,'BL');
	INSERT INTO rdap.country_code VALUES (654,'SH');
	INSERT INTO rdap.country_code VALUES (659,'KN');
	INSERT INTO rdap.country_code VALUES (662,'LC');
	INSERT INTO rdap.country_code VALUES (663,'MF');
	INSERT INTO rdap.country_code VALUES (666,'PM');
	INSERT INTO rdap.country_code VALUES (670,'VC');
	INSERT INTO rdap.country_code VALUES (882,'WS');
	INSERT INTO rdap.country_code VALUES (674,'SM');
	INSERT INTO rdap.country_code VALUES (678,'ST');
	INSERT INTO rdap.country_code VALUES (682,'SA');
	INSERT INTO rdap.country_code VALUES (686,'SN');
	INSERT INTO rdap.country_code VALUES (688,'RS');
	INSERT INTO rdap.country_code VALUES (690,'SC');
	INSERT INTO rdap.country_code VALUES (694,'SL');
	INSERT INTO rdap.country_code VALUES (702,'SG');
	INSERT INTO rdap.country_code VALUES (703,'SK');
	INSERT INTO rdap.country_code VALUES (705,'SI');
	INSERT INTO rdap.country_code VALUES (90,'SB');
	INSERT INTO rdap.country_code VALUES (706,'SO');
	INSERT INTO rdap.country_code VALUES (710,'ZA');
	INSERT INTO rdap.country_code VALUES (239,'GS');
	INSERT INTO rdap.country_code VALUES (728,'SS');
	INSERT INTO rdap.country_code VALUES (724,'ES');
	INSERT INTO rdap.country_code VALUES (144,'LK');
	INSERT INTO rdap.country_code VALUES (736,'SD');
	INSERT INTO rdap.country_code VALUES (740,'SR');
	INSERT INTO rdap.country_code VALUES (744,'SJ');
	INSERT INTO rdap.country_code VALUES (748,'SZ');
	INSERT INTO rdap.country_code VALUES (752,'SE');
	INSERT INTO rdap.country_code VALUES (756,'CH');
	INSERT INTO rdap.country_code VALUES (760,'SY');
	INSERT INTO rdap.country_code VALUES (158,'TW');
	INSERT INTO rdap.country_code VALUES (762,'TJ');
	INSERT INTO rdap.country_code VALUES (834,'TZ');
	INSERT INTO rdap.country_code VALUES (764,'TH');
	INSERT INTO rdap.country_code VALUES (626,'TL');
	INSERT INTO rdap.country_code VALUES (768,'TG');
	INSERT INTO rdap.country_code VALUES (772,'TK');
	INSERT INTO rdap.country_code VALUES (776,'TO');
	INSERT INTO rdap.country_code VALUES (780,'TT');
	INSERT INTO rdap.country_code VALUES (788,'TN');
	INSERT INTO rdap.country_code VALUES (792,'TR');
	INSERT INTO rdap.country_code VALUES (795,'TM');
	INSERT INTO rdap.country_code VALUES (796,'TC');
	INSERT INTO rdap.country_code VALUES (798,'TV');
	INSERT INTO rdap.country_code VALUES (800,'UG');
	INSERT INTO rdap.country_code VALUES (804,'UA');
	INSERT INTO rdap.country_code VALUES (784,'AE');
	INSERT INTO rdap.country_code VALUES (826,'GB');
	INSERT INTO rdap.country_code VALUES (840,'US');
	INSERT INTO rdap.country_code VALUES (581,'UM');
	INSERT INTO rdap.country_code VALUES (858,'UY');
	INSERT INTO rdap.country_code VALUES (860,'UZ');
	INSERT INTO rdap.country_code VALUES (548,'VU');
	INSERT INTO rdap.country_code VALUES (862,'VE');
	INSERT INTO rdap.country_code VALUES (704,'VN');
	INSERT INTO rdap.country_code VALUES (850,'VI');
	INSERT INTO rdap.country_code VALUES (876,'WF');
	INSERT INTO rdap.country_code VALUES (732,'EH');
	INSERT INTO rdap.country_code VALUES (887,'YE');
	INSERT INTO rdap.country_code VALUES (894,'ZM');
	INSERT INTO rdap.country_code VALUES (716,'ZW');

-- -----------------------------------------------------
-- Ip Version
-- -----------------------------------------------------
	INSERT INTO rdap.ip_version VALUES (4, 'v4');
	INSERT INTO rdap.ip_version VALUES (6, 'v6');

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
INSERT INTO rdap.domain VALUES(19,'1238','méxico','xn--mxico-bsa','whois.lat',4);
INSERT INTO rdap.domain VALUES(20,'xnxn','xn--elpjaroamarillo-pjb','elpájaroamarillo','whois.lat',4);

-- -----------------------------------------------------
-- Entities
-- -----------------------------------------------------
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

-- -----------------------------------------------------
-- VCard
-- -----------------------------------------------------
INSERT INTO rdap.vcard VALUES(1, 'Bill', 'Cheers and Os', 'cherryos.com', 'cherryos@bills.com', '87-89-25-46', '811-796-7719', '456-738-912', 'Lider en Liderazgo para Lideres');
INSERT INTO rdap.vcard VALUES(2, 'Billy', 'Dogs for Us', 'anothershopforpets.com', 'cherryos@billy.net', '87-89-25-46', '811-796-7719', null, null);
INSERT INTO rdap.vcard VALUES(3, 'Bob', 'Le yo que se', 'dunno.com', 'cherryos@bobagh.mx', '87-89-25-46', null, null, null);
INSERT INTO rdap.vcard VALUES(4, 'Barry', 'Naik', 'palomita.com', 'naik@barry.live', null, '812-452-8349', null, null);
INSERT INTO rdap.vcard VALUES(5, 'Wonka', 'Avidas', 'avidas.com', 'notcandys@wonka.ru', '87-89-25-46', null, null, null);

INSERT INTO rdap.vcard VALUES(6, 'Tristan', 'Skull Chocolate', null, null, null, null, null, null);
INSERT INTO rdap.vcard VALUES(7, 'Shane', 'Big Punch Gym', null, null, null, null, null, null);
INSERT INTO rdap.vcard VALUES(8, 'Layne', 'Cosas y Lacteos', null, null, null, null, null, null);
INSERT INTO rdap.vcard VALUES(9, 'Brittney', null, null, null, null, null, null, null);
INSERT INTO rdap.vcard VALUES(10, 'Blair', null, null, 'blair@hptmail.com', null, null, null, null);

INSERT INTO rdap.vcard VALUES(11, 'Gary', 'Crashman Demolitions', null, null, null, null, null, null);
INSERT INTO rdap.vcard VALUES(12, 'Gepetto', 'Red Plumber', null, null, null, null, null, null);
INSERT INTO rdap.vcard VALUES(13, 'Cindy', 'Blue Hedgehog', null, null, null, null, null, null);
INSERT INTO rdap.vcard VALUES(14, 'Roy', 'Roy Cubes', null, null, null, null, null, null);

-- -----------------------------------------------------
-- Vcard_Postal_Info
-- -----------------------------------------------------
INSERT INTO rdap.vcard_postal_info VALUES(1, 1, 'local', 'Mexico', 'Monterrey', 'Altavista', '100', 'Av Luis Elizondo', 'Nuevo Leon', '89420');
INSERT INTO rdap.vcard_postal_info VALUES(2, 3, 'local', 'Mexico', 'Monterrey', 'Altavista', '100', 'Av Luis Elizondo', 'Nuevo Leon', '89420');
INSERT INTO rdap.vcard_postal_info VALUES(3, 5, 'local', 'Mexico', 'Monterrey', 'Altavista', '100', 'Av Luis Elizondo', 'Nuevo Leon', '89420');
INSERT INTO rdap.vcard_postal_info VALUES(4, 7, 'local', 'Mexico', 'Monterrey', 'Altavista', '100', 'Av Luis Elizondo', 'Nuevo Leon', '89420');
INSERT INTO rdap.vcard_postal_info VALUES(5, 9, 'local', 'Mexico', 'Monterrey', 'Altavista', '100', 'Av Luis Elizondo', 'Nuevo Leon', '89420');
INSERT INTO rdap.vcard_postal_info VALUES(6, 11, 'local', 'Mexico', 'Monterrey', 'Altavista', '100', 'Av Luis Elizondo', 'Nuevo Leon', '89420');
INSERT INTO rdap.vcard_postal_info VALUES(6, 13, 'local', 'Mexico', 'Monterrey', 'Altavista', '100', 'Av Luis Elizondo', 'Nuevo Leon', '89420');
INSERT INTO rdap.vcard_postal_info VALUES(1, 14, 'local', 'Mexico', 'Monterrey', 'Altavista', '100', 'Av Luis Elizondo', 'Nuevo Leon', '89420');

INSERT INTO rdap.vcard_postal_info VALUES(2, 3, 'home', 'USA', 'New York', 'Altavista', '100', 'Av Luis Elizondo', null, '89430');
INSERT INTO rdap.vcard_postal_info VALUES(2, 3, 'home', 'England', 'London', 'Altavista', '100', 'Av Luis Elizondo', null, '89435');
INSERT INTO rdap.vcard_postal_info VALUES(2, 3, 'home', 'Rusia', 'Moscow', 'Altavista', '100', 'Av Luis Elizondo', null, '89445');
INSERT INTO rdap.vcard_postal_info VALUES(2, 3, 'home', 'Ucrania', 'Kiev', 'Altavista', '100', 'Av Luis Elizondo', null, '89450');

-- -----------------------------------------------------
-- Autonomous System Numbers
-- -----------------------------------------------------
INSERT INTO rdap.autonomous_system_number VALUES(1,'ASN1',1,10,'ASN-ONE-TEN','mx-type','whois.mx','484');
INSERT INTO rdap.autonomous_system_number VALUES(2,'ASN2',11,20,'ASN-TEN-TWENTY','mx-type','whois.mx','484');
INSERT INTO rdap.autonomous_system_number VALUES(3,'ASN3',21,30,'ASN-TWENTY-THIRTY','mx-type','whois.mx','484');
INSERT INTO rdap.autonomous_system_number VALUES(4,'ASN4',31,40,'ASN-THIRTY-FOURTY','mx-type','whois.mx','484');
INSERT INTO rdap.autonomous_system_number VALUES(5,'ASN5',41,50,'ASN-FOURTY-FIFTY','mx-type','whois.mx','484');

INSERT INTO rdap.autonomous_system_number VALUES(6,'ASN6',101,200,'ASN100','com-type','whois.com','826');
INSERT INTO rdap.autonomous_system_number VALUES(7,'ASN7',201,300,'ASN200','com-type','whois.com','826');
INSERT INTO rdap.autonomous_system_number VALUES(8,'ASN8',301,400,'ASN300','com-type','whois.com','826');
INSERT INTO rdap.autonomous_system_number VALUES(9,'ASN9',401,500,'ASN400','com-type','whois.com','826');
INSERT INTO rdap.autonomous_system_number VALUES(10,'ASN10',501,1000,'ASN500','com-type','whois.com','826');

-- -----------------------------------------------------
-- Nameservers
-- -----------------------------------------------------
INSERT INTO rdap.nameserver VALUES (1, 'NSE1', 'ns1.chopsuey.net', null, 'whois.net');
INSERT INTO rdap.nameserver VALUES (2, 'NSE2', 'ns2.chopsuey.net', null, 'whois.net');
INSERT INTO rdap.nameserver VALUES (3, 'NSE3', 'ns3.chopsuey.net', null, 'whois.net');
INSERT INTO rdap.nameserver VALUES (4, 'NSE4', 'ns4.chopsuey.net', null, 'whois.net');
INSERT INTO rdap.nameserver VALUES (5, 'NSE5', 'ns5.chopsuey.net', null, 'whois.net');

INSERT INTO rdap.nameserver VALUES (6, 'NSE6', 'ns1.white.cz', null, 'whois.cz');
INSERT INTO rdap.nameserver VALUES (7, 'NSE7', 'ns2.white.cz', null, 'whois.cz');
INSERT INTO rdap.nameserver VALUES (8, 'NSE8', 'ns3.white.cz', null, 'whois.cz');
INSERT INTO rdap.nameserver VALUES (9, 'NSE9', 'ns4.white.cz', null, 'whois.cz');
INSERT INTO rdap.nameserver VALUES (10,'NSE10', 'ns5.white.cz', null, 'whois.cz');

INSERT INTO rdap.nameserver VALUES (11, 'NSE11', 'ns1.bright.info', null, 'whois.info');
INSERT INTO rdap.nameserver VALUES (12, 'NSE12', 'ns2.bright.info', null, 'whois.info');
INSERT INTO rdap.nameserver VALUES (13, 'NSE13', 'ns3.bright.info', null, 'whois.info');
INSERT INTO rdap.nameserver VALUES (14, 'NSE14', 'ns4.bright.info', null, 'whois.info');
INSERT INTO rdap.nameserver VALUES (15, 'NSE15', 'ns5.bright.info', null, 'whois.info');

INSERT INTO rdap.nameserver VALUES (16, 'NSE16', 'ns1.camión.net', 'ns1.xn--camin-3ta.net', 'whois.mx');
INSERT INTO rdap.nameserver VALUES (17, 'NSE17', 'ns2.camión.net', 'ns2.xn--camin-3ta.net', 'whois.mx');
INSERT INTO rdap.nameserver VALUES (18, 'NSE18', 'ns3.camión.net', 'ns3.xn--camin-3ta.net', 'whois.mx');
INSERT INTO rdap.nameserver VALUES (19, 'NSE19', 'ns4.camión.net', 'ns4.xn--camin-3ta.net', 'whois.mx');
INSERT INTO rdap.nameserver VALUES (20, 'NSE20', 'ns5.camión.net', 'ns5.xn--camin-3ta.net', 'whois.mx');

-- -----------------------------------------------------
-- Remark Descriptions
-- --------------------------------------------------- --
INSERT INTO rdap.remark_description VALUES(1, 1, 'I AM NOT GOING TO STOP THE WHEEL.');
INSERT INTO rdap.remark_description VALUES(2, 1, 'I AM GOING TO BREAK THE WHEEL.');
INSERT INTO rdap.remark_description VALUES(1, 2, 'FOR THE WATCH');
INSERT INTO rdap.remark_description VALUES(2, 2, '*Stab*');
INSERT INTO rdap.remark_description VALUES(3, 2, 'FOR THE WATCH!');
INSERT INTO rdap.remark_description VALUES(4, 2, '*Stab* *Stab*');
INSERT INTO rdap.remark_description VALUES(1, 3, 'Good lords are dead.');
INSERT INTO rdap.remark_description VALUES(2, 3, 'The rest are monsters.');
INSERT INTO rdap.remark_description VALUES(1, 4, 'You know nothing Jon Snow');
INSERT INTO rdap.remark_description VALUES(1, 5, 'I AM GONNA HAVE TO EAT EVERY ***** CHICKEN IN THIS ROOM');
INSERT INTO rdap.remark_description VALUES(1, 6, 'Chaos is not a pit.');
INSERT INTO rdap.remark_description VALUES(2, 6, 'Chaos is a ladder.');
INSERT INTO rdap.remark_description VALUES(1, 7, 'Hey I just met you.');
INSERT INTO rdap.remark_description VALUES(2, 7, 'And this is crazy.');
INSERT INTO rdap.remark_description VALUES(3, 7, 'So here is my number.');
INSERT INTO rdap.remark_description VALUES(4, 7, 'So call me maybe?');
INSERT INTO rdap.remark_description VALUES(1, 8, 'Do not give up on your dreams');
INSERT INTO rdap.remark_description VALUES(2, 8, 'Keep sleeping');
INSERT INTO rdap.remark_description VALUES(1, 9, 'Always borrow money from a pessimist.');
INSERT INTO rdap.remark_description VALUES(2, 9, 'He won’t expect it back.');
INSERT INTO rdap.remark_description VALUES(1, 10, 'THE THINGS I DO FOR LOVE.');
INSERT INTO rdap.remark_description VALUES(1, 11, 'THE MAN WHO PASSES THE SENTENCE SHOULD SWING THE SWORD.');
INSERT INTO rdap.remark_description VALUES(1, 12, 'nope');
INSERT INTO rdap.remark_description VALUES(1, 13, 'yeap');

-- -----------------------------------------------------
-- Remark Descriptions
-- -----------------------------------------------------
INSERT INTO rdap.remark VALUES(1, 'Daenerys Targaryen','quote' , 'eng', );
INSERT INTO rdap.remark VALUES(2, 'Wait for it', null, 'eng', );
INSERT INTO rdap.remark VALUES(2, 'That must hurt.', null, 'eng', );
INSERT INTO rdap.remark VALUES(3, 'Also good politicians', null, 'eng', );
INSERT INTO rdap.remark VALUES(4, 'How naive.', null, 'eng', );
INSERT INTO rdap.remark VALUES(5, 'Poor guy.', null, 'eng', );
INSERT INTO rdap.remark VALUES(6, 'A pit comes handy sometimes you know...', null, 'eng', );
INSERT INTO rdap.remark VALUES(7, 'Sorry... had to do it.', null, 'eng', );
INSERT INTO rdap.remark VALUES(8, 'ZzZzZzZzZz...', null, 'eng', );
INSERT INTO rdap.remark VALUES(9, 'Sounds like a good plan.', null, 'eng', );
INSERT INTO rdap.remark VALUES(10, 'That is a bad excuse for pushing a kid from a tower', null, 'eng', );
INSERT INTO rdap.remark VALUES(11, 'RESPECT', null, 'eng', );
INSERT INTO rdap.remark VALUES(12, null, null, 'eng', );
INSERT INTO rdap.remark VALUES(13, null, null, 'eng', );




































--INSERT INTO rdap.ip_network values (1, 'INE3', null, 2013265920, null, 2013331455, 'ASD-LKJ-1', 'DIRECT ALLOCATION', 'whois.mx', 'pINE1', 16);
--INSERT INTO rdap.ip_network values (1, 'INE3', null, 2185974528, null, 2185974783, 'ASD-LKJ-1', 'DIRECT ALLOCATION', 'whois.mx', 'pINE1', 24);
--INSERT INTO rdap.ip_network values (1, 'INE3', null, 2083653760, null, 2083653887, 'ASD-LKJ-1', 'DIRECT ALLOCATION', 'whois.mx', 'pINE1', 25);
--INSERT INTO rdap.ip_network values (1, 'INE3', null, 2084964352, null, 2084964863, 'ASD-LKJ-1', 'DIRECT ALLOCATION', 'whois.mx', 'pINE1', 23);
--INSERT INTO rdap.ip_network values (1, 'INE3', null, 2085668864, null, 2085668871, 'ASD-LKJ-1', 'DIRECT ALLOCATION', 'whois.mx', 'pINE1', 29);


Commit;