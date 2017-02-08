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
INSERT INTO rdap.zone VALUES (2,'com.example');
INSERT INTO rdap.zone VALUES (3,'example');
INSERT INTO rdap.zone VALUES (4,'test');
INSERT INTO rdap.zone VALUES (5,'in-addr.arpa');

-- -----------------------------------------------------
-- Domains
-- -----------------------------------------------------
INSERT INTO rdap.domain(dom_id,dom_handle,dom_ldh_name,dom_unicode_name,dom_port43,zone_id) VALUES(1,'DOM1','whiterabbit',null,'whois.com',1);
INSERT INTO rdap.domain VALUES(2,'DOMCOM','goldfish',null,'whois.com',1);
INSERT INTO rdap.domain VALUES(3,'XXX2','reddog',null,'whois.com',1);
INSERT INTO rdap.domain VALUES(4,'1234','blackcat',null,'whois.com',1);
INSERT INTO rdap.domain VALUES(5,'ylb','yellowbird',null,'whois.com',1);

INSERT INTO rdap.domain VALUES(6,'DOM2','conejo_blanco',null,'whois.com.example',2);
INSERT INTO rdap.domain VALUES(7,'DOMCOMMX','pez_dorado',null,'whois.com.example',2);
INSERT INTO rdap.domain VALUES(8,'XXX3','perro_rojo',null,'whois.com.example',2);
INSERT INTO rdap.domain VALUES(9,'1235','gato_negro',null,'whois.com.example',2);
INSERT INTO rdap.domain VALUES(10,'pjra','pajaro_amarillo',null,'whois.com.example',2);

INSERT INTO rdap.domain VALUES(11,'DOM3','conejo_blanco',null,'whois.test',3);
INSERT INTO rdap.domain VALUES(12,'DOMMX','pez_dorado',null,'whois.test',3);
INSERT INTO rdap.domain VALUES(13,'XXX4','perro_rojo',null,'whois.test',3);
INSERT INTO rdap.domain VALUES(14,'1236','gato_negro',null,'whois.test',3);
INSERT INTO rdap.domain VALUES(15,'','pajaro_amarillo',null,'whois.test',3);

INSERT INTO rdap.domain VALUES(16,'DOM4','choco',null,'whois.net',4);
INSERT INTO rdap.domain VALUES(17,'DOMLAT','moka',null,'whois.net',4);
INSERT INTO rdap.domain VALUES(18,'XXX6','1.0.168.192',null,'whois.net',5);
INSERT INTO rdap.domain VALUES(19,'1238','méxico','xn--mxico-bsa','whois.net',4);
INSERT INTO rdap.domain VALUES(20,'xnxn','xn--elpjaroamarillo-pjb','elpájaroamarillo','whois.net',4);

-- -----------------------------------------------------
-- Variant
-- -----------------------------------------------------
INSERT INTO rdap.variant VALUES (1, 'idn table', 16);
INSERT INTO rdap.variant VALUES (2, 'idn table', 17);
INSERT INTO rdap.variant VALUES (3, 'idn table', 19);
INSERT INTO rdap.variant VALUES (4, 'idn table', 20);
INSERT INTO rdap.variant VALUES (5, 'idn table', 3);

-- -----------------------------------------------------
-- Variant Name
-- -----------------------------------------------------
INSERT INTO rdap.variant_name VALUES ('chocolate.test', 1);
INSERT INTO rdap.variant_name VALUES ('chocolateandcoffee.test', 2);
INSERT INTO rdap.variant_name VALUES ('mexico.test', 3);
INSERT INTO rdap.variant_name VALUES ('elpajaroamarillo.test', 4);
INSERT INTO rdap.variant_name VALUES ('rdapreddog.com', 5);

-- -----------------------------------------------------
-- Entities
-- -----------------------------------------------------
INSERT INTO rdap.entity VALUES(1,'mr_rabbit','whois.com');
INSERT INTO rdap.entity VALUES(2,'mr_fish','whois.com');
INSERT INTO rdap.entity VALUES(3,'mr_dog','whois.com');
INSERT INTO rdap.entity VALUES(4,'mr_cat','whois.com');
INSERT INTO rdap.entity VALUES(5,'mr_bird','whois.com');

INSERT INTO rdap.entity VALUES(6,'don_conejo','whois.example');
INSERT INTO rdap.entity VALUES(7,'don_pez','whois.example');
INSERT INTO rdap.entity VALUES(8,'don_perro','whois.example');
INSERT INTO rdap.entity VALUES(9,'don_gato','whois.example');
INSERT INTO rdap.entity VALUES(10,'don_pajaro','whois.example');

INSERT INTO rdap.entity VALUES(11,'sr_conejo','whois.com.example');
INSERT INTO rdap.entity VALUES(12,'sr_pez','whois.com.example');
INSERT INTO rdap.entity VALUES(13,'sr_perro','whois.com.example');
INSERT INTO rdap.entity VALUES(14,'sr_gato','whois.com.example');
INSERT INTO rdap.entity VALUES(15,'sr_pajaro','whois.com.example');

INSERT INTO rdap.entity VALUES(16,'cone','whois.test');
INSERT INTO rdap.entity VALUES(17,'pez','whois.test');
INSERT INTO rdap.entity VALUES(18,'perr','whois.test');
INSERT INTO rdap.entity VALUES(19,'gat','whois.test');
INSERT INTO rdap.entity VALUES(20,'paj','whois.test');

-- -----------------------------------------------------
-- VCard
-- -----------------------------------------------------
INSERT INTO rdap.vcard VALUES(1, 'Bill', 'Cheers and Os', 'cherryos.com', 'cherryos@bills.com', '87-89-25-46', '811-796-7719', '456-738-912', 'Lider en Liderazgo para Lideres');
INSERT INTO rdap.vcard VALUES(2, 'Billy', 'Dogs for Us', 'anothershopforpets.com', 'cherryos@billy.net', '87-89-25-46', '811-796-7719', null, null);
INSERT INTO rdap.vcard VALUES(3, 'Bob', 'Le yo que se', 'dunno.com', 'cherryos@bobagh.com', '87-89-25-46', null, null, null);
INSERT INTO rdap.vcard VALUES(4, 'Barry', 'Naik', 'palomita.com', 'naik@barry.com', null, '812-452-8349', null, null);
INSERT INTO rdap.vcard VALUES(5, 'Wonka', 'Avidas', 'avidas.com', 'notcandys@wonka.com', '87-89-25-46', null, null, null);

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
INSERT INTO rdap.vcard_postal_info VALUES(2, 3, 'local', 'Mexico', 'Monterrey', 'Altavista', '100', 'Av Luis Elizondo', 'Nuevo Leon', '89421');
INSERT INTO rdap.vcard_postal_info VALUES(3, 5, 'local', 'Mexico', 'Monterrey', 'Altavista', '100', 'Av Luis Elizondo', 'Nuevo Leon', '89422');
INSERT INTO rdap.vcard_postal_info VALUES(4, 7, 'local', 'Mexico', 'Monterrey', 'Altavista', '100', 'Av Luis Elizondo', 'Nuevo Leon', '89423');
INSERT INTO rdap.vcard_postal_info VALUES(5, 9, 'local', 'Mexico', 'Monterrey', 'Altavista', '100', 'Av Luis Elizondo', 'Nuevo Leon', '89424');
INSERT INTO rdap.vcard_postal_info VALUES(6, 11, 'local', 'Mexico', 'Monterrey', 'Altavista', '100', 'Av Luis Elizondo', 'Nuevo Leon', '89425');
INSERT INTO rdap.vcard_postal_info VALUES(7, 13, 'local', 'Mexico', 'Monterrey', 'Altavista', '100', 'Av Luis Elizondo', 'Nuevo Leon', '89426');
INSERT INTO rdap.vcard_postal_info VALUES(8, 14, 'local', 'Mexico', 'Monterrey', 'Altavista', '100', 'Av Luis Elizondo', 'Nuevo Leon', '89427');

INSERT INTO rdap.vcard_postal_info VALUES(9, 3, 'home', 'USA', 'New York', 'Altavista', '100', 'Av Luis Elizondo', null, '89430');
INSERT INTO rdap.vcard_postal_info VALUES(10, 3, 'home', 'England', 'London', 'Altavista', '100', 'Av Luis Elizondo', null, '89435');
INSERT INTO rdap.vcard_postal_info VALUES(11, 3, 'home', 'Rusia', 'Moscow', 'Altavista', '100', 'Av Luis Elizondo', null, '89445');
INSERT INTO rdap.vcard_postal_info VALUES(12, 3, 'home', 'Ucrania', 'Kiev', 'Altavista', '100', 'Av Luis Elizondo', null, '89450');

-- -----------------------------------------------------
-- Autonomous System Numbers
-- -----------------------------------------------------
--INSERT INTO rdap.autonomous_system_number VALUES(1,'ASN1',1,10,'ASN-ONE-TEN','mx-type','whois.test','484');
--INSERT INTO rdap.autonomous_system_number VALUES(2,'ASN2',11,20,'ASN-TEN-TWENTY','mx-type','whois.test','484');
--INSERT INTO rdap.autonomous_system_number VALUES(3,'ASN3',21,30,'ASN-TWENTY-THIRTY','mx-type','whois.test','484');
--INSERT INTO rdap.autonomous_system_number VALUES(4,'ASN4',31,40,'ASN-THIRTY-FOURTY','mx-type','whois.test','484');
--INSERT INTO rdap.autonomous_system_number VALUES(5,'ASN5',41,50,'ASN-FOURTY-FIFTY','mx-type','whois.test','484');

--INSERT INTO rdap.autonomous_system_number VALUES(6,'ASN6',101,200,'ASN100','com-type','whois.com','826');
--INSERT INTO rdap.autonomous_system_number VALUES(7,'ASN7',201,300,'ASN200','com-type','whois.com','826');
--INSERT INTO rdap.autonomous_system_number VALUES(8,'ASN8',301,400,'ASN300','com-type','whois.com','826');
--INSERT INTO rdap.autonomous_system_number VALUES(9,'ASN9',401,500,'ASN400','com-type','whois.com','826');
--INSERT INTO rdap.autonomous_system_number VALUES(10,'ASN10',501,1000,'ASN500','com-type','whois.com','826');

-- -----------------------------------------------------
-- Nameservers
-- -----------------------------------------------------
INSERT INTO rdap.nameserver VALUES (1, 'NSE1', 'ns1.chopsuey.net', null, 'whois.net');
INSERT INTO rdap.nameserver VALUES (2, 'NSE2', 'ns2.chopsuey.net', null, 'whois.net');
INSERT INTO rdap.nameserver VALUES (3, 'NSE3', 'ns3.chopsuey.net', null, 'whois.net');
INSERT INTO rdap.nameserver VALUES (4, 'NSE4', 'ns4.chopsuey.net', null, 'whois.net');
INSERT INTO rdap.nameserver VALUES (5, 'NSE5', 'ns5.chopsuey.net', null, 'whois.net');

INSERT INTO rdap.nameserver VALUES (6, 'NSE6', 'ns1.white.cz', null, 'whois.example');
INSERT INTO rdap.nameserver VALUES (7, 'NSE7', 'ns2.white.cz', null, 'whois.example');
INSERT INTO rdap.nameserver VALUES (8, 'NSE8', 'ns3.white.cz', null, 'whois.example');
INSERT INTO rdap.nameserver VALUES (9, 'NSE9', 'ns4.white.cz', null, 'whois.example');
INSERT INTO rdap.nameserver VALUES (10,'NSE10', 'ns5.white.cz', null, 'whois.example');

INSERT INTO rdap.nameserver VALUES (11, 'NSE11', 'ns1.bright.info', null, 'whois.info');
INSERT INTO rdap.nameserver VALUES (12, 'NSE12', 'ns2.bright.info', null, 'whois.info');
INSERT INTO rdap.nameserver VALUES (13, 'NSE13', 'ns3.bright.info', null, 'whois.info');
INSERT INTO rdap.nameserver VALUES (14, 'NSE14', 'ns4.bright.info', null, 'whois.info');
INSERT INTO rdap.nameserver VALUES (15, 'NSE15', 'ns5.bright.info', null, 'whois.info');

INSERT INTO rdap.nameserver VALUES (16, 'NSE16', 'ns1.camión.net', 'ns1.xn--camin-3ta.net', 'whois.test');
INSERT INTO rdap.nameserver VALUES (17, 'NSE17', 'ns2.camión.net', 'ns2.xn--camin-3ta.net', 'whois.test');
INSERT INTO rdap.nameserver VALUES (18, 'NSE18', 'ns3.camión.net', 'ns3.xn--camin-3ta.net', 'whois.test');
INSERT INTO rdap.nameserver VALUES (19, 'NSE19', 'ns4.camión.net', 'ns4.xn--camin-3ta.net', 'whois.test');
INSERT INTO rdap.nameserver VALUES (20, 'NSE20', 'ns5.camión.net', 'ns5.xn--camin-3ta.net', 'whois.test');

-- -----------------------------------------------------
-- Remark 
-- -----------------------------------------------------
INSERT INTO rdap.remark VALUES(1, 'Daenerys Targaryen','quote' , 'eng', );
INSERT INTO rdap.remark VALUES(2, 'Wait for it', null, 'eng', );
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

-- -----------------------------------------------------
-- Remark Descriptions
-- -----------------------------------------------------
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
-- Events
-- -----------------------------------------------------
INSERT INTO rdap.event VALUES(1, 1, 'XXXXX', '2017-1-02');
INSERT INTO rdap.event VALUES(2, 2, 'XXXXX', '2017-2-04');
INSERT INTO rdap.event VALUES(3, 3, 'XXXXX', '2017-3-06');
INSERT INTO rdap.event VALUES(4, 4, 'XXXXX', '2017-4-08');
INSERT INTO rdap.event VALUES(5, 5, 'XXXXX', '2017-5-10');

INSERT INTO rdap.event VALUES(6, 6, 'XXXXX', '2017-6-12');
INSERT INTO rdap.event VALUES(7, 7, 'XXXXX', '2017-7-14');
INSERT INTO rdap.event VALUES(8, 8, 'XXXXX', '2017-8-16');
INSERT INTO rdap.event VALUES(9, 9, 'XXXXX', '2017-9-18');
INSERT INTO rdap.event VALUES(10, 10, 'XXXXX', '2017-10-20');

INSERT INTO rdap.event VALUES(11, 11, 'XXXXX', '2017-11-22');
INSERT INTO rdap.event VALUES(12, 12, 'XXXXX', '2017-12-24');
INSERT INTO rdap.event VALUES(13, 1, 'XXXXX', '2017-1-26');
INSERT INTO rdap.event VALUES(14, 2, 'XXXXX', '2017-2-28');
INSERT INTO rdap.event VALUES(15, 3, 'XXXXX', '2017-3-30');

-- -----------------------------------------------------
-- Ip Address
-- -----------------------------------------------------
INSERT INTO rdap.ip_address VALUES(1, 1, 4, 2013265920);
INSERT INTO rdap.ip_address VALUES(2, 2, 4, 2013331455);
INSERT INTO rdap.ip_address VALUES(3, 3, 4, 2185974528);
INSERT INTO rdap.ip_address VALUES(4, 4, 4, 2185974783);
INSERT INTO rdap.ip_address VALUES(5, 5, 4, 2083653760);

INSERT INTO rdap.ip_address VALUES(6, 6, 4, 2083653887);
INSERT INTO rdap.ip_address VALUES(7, 7, 4, 2084964352);
INSERT INTO rdap.ip_address VALUES(8, 8, 4, 2084964863);
INSERT INTO rdap.ip_address VALUES(9, 9, 4, 2085668864);
INSERT INTO rdap.ip_address VALUES(10, 10, 4, 2085668871);

-- -----------------------------------------------------
-- Link
-- -----------------------------------------------------
INSERT INTO rdap.link VALUES (1, 'http://example.net', 'self', 'http://example.net', 'en', 'Link title', 'screen', 'text/html');
INSERT INTO rdap.link VALUES (2, 'http://example.net/event', 'self', 'http://example.net/event', 'en', 'Link to event', 'screen', 'application/rdap+json');
INSERT INTO rdap.link VALUES (3, 'http://example.net/domain', 'self', 'http://example.net/domain', 'en', 'Link to domain', 'screen', 'application/rdap+json');
INSERT INTO rdap.link VALUES (4, 'http://example.net/remark', 'self', 'http://example.net/remark', 'en', 'Link to remark', 'screen', 'application/rdap+json');
INSERT INTO rdap.link VALUES (5, 'http://example.net/nameserver', 'self', 'http://example.net/nameserver', 'en', 'Link to nameserver', 'screen', 'application/rdap+json');
INSERT INTO rdap.link VALUES (6, 'http://example.net/asn', 'self', 'http://example.net/asn', 'en', 'Link to asn', 'screen', 'application/rdap+json');
INSERT INTO rdap.link VALUES (7, 'http://example.net/ds', 'self', 'http://example.net/ds', 'en', 'Link to ds data', 'screen', 'application/rdap+json');
INSERT INTO rdap.link VALUES (8, 'http://example.net/entity', 'self', 'http://example.net/entity', 'en', 'Link to entity', 'screen', 'application/rdap+json');

-- -----------------------------------------------------
-- Public Id
-- -----------------------------------------------------
INSERT INTO rdap.public_id VALUES(1, 'IANA registrar ID', '1');
INSERT INTO rdap.public_id VALUES(2, 'IANA registrant ID', '2');
INSERT INTO rdap.public_id VALUES(3, 'IANA registry ID', '3');
INSERT INTO rdap.public_id VALUES(4, 'IANA domain ID', '4');
INSERT INTO rdap.public_id VALUES(5, 'Just some type of id', '5');

INSERT INTO rdap.public_id VALUES(6, 'IANA registrar ID', 'QDL-SAD-GF43');
INSERT INTO rdap.public_id VALUES(7, 'IANA registrant ID', 'IFGO-RE');
INSERT INTO rdap.public_id VALUES(8, 'IANA registry ID', 'REW8-DFT5');
INSERT INTO rdap.public_id VALUES(9, 'IANA domain ID', '6E46-4RE5-R45E');
INSERT INTO rdap.public_id VALUES(10, 'Just some type of id', 'QWG-5486-TG');

-- -----------------------------------------------------
-- Secure DNS
-- -----------------------------------------------------
INSERT INTO rdap.secure_dns VALUES(1, true, false, 604800, 1);
INSERT INTO rdap.secure_dns VALUES(2, true, false, 604900, 2);
INSERT INTO rdap.secure_dns VALUES(3, true, false, 605000, 3);
INSERT INTO rdap.secure_dns VALUES(4, true, true, 605800, 4);
INSERT INTO rdap.secure_dns VALUES(5, true, true, 605400, 5);

INSERT INTO rdap.secure_dns VALUES(6, true, true, null, 6);
INSERT INTO rdap.secure_dns VALUES(7, true, true, null, 7);
INSERT INTO rdap.secure_dns VALUES(8, false, true, null, 8);
INSERT INTO rdap.secure_dns VALUES(9, false, true, null, 9);
INSERT INTO rdap.secure_dns VALUES(10, false, true, null, 10);

-- -----------------------------------------------------
-- Ds Data
-- -----------------------------------------------------
INSERT INTO rdap.ds_data VALUES(1, 1, 12345, 3, '49FD46E6C4B45C55D4AC', 1);
INSERT INTO rdap.ds_data VALUES(2, 2, 12345, 3, 'CE56984804C8E0F5D4AC', 1);
INSERT INTO rdap.ds_data VALUES(3, 3, 12345, 3, '24EE8208FA711603326C', 1);
INSERT INTO rdap.ds_data VALUES(4, 4, 12345, 3, '5D75FC0E693F45D3496C', 1);
INSERT INTO rdap.ds_data VALUES(5, 5, 12345, 3, '3PSSSTNOTANEASTEREGG', 1);
INSERT INTO rdap.ds_data VALUES(6, 6, 12345, 3, 'A9649A1B015ACE676C43', 1);
INSERT INTO rdap.ds_data VALUES(7, 7, 12345, 3, '1AB52C2523B12519D9AE', 1);

-- -----------------------------------------------------
-- Domain Nameservers
-- -----------------------------------------------------
INSERT INTO rdap.domain_nameservers VALUES(1,1);
INSERT INTO rdap.domain_nameservers VALUES(2,2);
INSERT INTO rdap.domain_nameservers VALUES(3,3);
INSERT INTO rdap.domain_nameservers VALUES(4,4);
INSERT INTO rdap.domain_nameservers VALUES(5,5);

INSERT INTO rdap.domain_nameservers VALUES(6,6);
INSERT INTO rdap.domain_nameservers VALUES(7,7);
INSERT INTO rdap.domain_nameservers VALUES(8,8);
INSERT INTO rdap.domain_nameservers VALUES(9,9);
INSERT INTO rdap.domain_nameservers VALUES(10,10);

INSERT INTO rdap.domain_nameservers VALUES(11,11);
INSERT INTO rdap.domain_nameservers VALUES(12,12);
INSERT INTO rdap.domain_nameservers VALUES(13,13);
INSERT INTO rdap.domain_nameservers VALUES(14,14);
INSERT INTO rdap.domain_nameservers VALUES(15,15);

INSERT INTO rdap.domain_nameservers VALUES(16,16);
INSERT INTO rdap.domain_nameservers VALUES(17,17);
INSERT INTO rdap.domain_nameservers VALUES(18,18);
INSERT INTO rdap.domain_nameservers VALUES(19,19);
INSERT INTO rdap.domain_nameservers VALUES(20,20);

INSERT INTO rdap.domain_nameservers VALUES(1,20);
INSERT INTO rdap.domain_nameservers VALUES(2,19);
INSERT INTO rdap.domain_nameservers VALUES(3,18);
INSERT INTO rdap.domain_nameservers VALUES(4,17);
INSERT INTO rdap.domain_nameservers VALUES(5,16);

-- -----------------------------------------------------
-- Entity Contact (vcard)
-- -----------------------------------------------------
INSERT INTO rdap.entity_contact VALUES (1,1);
INSERT INTO rdap.entity_contact VALUES (2,2);
INSERT INTO rdap.entity_contact VALUES (3,3);
INSERT INTO rdap.entity_contact VALUES (4,4);
INSERT INTO rdap.entity_contact VALUES (5,5);

INSERT INTO rdap.entity_contact VALUES (6,6);
INSERT INTO rdap.entity_contact VALUES (7,7);
INSERT INTO rdap.entity_contact VALUES (8,8);
INSERT INTO rdap.entity_contact VALUES (9,9);
INSERT INTO rdap.entity_contact VALUES (10,10);

INSERT INTO rdap.entity_contact VALUES (1,11);
INSERT INTO rdap.entity_contact VALUES (2,12);
INSERT INTO rdap.entity_contact VALUES (3,13);
INSERT INTO rdap.entity_contact VALUES (4,14);
INSERT INTO rdap.entity_contact VALUES (5,1);

-- -----------------------------------------------------
-- Variant Relation
-- -----------------------------------------------------
INSERT INTO rdap.variant_relation VALUES(1,1);
INSERT INTO rdap.variant_relation VALUES(2,2);
INSERT INTO rdap.variant_relation VALUES(3,3);
INSERT INTO rdap.variant_relation VALUES(4,4);
INSERT INTO rdap.variant_relation VALUES(5,5);
INSERT INTO rdap.variant_relation VALUES(1,4);
INSERT INTO rdap.variant_relation VALUES(2,5);

-- -----------------------------------------------------
-- Entity public Ids
-- -----------------------------------------------------
INSERT INTO rdap.entity_public_ids VALUES(1,1);
INSERT INTO rdap.entity_public_ids VALUES(2,2);
INSERT INTO rdap.entity_public_ids VALUES(3,3);
INSERT INTO rdap.entity_public_ids VALUES(4,4);
INSERT INTO rdap.entity_public_ids VALUES(5,5);
INSERT INTO rdap.entity_public_ids VALUES(6,6);
INSERT INTO rdap.entity_public_ids VALUES(7,7);
INSERT INTO rdap.entity_public_ids VALUES(8,8);
INSERT INTO rdap.entity_public_ids VALUES(9,9);
INSERT INTO rdap.entity_public_ids VALUES(10,10);

-- -----------------------------------------------------
-- Domain public Ids 
-- -----------------------------------------------------
INSERT INTO rdap.domain_public_ids VALUES(1,1);
INSERT INTO rdap.domain_public_ids VALUES(2,2);
INSERT INTO rdap.domain_public_ids VALUES(3,3);
INSERT INTO rdap.domain_public_ids VALUES(4,4);
INSERT INTO rdap.domain_public_ids VALUES(5,5);
INSERT INTO rdap.domain_public_ids VALUES(6,6);
INSERT INTO rdap.domain_public_ids VALUES(7,7);
INSERT INTO rdap.domain_public_ids VALUES(8,8);
INSERT INTO rdap.domain_public_ids VALUES(9,9);
INSERT INTO rdap.domain_public_ids VALUES(10,10);

-- -----------------------------------------------------
-- Domain Status
-- -----------------------------------------------------
INSERT INTO rdap.domain_status VALUES(1,1);
INSERT INTO rdap.domain_status VALUES(2,2);
INSERT INTO rdap.domain_status VALUES(3,3);
INSERT INTO rdap.domain_status VALUES(4,4);
INSERT INTO rdap.domain_status VALUES(5,5);
INSERT INTO rdap.domain_status VALUES(6,6);
INSERT INTO rdap.domain_status VALUES(7,7);
INSERT INTO rdap.domain_status VALUES(8,8);
INSERT INTO rdap.domain_status VALUES(9,9);
INSERT INTO rdap.domain_status VALUES(10,10);
INSERT INTO rdap.domain_status VALUES(11,11);
INSERT INTO rdap.domain_status VALUES(12,12);
INSERT INTO rdap.domain_status VALUES(13,13);
INSERT INTO rdap.domain_status VALUES(14,14);
INSERT INTO rdap.domain_status VALUES(15,15);
INSERT INTO rdap.domain_status VALUES(16,16);
INSERT INTO rdap.domain_status VALUES(17,17);
INSERT INTO rdap.domain_status VALUES(18,18);
INSERT INTO rdap.domain_status VALUES(19,1);
INSERT INTO rdap.domain_status VALUES(20,2);
INSERT INTO rdap.domain_status VALUES(16,1);
INSERT INTO rdap.domain_status VALUES(17,13);
INSERT INTO rdap.domain_status VALUES(18,17);
INSERT INTO rdap.domain_status VALUES(18,16);
INSERT INTO rdap.domain_status VALUES(18,15);

-- -----------------------------------------------------
-- Entity Status
-- -----------------------------------------------------
INSERT INTO rdap.entity_status VALUES(1,1);
INSERT INTO rdap.entity_status VALUES(2,2);
INSERT INTO rdap.entity_status VALUES(3,3);
INSERT INTO rdap.entity_status VALUES(4,4);
INSERT INTO rdap.entity_status VALUES(5,5);
INSERT INTO rdap.entity_status VALUES(6,6);
INSERT INTO rdap.entity_status VALUES(7,7);
INSERT INTO rdap.entity_status VALUES(8,8);
INSERT INTO rdap.entity_status VALUES(9,9);
INSERT INTO rdap.entity_status VALUES(10,10);
INSERT INTO rdap.entity_status VALUES(11,11);
INSERT INTO rdap.entity_status VALUES(12,12);
INSERT INTO rdap.entity_status VALUES(13,13);
INSERT INTO rdap.entity_status VALUES(14,14);
INSERT INTO rdap.entity_status VALUES(15,15);
INSERT INTO rdap.entity_status VALUES(16,16);
INSERT INTO rdap.entity_status VALUES(17,17);
INSERT INTO rdap.entity_status VALUES(18,18);
INSERT INTO rdap.entity_status VALUES(19,1);
INSERT INTO rdap.entity_status VALUES(20,2);
INSERT INTO rdap.entity_status VALUES(16,1);
INSERT INTO rdap.entity_status VALUES(17,13);
INSERT INTO rdap.entity_status VALUES(18,17);
INSERT INTO rdap.entity_status VALUES(18,16);
INSERT INTO rdap.entity_status VALUES(18,15);

-- -----------------------------------------------------
-- Nameserver Status
-- -----------------------------------------------------
INSERT INTO rdap.nameserver_status VALUES(1,1);
INSERT INTO rdap.nameserver_status VALUES(2,2);
INSERT INTO rdap.nameserver_status VALUES(3,3);
INSERT INTO rdap.nameserver_status VALUES(4,4);
INSERT INTO rdap.nameserver_status VALUES(5,5);
INSERT INTO rdap.nameserver_status VALUES(6,6);
INSERT INTO rdap.nameserver_status VALUES(7,7);
INSERT INTO rdap.nameserver_status VALUES(8,8);
INSERT INTO rdap.nameserver_status VALUES(9,9);
INSERT INTO rdap.nameserver_status VALUES(10,10);
INSERT INTO rdap.nameserver_status VALUES(11,11);
INSERT INTO rdap.nameserver_status VALUES(12,12);
INSERT INTO rdap.nameserver_status VALUES(13,13);
INSERT INTO rdap.nameserver_status VALUES(14,14);
INSERT INTO rdap.nameserver_status VALUES(15,15);
INSERT INTO rdap.nameserver_status VALUES(16,16);
INSERT INTO rdap.nameserver_status VALUES(17,17);
INSERT INTO rdap.nameserver_status VALUES(18,18);
INSERT INTO rdap.nameserver_status VALUES(19,1);
INSERT INTO rdap.nameserver_status VALUES(20,2);
INSERT INTO rdap.nameserver_status VALUES(16,1);
INSERT INTO rdap.nameserver_status VALUES(17,13);
INSERT INTO rdap.nameserver_status VALUES(18,17);
INSERT INTO rdap.nameserver_status VALUES(18,16);
INSERT INTO rdap.nameserver_status VALUES(18,15);

-- -----------------------------------------------------
-- Domain Entity Roles
-- -----------------------------------------------------
INSERT INTO rdap.domain_entity_roles VALUES(1,1,1);
INSERT INTO rdap.domain_entity_roles VALUES(2,2,2);
INSERT INTO rdap.domain_entity_roles VALUES(3,3,3);
INSERT INTO rdap.domain_entity_roles VALUES(4,4,4);
INSERT INTO rdap.domain_entity_roles VALUES(5,5,5);
INSERT INTO rdap.domain_entity_roles VALUES(6,6,6);
INSERT INTO rdap.domain_entity_roles VALUES(7,7,7);
INSERT INTO rdap.domain_entity_roles VALUES(8,8,8);
INSERT INTO rdap.domain_entity_roles VALUES(9,9,9);
INSERT INTO rdap.domain_entity_roles VALUES(10,10,10);
INSERT INTO rdap.domain_entity_roles VALUES(11,11,11);
INSERT INTO rdap.domain_entity_roles VALUES(1,4,4);
INSERT INTO rdap.domain_entity_roles VALUES(2,4,4);
INSERT INTO rdap.domain_entity_roles VALUES(12,1,1);
INSERT INTO rdap.domain_entity_roles VALUES(13,1,3);
INSERT INTO rdap.domain_entity_roles VALUES(14,1,7);
INSERT INTO rdap.domain_entity_roles VALUES(15,3,1);
INSERT INTO rdap.domain_entity_roles VALUES(16,5,1);
INSERT INTO rdap.domain_entity_roles VALUES(17,6,1);
INSERT INTO rdap.domain_entity_roles VALUES(18,6,1);

-- -----------------------------------------------------
-- Entity Entity Roles
-- -----------------------------------------------------
INSERT INTO rdap.entity_entity_roles VALUES(1,1,1);
INSERT INTO rdap.entity_entity_roles VALUES(2,2,2);
INSERT INTO rdap.entity_entity_roles VALUES(3,3,3);
INSERT INTO rdap.entity_entity_roles VALUES(4,4,4);
INSERT INTO rdap.entity_entity_roles VALUES(5,5,5);
INSERT INTO rdap.entity_entity_roles VALUES(6,6,6);
INSERT INTO rdap.entity_entity_roles VALUES(7,7,7);
INSERT INTO rdap.entity_entity_roles VALUES(8,8,8);
INSERT INTO rdap.entity_entity_roles VALUES(9,9,9);
INSERT INTO rdap.entity_entity_roles VALUES(10,10,10);
INSERT INTO rdap.entity_entity_roles VALUES(11,11,11);
INSERT INTO rdap.entity_entity_roles VALUES(1,4,4);
INSERT INTO rdap.entity_entity_roles VALUES(2,4,4);
INSERT INTO rdap.entity_entity_roles VALUES(12,1,1);
INSERT INTO rdap.entity_entity_roles VALUES(13,1,3);
INSERT INTO rdap.entity_entity_roles VALUES(14,1,7);
INSERT INTO rdap.entity_entity_roles VALUES(15,3,1);
INSERT INTO rdap.entity_entity_roles VALUES(16,5,1);
INSERT INTO rdap.entity_entity_roles VALUES(17,6,1);
INSERT INTO rdap.entity_entity_roles VALUES(18,6,1);

-- -----------------------------------------------------
-- Nameserver Entity Roles
-- -----------------------------------------------------
INSERT INTO rdap.nameserver_entity_roles VALUES(1,1,1);
INSERT INTO rdap.nameserver_entity_roles VALUES(2,2,2);
INSERT INTO rdap.nameserver_entity_roles VALUES(3,3,3);
INSERT INTO rdap.nameserver_entity_roles VALUES(4,4,4);
INSERT INTO rdap.nameserver_entity_roles VALUES(5,5,5);
INSERT INTO rdap.nameserver_entity_roles VALUES(6,6,6);
INSERT INTO rdap.nameserver_entity_roles VALUES(7,7,7);
INSERT INTO rdap.nameserver_entity_roles VALUES(8,8,8);
INSERT INTO rdap.nameserver_entity_roles VALUES(9,9,9);
INSERT INTO rdap.nameserver_entity_roles VALUES(10,10,10);
INSERT INTO rdap.nameserver_entity_roles VALUES(11,11,11);
INSERT INTO rdap.nameserver_entity_roles VALUES(1,4,4);
INSERT INTO rdap.nameserver_entity_roles VALUES(2,4,4);
INSERT INTO rdap.nameserver_entity_roles VALUES(12,1,1);
INSERT INTO rdap.nameserver_entity_roles VALUES(13,1,3);
INSERT INTO rdap.nameserver_entity_roles VALUES(14,1,7);
INSERT INTO rdap.nameserver_entity_roles VALUES(15,3,1);
INSERT INTO rdap.nameserver_entity_roles VALUES(16,5,1);
INSERT INTO rdap.nameserver_entity_roles VALUES(17,6,1);
INSERT INTO rdap.nameserver_entity_roles VALUES(18,6,1);

-- -----------------------------------------------------
-- Entity Remarks
-- -----------------------------------------------------
INSERT INTO rdap.entity_remarks VALUES(1,1);
INSERT INTO rdap.entity_remarks VALUES(2,2);
INSERT INTO rdap.entity_remarks VALUES(3,3);
INSERT INTO rdap.entity_remarks VALUES(4,4);
INSERT INTO rdap.entity_remarks VALUES(5,5);

INSERT INTO rdap.entity_remarks VALUES(6,6);
INSERT INTO rdap.entity_remarks VALUES(7,7);
INSERT INTO rdap.entity_remarks VALUES(8,8);
INSERT INTO rdap.entity_remarks VALUES(9,9);
INSERT INTO rdap.entity_remarks VALUES(10,10);

INSERT INTO rdap.entity_remarks VALUES(11,11);
INSERT INTO rdap.entity_remarks VALUES(12,12);
INSERT INTO rdap.entity_remarks VALUES(13,13);

-- -----------------------------------------------------
-- Domain Remarks
-- -----------------------------------------------------
INSERT INTO rdap.domain_remarks VALUES(1,1);
INSERT INTO rdap.domain_remarks VALUES(2,2);
INSERT INTO rdap.domain_remarks VALUES(3,3);
INSERT INTO rdap.domain_remarks VALUES(4,4);
INSERT INTO rdap.domain_remarks VALUES(5,5);

INSERT INTO rdap.domain_remarks VALUES(6,6);
INSERT INTO rdap.domain_remarks VALUES(7,7);
INSERT INTO rdap.domain_remarks VALUES(8,8);
INSERT INTO rdap.domain_remarks VALUES(9,9);
INSERT INTO rdap.domain_remarks VALUES(10,10);

INSERT INTO rdap.domain_remarks VALUES(11,11);
INSERT INTO rdap.domain_remarks VALUES(12,12);
INSERT INTO rdap.domain_remarks VALUES(13,13); 

-- -----------------------------------------------------
-- Nameserver Remarks
-- -----------------------------------------------------
INSERT INTO rdap.nameserver_remarks VALUES(1,1);
INSERT INTO rdap.nameserver_remarks VALUES(2,2);
INSERT INTO rdap.nameserver_remarks VALUES(3,3);
INSERT INTO rdap.nameserver_remarks VALUES(4,4);
INSERT INTO rdap.nameserver_remarks VALUES(5,5);

INSERT INTO rdap.nameserver_remarks VALUES(6,6);
INSERT INTO rdap.nameserver_remarks VALUES(7,7);
INSERT INTO rdap.nameserver_remarks VALUES(8,8);
INSERT INTO rdap.nameserver_remarks VALUES(9,9);
INSERT INTO rdap.nameserver_remarks VALUES(10,10);

INSERT INTO rdap.nameserver_remarks VALUES(11,11);
INSERT INTO rdap.nameserver_remarks VALUES(12,12);
INSERT INTO rdap.nameserver_remarks VALUES(13,13); 

-- -----------------------------------------------------
-- Domain Links
-- -----------------------------------------------------
INSERT INTO rdap.domain_links VALUES(1,1);
INSERT INTO rdap.domain_links VALUES(3,2);
INSERT INTO rdap.domain_links VALUES(5,3);
INSERT INTO rdap.domain_links VALUES(7,4);
INSERT INTO rdap.domain_links VALUES(9,5);

INSERT INTO rdap.domain_links VALUES(2,6);
INSERT INTO rdap.domain_links VALUES(4,7);
INSERT INTO rdap.domain_links VALUES(6,8);

-- -----------------------------------------------------
-- Nameserver Links
-- -----------------------------------------------------
INSERT INTO rdap.nameserver_links VALUES(1,1);
INSERT INTO rdap.nameserver_links VALUES(3,2);
INSERT INTO rdap.nameserver_links VALUES(5,3);
INSERT INTO rdap.nameserver_links VALUES(7,4);
INSERT INTO rdap.nameserver_links VALUES(9,5);

-- -----------------------------------------------------
-- Entity Links
-- -----------------------------------------------------
INSERT INTO rdap.entity_links VALUES(1,1);
INSERT INTO rdap.entity_links VALUES(3,2);
INSERT INTO rdap.entity_links VALUES(5,3);
INSERT INTO rdap.entity_links VALUES(7,4);
INSERT INTO rdap.entity_links VALUES(9,5);

-- -----------------------------------------------------
-- Remark Links
-- -----------------------------------------------------
INSERT INTO rdap.remark_links VALUES(1,1);
INSERT INTO rdap.remark_links VALUES(2,2);
INSERT INTO rdap.remark_links VALUES(3,3);
INSERT INTO rdap.remark_links VALUES(4,4);
INSERT INTO rdap.remark_links VALUES(5,5);

-- -----------------------------------------------------
-- Event Links
-- -----------------------------------------------------
INSERT INTO rdap.event_links VALUES(1,1);
INSERT INTO rdap.event_links VALUES(2,2);
INSERT INTO rdap.event_links VALUES(3,3);
INSERT INTO rdap.event_links VALUES(4,4);
INSERT INTO rdap.event_links VALUES(5,5);

INSERT INTO rdap.event_links VALUES(6,6);
INSERT INTO rdap.event_links VALUES(7,7);
INSERT INTO rdap.event_links VALUES(8,8);

-- -----------------------------------------------------
-- Ds Data Links
-- -----------------------------------------------------
INSERT INTO rdap.ds_links VALUES(1,1);
INSERT INTO rdap.ds_links VALUES(2,2);
INSERT INTO rdap.ds_links VALUES(3,3);
INSERT INTO rdap.ds_links VALUES(4,4);
INSERT INTO rdap.ds_links VALUES(5,5);

INSERT INTO rdap.ds_links VALUES(3,6);
INSERT INTO rdap.ds_links VALUES(4,7);
INSERT INTO rdap.ds_links VALUES(5,8);

-- -----------------------------------------------------
-- Entity Events
-- -----------------------------------------------------
INSERT INTO rdap.entity_events VALUES(1,1);
INSERT INTO rdap.entity_events VALUES(2,2);
INSERT INTO rdap.entity_events VALUES(3,3);
INSERT INTO rdap.entity_events VALUES(4,4);
INSERT INTO rdap.entity_events VALUES(5,5);

INSERT INTO rdap.entity_events VALUES(6,6);
INSERT INTO rdap.entity_events VALUES(7,7);
INSERT INTO rdap.entity_events VALUES(8,8);
INSERT INTO rdap.entity_events VALUES(9,9);
INSERT INTO rdap.entity_events VALUES(10,10);

INSERT INTO rdap.entity_events VALUES(1,11);
INSERT INTO rdap.entity_events VALUES(3,12);
INSERT INTO rdap.entity_events VALUES(5,13);
INSERT INTO rdap.entity_events VALUES(7,14);
INSERT INTO rdap.entity_events VALUES(9,15);

-- -----------------------------------------------------
-- Domain Events
-- -----------------------------------------------------
INSERT INTO rdap.domain_events VALUES(1,1);
INSERT INTO rdap.domain_events VALUES(2,2);
INSERT INTO rdap.domain_events VALUES(3,3);
INSERT INTO rdap.domain_events VALUES(4,4);
INSERT INTO rdap.domain_events VALUES(5,5);

INSERT INTO rdap.domain_events VALUES(6,6);
INSERT INTO rdap.domain_events VALUES(7,7);
INSERT INTO rdap.domain_events VALUES(8,8);
INSERT INTO rdap.domain_events VALUES(9,9);
INSERT INTO rdap.domain_events VALUES(10,10);

INSERT INTO rdap.domain_events VALUES(1,11);
INSERT INTO rdap.domain_events VALUES(3,12);
INSERT INTO rdap.domain_events VALUES(5,13);
INSERT INTO rdap.domain_events VALUES(7,14);
INSERT INTO rdap.domain_events VALUES(9,15);

-- -----------------------------------------------------
-- Nameserver Events
-- -----------------------------------------------------
INSERT INTO rdap.nameserver_events VALUES(1,1);
INSERT INTO rdap.nameserver_events VALUES(2,2);
INSERT INTO rdap.nameserver_events VALUES(3,3);
INSERT INTO rdap.nameserver_events VALUES(4,4);
INSERT INTO rdap.nameserver_events VALUES(5,5);

INSERT INTO rdap.nameserver_events VALUES(6,6);
INSERT INTO rdap.nameserver_events VALUES(7,7);
INSERT INTO rdap.nameserver_events VALUES(8,8);
INSERT INTO rdap.nameserver_events VALUES(9,9);
INSERT INTO rdap.nameserver_events VALUES(10,10);

INSERT INTO rdap.nameserver_events VALUES(1,11);
INSERT INTO rdap.nameserver_events VALUES(3,12);
INSERT INTO rdap.nameserver_events VALUES(5,13);
INSERT INTO rdap.nameserver_events VALUES(7,14);
INSERT INTO rdap.nameserver_events VALUES(9,15);

-- -----------------------------------------------------
-- Ds Data Events
-- -----------------------------------------------------
INSERT INTO rdap.ds_events VALUES(1,1);
INSERT INTO rdap.ds_events VALUES(2,3);
INSERT INTO rdap.ds_events VALUES(3,5);
INSERT INTO rdap.ds_events VALUES(4,7);
INSERT INTO rdap.ds_events VALUES(5,9);

-- -----------------------------------------------------
-- Rdap User
-- -----------------------------------------------------
INSERT INTO rdap.rdap_user VALUES(1, 'demo', 'demo', 10);

-- -----------------------------------------------------
-- Rdap User Role
-- -----------------------------------------------------
INSERT INTO rdap.rdap_user_role VALUES('demo', 'AUTHENTICATED');

Commit;