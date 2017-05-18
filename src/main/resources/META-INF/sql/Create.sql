
CREATE SCHEMA IF NOT EXISTS `rdap`;
-- -----------------------------------------------------
-- Table `rdap`.`entity`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`entity` (
  `ent_id` BIGINT NOT NULL AUTO_INCREMENT,
  `ent_handle` VARCHAR(100) NULL,
  `ent_port43` VARCHAR(254) NULL,
  PRIMARY KEY (`ent_id`),
  UNIQUE KEY `id_UNIQUE` (`ent_id`),
  UNIQUE KEY `handle_UNIQUE` (`ent_handle`),
  KEY `ent_handle_idx` (`ent_handle`)
) ENGINE=InnoDB AUTO_INCREMENT=1648 DEFAULT CHARSET=utf8;


-- -----------------------------------------------------
-- Table `rdap`.`vcard`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`vcard` (
  `vca_id` BIGINT NOT NULL AUTO_INCREMENT,
  `vca_name` VARCHAR(100) NULL,
  `vca_company_name` VARCHAR(255) NULL,
  `vca_company_url` VARCHAR(255) NULL,
  `vca_email` VARCHAR(200) NULL,
  `vca_voice` VARCHAR(50) NULL,
  `vca_cellphone` VARCHAR(50) NULL,
  `vca_fax` VARCHAR(50) NULL,
  `vca_job_title` VARCHAR(200) NULL,
  PRIMARY KEY (`vca_id`),
  UNIQUE INDEX `vca_id_UNIQUE_2` (`vca_id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`remark`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`remark` (
  `rem_id` BIGINT NOT NULL AUTO_INCREMENT,
  `rem_title` VARCHAR(255) NULL,
  `rem_type` VARCHAR(255) NULL,
  `rem_lang` VARCHAR(255) NULL,
  PRIMARY KEY (`rem_id`),
  UNIQUE INDEX `rem_id_UNIQUE_1` (`rem_id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`link`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`link` (
  `lin_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lin_value` varchar(45) DEFAULT NULL,
  `lin_rel` varchar(45) DEFAULT NULL,
  `lin_href` varchar(45) DEFAULT NULL,
  `lin_title` varchar(45) DEFAULT NULL,
  `lin_media` varchar(45) DEFAULT NULL,
  `lin_type` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`lin_id`),
  UNIQUE KEY `lin_id_UNIQUE` (`lin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=513 DEFAULT CHARSET=utf8;


-- -----------------------------------------------------
-- Table `rdap`.`event_action`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`event_action` (
  `eac_id` SMALLINT NOT NULL,
  `eac_name` VARCHAR(100) NULL,
  PRIMARY KEY (`eac_id`),
  UNIQUE INDEX `eac_id_UNIQUE` (`eac_id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`event` (
  `eve_id` BIGINT NOT NULL AUTO_INCREMENT,
  `eac_id` SMALLINT NOT NULL,
  `eve_actor` VARCHAR(45) NULL,
  `eve_date` VARCHAR(45) NULL,
  PRIMARY KEY (`eve_id`),
  UNIQUE INDEX `eve_id_UNIQUE_1` (`eve_id` ASC),
  INDEX `fk_event_event_action1_idx` (`eac_id` ASC),
  CONSTRAINT `fk_event_event_action1`
    FOREIGN KEY (`eac_id`)
    REFERENCES `rdap`.`event_action` (`eac_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`roles` (
  `rol_id` TINYINT NOT NULL,
  `rol_name` VARCHAR(100) NULL,
  PRIMARY KEY (`rol_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`zone`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`zone` (
  `zone_id` SMALLINT NOT NULL AUTO_INCREMENT,
  `zone_name` VARCHAR(254) NOT NULL,
  PRIMARY KEY (`zone_id`),
  UNIQUE INDEX `ZONE_ID_UNIQUE` (`zone_id` ASC),
  UNIQUE INDEX `zone_name_UNIQUE` (`zone_name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`domain`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`domain` (
  `dom_id` BIGINT NOT NULL AUTO_INCREMENT,
  `dom_handle` VARCHAR(255) NULL,
  `dom_ldh_name` VARCHAR(64) NULL,
  `dom_unicode_name` VARCHAR(255) NULL,
  `dom_port43` VARCHAR(254) NULL,
  `zone_id` SMALLINT NOT NULL,
  PRIMARY KEY (`dom_id`, `zone_id`),
  INDEX `fk_DOMAIN_ZONE1_idx` (`zone_id` ASC),
  UNIQUE INDEX `dom_id_UNIQUE` (`dom_id` ASC),
  UNIQUE INDEX `dom_handle_UNIQUE` (`dom_handle` ASC),
  CONSTRAINT `fk_DOMAIN_ZONE1`
    FOREIGN KEY (`zone_id`)
    REFERENCES `rdap`.`zone` (`zone_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`domain_entity_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`domain_entity_roles` (
  `dom_id` BIGINT NOT NULL,
  `ent_id` BIGINT NOT NULL,
  `rol_id` TINYINT NOT NULL,
  PRIMARY KEY (`dom_id`, `ent_id`, `rol_id`),
  INDEX `fk_entity_roles_roles1_idx_3` (`rol_id` ASC),
  INDEX `fk_entity_roles_entity1_idx_3` (`ent_id` ASC),
  INDEX `fk_domain_entity_roles_domain1_idx` (`dom_id` ASC),
  UNIQUE INDEX `ux_domain_role_unique` (`dom_id` ASC, `rol_id` ASC),
  CONSTRAINT `fk_entity_roles_roles1`
    FOREIGN KEY (`rol_id`)
    REFERENCES `rdap`.`roles` (`rol_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_entity_roles_entity1`
    FOREIGN KEY (`ent_id`)
    REFERENCES `rdap`.`entity` (`ent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_domain_entity_roles_domain1`
    FOREIGN KEY (`dom_id`)
    REFERENCES `rdap`.`domain` (`dom_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`remark_description`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`remark_description` (
  `rde_order` MEDIUMINT NOT NULL,
  `rem_id` BIGINT NOT NULL,
  `rde_description` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`rde_order`, `rem_id`),
  CONSTRAINT `fk_REMARK_DESCRIPTION_REMARK1`
    FOREIGN KEY (`rem_id`)
    REFERENCES `rdap`.`remark` (`rem_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`entity_remarks`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`entity_remarks` (
  `ent_id` BIGINT NOT NULL,
  `rem_id` BIGINT NOT NULL,
  PRIMARY KEY (`ent_id`, `rem_id`),
  INDEX `fk_entity_remarks_remark1_idx` (`rem_id` ASC),
  UNIQUE INDEX `rem_id_UNIQUE_2` (`rem_id` ASC),
  CONSTRAINT `fk_ENTITY_REMARKS_ENTITY1`
    FOREIGN KEY (`ent_id`)
    REFERENCES `rdap`.`entity` (`ent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_entity_remarks_remark1`
    FOREIGN KEY (`rem_id`)
    REFERENCES `rdap`.`remark` (`rem_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`status` (
  `sta_id` SMALLINT NOT NULL,
  `sta_name` VARCHAR(100) NULL,
  PRIMARY KEY (`sta_id`),
  UNIQUE INDEX `sta_id_UNIQUE` (`sta_id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`entity_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`entity_status` (
  `ent_id` BIGINT NOT NULL,
  `sta_id` SMALLINT NOT NULL,
  PRIMARY KEY (`ent_id`, `sta_id`),
  INDEX `fk_entity_status_status1_idx` (`sta_id` ASC),
  INDEX `fk_entity_status_entity1_idx` (`ent_id` ASC),
  CONSTRAINT `fk_entity_status_status1`
    FOREIGN KEY (`sta_id`)
    REFERENCES `rdap`.`status` (`sta_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_entity_status_entity1`
    FOREIGN KEY (`ent_id`)
    REFERENCES `rdap`.`entity` (`ent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`entity_events`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`entity_events` (
  `ent_id` BIGINT NOT NULL,
  `eve_id` BIGINT NOT NULL,
  PRIMARY KEY (`ent_id`, `eve_id`),
  INDEX `fk_entity_events_event1_idx` (`eve_id` ASC),
  UNIQUE INDEX `eve_id_UNIQUE_2` (`eve_id` ASC),
  CONSTRAINT `fk_ENTITY_EVENT_ENTITY1`
    FOREIGN KEY (`ent_id`)
    REFERENCES `rdap`.`entity` (`ent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_entity_events_event1`
    FOREIGN KEY (`eve_id`)
    REFERENCES `rdap`.`event` (`eve_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`entity_links`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`entity_links` (
  `ent_id` BIGINT NOT NULL,
  `lin_id` BIGINT NOT NULL,
  PRIMARY KEY (`ent_id`, `lin_id`),
  INDEX `fk_entity_links_link1_idx` (`lin_id` ASC),
  UNIQUE INDEX `lin_id_UNIQUE_1` (`lin_id` ASC),
  CONSTRAINT `fk_ENTITY_LINK_ENTITY1`
    FOREIGN KEY (`ent_id`)
    REFERENCES `rdap`.`entity` (`ent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_entity_links_link1`
    FOREIGN KEY (`lin_id`)
    REFERENCES `rdap`.`link` (`lin_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`country_code`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`country_code` (
  `ccd_id` SMALLINT UNSIGNED NOT NULL,
  `ccd_code` VARCHAR(2) NOT NULL,
  PRIMARY KEY (`ccd_id`),
  UNIQUE INDEX `ccd_id_UNIQUE` (`ccd_id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`autonomous_system_number`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`autonomous_system_number` (
  `asn_id` BIGINT NOT NULL AUTO_INCREMENT,
  `asn_handle` VARCHAR(100) NOT NULL,
  `asn_start_autnum` BIGINT NOT NULL,
  `asn_end_autnum` BIGINT NOT NULL,
  `asn_name` VARCHAR(200) NULL DEFAULT NULL,
  `asn_type` VARCHAR(200) NULL DEFAULT NULL,
  `asn_port43` VARCHAR(254) NULL DEFAULT NULL,
  `ccd_id` SMALLINT UNSIGNED NOT NULL,
  PRIMARY KEY (`asn_id`, `ccd_id`),
  UNIQUE INDEX `asn_handle_UNIQUE` (`asn_handle` ASC),
  UNIQUE INDEX `asn_id_UNIQUE` (`asn_id` ASC),
  INDEX `fk_autonomous_system_number_country_code1_idx` (`ccd_id` ASC),
  CONSTRAINT `fk_autonomous_system_number_country_code1`
    FOREIGN KEY (`ccd_id`)
    REFERENCES `rdap`.`country_code` (`ccd_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`asn_entity_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`asn_entity_roles` (
  `asn_id` BIGINT NOT NULL,
  `ent_id` BIGINT NOT NULL,
  `rol_id` TINYINT NOT NULL,
  PRIMARY KEY (`asn_id`, `ent_id`, `rol_id`),
  INDEX `fk_ENTITY_AUTNUMS_autonomous_system_number1_idx` (`asn_id` ASC),
  INDEX `fk_asn_entity_roles_roles1_idx` (`rol_id` ASC),
  CONSTRAINT `fk_ENTITY_AUTNUMS_ENTITY1`
    FOREIGN KEY (`ent_id`)
    REFERENCES `rdap`.`entity` (`ent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ENTITY_AUTNUMS_autonomous_system_number1`
    FOREIGN KEY (`asn_id`)
    REFERENCES `rdap`.`autonomous_system_number` (`asn_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_asn_entity_roles_roles1`
    FOREIGN KEY (`rol_id`)
    REFERENCES `rdap`.`roles` (`rol_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`nameserver`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`nameserver` (
  `nse_id` BIGINT NOT NULL AUTO_INCREMENT,
  `nse_handle` VARCHAR(100) NULL,
  `nse_ldh_name` VARCHAR(254) NULL DEFAULT NULL,
  `nse_unicode_name` VARCHAR(255) NULL,
  `nse_port43` VARCHAR(254) NULL DEFAULT NULL,
  PRIMARY KEY (`nse_id`),
  UNIQUE INDEX `nse_handle_UNIQUE` (`nse_handle` ASC),
  UNIQUE INDEX `nse_id_UNIQUE` (`nse_id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`domain_nameservers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`domain_nameservers` (
  `dom_id` BIGINT NOT NULL,
  `nse_id` BIGINT NOT NULL,
  PRIMARY KEY (`dom_id`, `nse_id`),
  INDEX `fk_DOMAIN_NAMESERVERS_nameserver1_idx` (`nse_id` ASC),
  CONSTRAINT `fk_DOMAIN_NAMESERVERS_DOMAIN1`
    FOREIGN KEY (`dom_id`)
    REFERENCES `rdap`.`domain` (`dom_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_DOMAIN_NAMESERVERS_nameserver1`
    FOREIGN KEY (`nse_id`)
    REFERENCES `rdap`.`nameserver` (`nse_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`secure_dns`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`secure_dns` (
  `sdns_id` BIGINT NOT NULL AUTO_INCREMENT,
  `sdns_zone_signed` TINYINT(1) NOT NULL,
  `sdns_delegation_signed` TINYINT(1) NOT NULL,
  `sdns_max_sig_life` INT NULL,
  `dom_id` BIGINT NOT NULL,
  PRIMARY KEY (`sdns_id`),
  INDEX `fk_SECURE_DNS_DOMAIN1_idx` (`dom_id` ASC),
  UNIQUE INDEX `sdns_id_UNIQUE` (`sdns_id` ASC),
  CONSTRAINT `fk_SECURE_DNS_DOMAIN1`
    FOREIGN KEY (`dom_id`)
    REFERENCES `rdap`.`domain` (`dom_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`ds_data`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`ds_data` (
  `dsd_id` BIGINT NOT NULL AUTO_INCREMENT,
  `sdns_id` BIGINT NOT NULL,
  `dsd_keytag` INT NOT NULL,
  `dsd_algorithm` INT NOT NULL,
  `dsd_digest` VARCHAR(255) NOT NULL,
  `dsd_digest_type` INT NOT NULL,
  PRIMARY KEY (`dsd_id`, `sdns_id`),
  INDEX `fk_ds_data_secure_dns1_idx` (`sdns_id` ASC),
  UNIQUE INDEX `dsd_id_UNIQUE` (`dsd_id` ASC),
  CONSTRAINT `fk_ds_data_secure_dns1`
    FOREIGN KEY (`sdns_id`)
    REFERENCES `rdap`.`secure_dns` (`sdns_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`domain_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`domain_status` (
  `dom_id` BIGINT NOT NULL,
  `sta_id` SMALLINT NOT NULL,
  PRIMARY KEY (`dom_id`, `sta_id`),
  INDEX `fk_domain_status_status1_idx` (`sta_id` ASC),
  CONSTRAINT `fk_DOMAIN_STATUS_DOMAIN1`
    FOREIGN KEY (`dom_id`)
    REFERENCES `rdap`.`domain` (`dom_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_domain_status_status1`
    FOREIGN KEY (`sta_id`)
    REFERENCES `rdap`.`status` (`sta_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`domain_remarks`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`domain_remarks` (
  `dom_id` BIGINT NOT NULL,
  `rem_id` BIGINT NOT NULL,
  PRIMARY KEY (`dom_id`, `rem_id`),
  INDEX `fk_domain_remarks_remark1_idx` (`rem_id` ASC),
  UNIQUE INDEX `rem_id_UNIQUE_3` (`rem_id` ASC),
  CONSTRAINT `fk_DOMAIN_REMARKS_DOMAIN1`
    FOREIGN KEY (`dom_id`)
    REFERENCES `rdap`.`domain` (`dom_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_domain_remarks_remark1`
    FOREIGN KEY (`rem_id`)
    REFERENCES `rdap`.`remark` (`rem_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`domain_links`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`domain_links` (
  `dom_id` BIGINT NOT NULL,
  `lin_id` BIGINT NOT NULL,
  PRIMARY KEY (`dom_id`, `lin_id`),
  INDEX `fk_domain_links_link1_idx` (`lin_id` ASC),
  UNIQUE INDEX `lin_id_UNIQUE_2` (`lin_id` ASC),
  CONSTRAINT `fk_DOMAIN_LINKS_DOMAIN1`
    FOREIGN KEY (`dom_id`)
    REFERENCES `rdap`.`domain` (`dom_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_domain_links_link1`
    FOREIGN KEY (`lin_id`)
    REFERENCES `rdap`.`link` (`lin_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`domain_events`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`domain_events` (
  `dom_id` BIGINT NOT NULL,
  `eve_id` BIGINT NOT NULL,
  PRIMARY KEY (`dom_id`, `eve_id`),
  INDEX `fk_domain_events_event1_idx` (`eve_id` ASC),
  UNIQUE INDEX `eve_id_UNIQUE_3` (`eve_id` ASC),
  CONSTRAINT `fk_DOMAIN_EVENTS_DOMAIN1`
    FOREIGN KEY (`dom_id`)
    REFERENCES `rdap`.`domain` (`dom_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_domain_events_event1`
    FOREIGN KEY (`eve_id`)
    REFERENCES `rdap`.`event` (`eve_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`ip_version`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`ip_version` (
  `ive_id` TINYINT UNSIGNED NOT NULL,
  `ive_name` VARCHAR(2) NOT NULL,
  PRIMARY KEY (`ive_id`),
  UNIQUE INDEX `ipv_name_UNIQUE` (`ive_name` ASC),
  UNIQUE INDEX `ipv_id_UNIQUE` (`ive_id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`ip_network`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`ip_network` (
  `ine_id` BIGINT  NOT NULL AUTO_INCREMENT,
  `ine_handle` VARCHAR(255) NOT NULL,
  `ine_start_address_up` BIGINT  NULL DEFAULT NULL,
  `ine_start_address_down` BIGINT  NULL DEFAULT NULL,
  `ine_end_address_up` BIGINT  NULL DEFAULT NULL,
  `ine_end_address_down` BIGINT  NULL DEFAULT NULL,
  `ine_name` VARCHAR(255) NULL DEFAULT NULL,
  `ine_type` VARCHAR(255) NULL DEFAULT NULL,
  `ine_port43` VARCHAR(254) NULL DEFAULT NULL,
  `ccd_id` SMALLINT  NOT NULL,
  `ip_version_id` TINYINT  NOT NULL,
  `ine_parent_handle` VARCHAR(255) NULL,
  `ine_cidr` SMALLINT NULL,
  PRIMARY KEY (`ine_id`, `ine_handle`),
  UNIQUE INDEX `ine_handle_UNIQUE` (`ine_handle` ASC),
  UNIQUE INDEX `ine_id_UNIQUE` (`ine_id` ASC),
  INDEX `fk_ip_network_country_code1_idx` (`ccd_id` ASC),
  INDEX `fk_ip_network_ip_version1_idx` (`ip_version_id` ASC),
  CONSTRAINT `fk_ip_network_country_code1`
    FOREIGN KEY (`ccd_id`)
    REFERENCES `rdap`.`country_code` (`ccd_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ip_network_ip_version1`
    FOREIGN KEY (`ip_version_id`)
    REFERENCES `rdap`.`ip_version` (`ive_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`domain_networks`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`domain_networks` (
  `dom_id` BIGINT NOT NULL,
  `ine_id` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`dom_id`, `ine_id`),
  INDEX `fk_DOMAIN_NETWORKS_ip_network1_idx` (`ine_id` ASC),
  CONSTRAINT `fk_DOMAIN_NETWORKS_DOMAIN1`
    FOREIGN KEY (`dom_id`)
    REFERENCES `rdap`.`domain` (`dom_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_DOMAIN_NETWORKS_ip_network1`
    FOREIGN KEY (`ine_id`)
    REFERENCES `rdap`.`ip_network` (`ine_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`ds_events`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`ds_events` (
  `dsd_id` BIGINT NOT NULL,
  `eve_id` BIGINT NOT NULL,
  PRIMARY KEY (`dsd_id`, `eve_id`),
  INDEX `fk_ds_events_event1_idx` (`eve_id` ASC),
  UNIQUE INDEX `event_eve_id_UNIQUE_4` (`eve_id` ASC),
  CONSTRAINT `fk_DS_EVENTS_DS_DATA1`
    FOREIGN KEY (`dsd_id`)
    REFERENCES `rdap`.`ds_data` (`dsd_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ds_events_event1`
    FOREIGN KEY (`eve_id`)
    REFERENCES `rdap`.`event` (`eve_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`ds_links`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`ds_links` (
  `dsd_id` BIGINT NOT NULL,
  `lin_id` BIGINT NOT NULL,
  PRIMARY KEY (`dsd_id`, `lin_id`),
  INDEX `fk_ds_links_link1_idx` (`lin_id` ASC),
  UNIQUE INDEX `lin_id_UNIQUE_3` (`lin_id` ASC),
  CONSTRAINT `fk_DS_LINKS_DS_DATA1`
    FOREIGN KEY (`dsd_id`)
    REFERENCES `rdap`.`ds_data` (`dsd_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ds_links_link1`
    FOREIGN KEY (`lin_id`)
    REFERENCES `rdap`.`link` (`lin_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`nameserver_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`nameserver_status` (
  `nse_id` BIGINT NOT NULL,
  `sta_id` SMALLINT NOT NULL,
  PRIMARY KEY (`nse_id`, `sta_id`),
  INDEX `fk_nameserver_status_status1_idx` (`sta_id` ASC),
  CONSTRAINT `fk_nameserver_status_nameserver`
    FOREIGN KEY (`nse_id`)
    REFERENCES `rdap`.`nameserver` (`nse_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_nameserver_status_status1`
    FOREIGN KEY (`sta_id`)
    REFERENCES `rdap`.`status` (`sta_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`ip_address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`ip_address` (
  `iad_id` INT NOT NULL AUTO_INCREMENT,
  `nse_id` BIGINT NOT NULL,
  `iad_type` TINYINT NOT NULL,
  `iad_value` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`iad_id`, `nse_id`),
  UNIQUE INDEX `iad_id_UNIQUE` (`iad_id` ASC),
  CONSTRAINT `fk_nameserver_ip_addresses_nameserver1`
    FOREIGN KEY (`nse_id`)
    REFERENCES `rdap`.`nameserver` (`nse_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`nameserver_remarks`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`nameserver_remarks` (
  `nse_id` BIGINT NOT NULL,
  `rem_id` BIGINT NOT NULL,
  PRIMARY KEY (`nse_id`, `rem_id`),
  INDEX `fk_nameserver_remarks_remark1_idx` (`rem_id` ASC),
  UNIQUE INDEX `rem_id_UNIQUE_4` (`rem_id` ASC),
  CONSTRAINT `fk_nameserver_entities_nameserver10`
    FOREIGN KEY (`nse_id`)
    REFERENCES `rdap`.`nameserver` (`nse_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_nameserver_remarks_remark1`
    FOREIGN KEY (`rem_id`)
    REFERENCES `rdap`.`remark` (`rem_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`nameserver_links`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`nameserver_links` (
  `nse_id` BIGINT NOT NULL,
  `lin_id` BIGINT NOT NULL,
  PRIMARY KEY (`nse_id`, `lin_id`),
  INDEX `fk_nameserver_links_link1_idx` (`lin_id` ASC),
  UNIQUE INDEX `lin_id_UNIQUE_4` (`lin_id` ASC),
  CONSTRAINT `fk_nameserver_entities_nameserver100`
    FOREIGN KEY (`nse_id`)
    REFERENCES `rdap`.`nameserver` (`nse_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_nameserver_links_link1`
    FOREIGN KEY (`lin_id`)
    REFERENCES `rdap`.`link` (`lin_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`asn_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`asn_status` (
  `asn_id` BIGINT NOT NULL,
  `sta_id` SMALLINT NOT NULL,
  PRIMARY KEY (`asn_id`, `sta_id`),
  INDEX `fk_asn_status_status1_idx` (`sta_id` ASC),
  CONSTRAINT `fk_asn_status_autonomous_system_number1`
    FOREIGN KEY (`asn_id`)
    REFERENCES `rdap`.`autonomous_system_number` (`asn_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_asn_status_status1`
    FOREIGN KEY (`sta_id`)
    REFERENCES `rdap`.`status` (`sta_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`asn_remarks`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`asn_remarks` (
  `asn_id` BIGINT NOT NULL,
  `rem_id` BIGINT NOT NULL,
  PRIMARY KEY (`asn_id`, `rem_id`),
  INDEX `fk_asn_remarks_remark1_idx` (`rem_id` ASC),
  UNIQUE INDEX `rem_id_UNIQUE_5` (`rem_id` ASC),
  CONSTRAINT `fk_asn_status_autonomous_system_number10`
    FOREIGN KEY (`asn_id`)
    REFERENCES `rdap`.`autonomous_system_number` (`asn_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_asn_remarks_remark1`
    FOREIGN KEY (`rem_id`)
    REFERENCES `rdap`.`remark` (`rem_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`asn_links`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`asn_links` (
  `asn_id` BIGINT NOT NULL,
  `lin_id` BIGINT NOT NULL,
  PRIMARY KEY (`asn_id`, `lin_id`),
  INDEX `fk_asn_links_link1_idx` (`lin_id` ASC),
  UNIQUE INDEX `lin_id_UNIQUE_5` (`lin_id` ASC),
  CONSTRAINT `fk_asn_status_autonomous_system_number12`
    FOREIGN KEY (`asn_id`)
    REFERENCES `rdap`.`autonomous_system_number` (`asn_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_asn_links_link1`
    FOREIGN KEY (`lin_id`)
    REFERENCES `rdap`.`link` (`lin_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`asn_events`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`asn_events` (
  `asn_id` BIGINT NOT NULL,
  `eve_id` BIGINT NOT NULL,
  PRIMARY KEY (`asn_id`, `eve_id`),
  INDEX `fk_asn_events_event1_idx` (`eve_id` ASC),
  UNIQUE INDEX `eve_id_UNIQUE_5` (`eve_id` ASC),
  CONSTRAINT `fk_asn_status_autonomous_system_number120`
    FOREIGN KEY (`asn_id`)
    REFERENCES `rdap`.`autonomous_system_number` (`asn_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_asn_events_event1`
    FOREIGN KEY (`eve_id`)
    REFERENCES `rdap`.`event` (`eve_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`ip_network_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`ip_network_status` (
  `ine_id` BIGINT UNSIGNED NOT NULL,
  `sta_id` SMALLINT NOT NULL,
  PRIMARY KEY (`ine_id`, `sta_id`),
  INDEX `fk_ip_network_status_status1_idx` (`sta_id` ASC),
  CONSTRAINT `fk_ip_network_status_ip_network1`
    FOREIGN KEY (`ine_id`)
    REFERENCES `rdap`.`ip_network` (`ine_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ip_network_status_status1`
    FOREIGN KEY (`sta_id`)
    REFERENCES `rdap`.`status` (`sta_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`ip_network_remarks`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`ip_network_remarks` (
  `ine_id` BIGINT UNSIGNED NOT NULL,
  `rem_id` BIGINT NOT NULL,
  PRIMARY KEY (`ine_id`, `rem_id`),
  INDEX `fk_ip_network_remarks_remark1_idx` (`rem_id` ASC),
  UNIQUE INDEX `remark_rem_id_UNIQUE_6` (`rem_id` ASC),
  CONSTRAINT `fk_ip_network_status_ip_network100`
    FOREIGN KEY (`ine_id`)
    REFERENCES `rdap`.`ip_network` (`ine_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ip_network_remarks_remark1`
    FOREIGN KEY (`rem_id`)
    REFERENCES `rdap`.`remark` (`rem_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`ip_network_links`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`ip_network_links` (
  `ine_id` BIGINT UNSIGNED NOT NULL,
  `lin_id` BIGINT NOT NULL,
  PRIMARY KEY (`ine_id`, `lin_id`),
  INDEX `fk_ip_network_links_link1_idx` (`lin_id` ASC),
  UNIQUE INDEX `lin_id_UNIQUE_6` (`lin_id` ASC),
  CONSTRAINT `fk_ip_network_status_ip_network101`
    FOREIGN KEY (`ine_id`)
    REFERENCES `rdap`.`ip_network` (`ine_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ip_network_links_link1`
    FOREIGN KEY (`lin_id`)
    REFERENCES `rdap`.`link` (`lin_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`ip_network_events`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`ip_network_events` (
  `ine_id` BIGINT UNSIGNED NOT NULL,
  `eve_id` BIGINT NOT NULL,
  PRIMARY KEY (`ine_id`, `eve_id`),
  INDEX `fk_ip_network_events_event1_idx` (`eve_id` ASC),
  UNIQUE INDEX `event_eve_id_UNIQUE_6` (`eve_id` ASC),
  CONSTRAINT `fk_ip_network_status_ip_network102`
    FOREIGN KEY (`ine_id`)
    REFERENCES `rdap`.`ip_network` (`ine_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ip_network_events_event1`
    FOREIGN KEY (`eve_id`)
    REFERENCES `rdap`.`event` (`eve_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`variant`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`variant` (
  `var_id` BIGINT NOT NULL AUTO_INCREMENT,
  `var_idn_table` VARCHAR(100) NULL,
  `dom_id` BIGINT NOT NULL,
  PRIMARY KEY (`var_id`, `dom_id`),
  INDEX `fk_variant_domain1_idx` (`dom_id` ASC),
  UNIQUE INDEX `var_id_UNIQUE` (`var_id` ASC),
  CONSTRAINT `fk_variant_domain1`
    FOREIGN KEY (`dom_id`)
    REFERENCES `rdap`.`domain` (`dom_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`relation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`relation` (
  `rel_id` TINYINT NOT NULL,
  `rel_type` VARCHAR(255) NULL,
  PRIMARY KEY (`rel_id`),
  UNIQUE INDEX `rel_id_UNIQUE` (`rel_id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`variant_name`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`variant_name` (
  `vna_ldh_name` VARCHAR(63) NULL,
  `var_id` BIGINT NOT NULL,
  UNIQUE INDEX `vna_ldh_name_UNIQUE` (`vna_ldh_name` ASC),
  CONSTRAINT `fk_VARIANT_NAME_VARIANT1`
    FOREIGN KEY (`var_id`)
    REFERENCES `rdap`.`variant` (`var_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`vcard_postal_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`vcard_postal_info` (
  `vpi_id` BIGINT NOT NULL AUTO_INCREMENT,
  `vca_id` BIGINT NOT NULL,
  `vpi_type` VARCHAR(45) NULL,
  `vpi_country` VARCHAR(100) NULL,
  `vpi_city` VARCHAR(100) NULL,
  `vpi_street1` VARCHAR(100) NULL,
  `vpi_street2` VARCHAR(100) NULL,
  `vpi_street3` VARCHAR(100) NULL,
  `vpi_state` VARCHAR(100) NULL,
  `vpi_postal_code` VARCHAR(100) NULL,
  PRIMARY KEY (`vpi_id`, `vca_id`),
  UNIQUE INDEX `vca_id_UNIQUE_1` (`vpi_id` ASC),
  INDEX `fk_vcard_postal_info_vcard1_idx` (`vca_id` ASC),
  CONSTRAINT `fk_vcard_postal_info_vcard1`
    FOREIGN KEY (`vca_id`)
    REFERENCES `rdap`.`vcard` (`vca_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`nameserver_events`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`nameserver_events` (
  `nse_id` BIGINT NOT NULL,
  `eve_id` BIGINT NOT NULL,
  PRIMARY KEY (`nse_id`, `eve_id`),
  INDEX `fk_nameserver_events_event1_idx` (`eve_id` ASC),
  UNIQUE INDEX `eve_id_UNIQUE_7` (`eve_id` ASC),
  CONSTRAINT `fk_nameserver_events_nameserver1`
    FOREIGN KEY (`nse_id`)
    REFERENCES `rdap`.`nameserver` (`nse_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_nameserver_events_event1`
    FOREIGN KEY (`eve_id`)
    REFERENCES `rdap`.`event` (`eve_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`public_id`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`public_id` (
  `pid_id` BIGINT NOT NULL AUTO_INCREMENT,
  `pid_type` VARCHAR(255) NULL,
  `pid_identifier` VARCHAR(255) NULL,
  PRIMARY KEY (`pid_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`domain_public_ids`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`domain_public_ids` (
  `dom_id` BIGINT NOT NULL,
  `pid_id` BIGINT NOT NULL,
  PRIMARY KEY (`dom_id`, `pid_id`),
  INDEX `fk_domain_public_ids_public_id1_idx_1` (`pid_id` ASC),
  UNIQUE INDEX `public_id_pid_id_UNIQUE_2` (`pid_id` ASC),
  CONSTRAINT `fk_domain_public_ids_domain1`
    FOREIGN KEY (`dom_id`)
    REFERENCES `rdap`.`domain` (`dom_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_domain_public_ids_public_id1`
    FOREIGN KEY (`pid_id`)
    REFERENCES `rdap`.`public_id` (`pid_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`entity_public_ids`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`entity_public_ids` (
  `ent_id` BIGINT NOT NULL,
  `pid_id` BIGINT NOT NULL,
  PRIMARY KEY (`ent_id`, `pid_id`),
  INDEX `fk_domain_public_ids_public_id1_idx_2` (`pid_id` ASC),
  UNIQUE INDEX `public_id_pid_id_UNIQUE_1` (`pid_id` ASC),
  INDEX `fk_domain_public_ids_copy2_entity1_idx` (`ent_id` ASC),
  CONSTRAINT `fk_domain_public_ids_public_id11`
    FOREIGN KEY (`pid_id`)
    REFERENCES `rdap`.`public_id` (`pid_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_domain_public_ids_copy2_entity1`
    FOREIGN KEY (`ent_id`)
    REFERENCES `rdap`.`entity` (`ent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`event_links`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`event_links` (
  `eve_id` BIGINT NOT NULL,
  `lin_id` BIGINT NOT NULL,
  PRIMARY KEY (`eve_id`, `lin_id`),
  INDEX `fk_event_has_link_link1_idx` (`lin_id` ASC),
  INDEX `fk_event_has_link_event1_idx` (`eve_id` ASC),
  CONSTRAINT `fk_event_has_link_event1`
    FOREIGN KEY (`eve_id`)
    REFERENCES `rdap`.`event` (`eve_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_has_link_link1`
    FOREIGN KEY (`lin_id`)
    REFERENCES `rdap`.`link` (`lin_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`remark_links`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`remark_links` (
  `rem_id` BIGINT NOT NULL,
  `lin_id` BIGINT NOT NULL,
  PRIMARY KEY (`rem_id`, `lin_id`),
  INDEX `fk_remark_has_link_link2_idx` (`lin_id` ASC),
  INDEX `fk_remark_has_link_remark2_idx` (`rem_id` ASC),
  CONSTRAINT `fk_remark_has_link_remark2`
    FOREIGN KEY (`rem_id`)
    REFERENCES `rdap`.`remark` (`rem_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_remark_has_link_link2`
    FOREIGN KEY (`lin_id`)
    REFERENCES `rdap`.`link` (`lin_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`variant_relation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`variant_relation` (
  `rel_id` TINYINT NOT NULL,
  `var_id` BIGINT NOT NULL,
  PRIMARY KEY (`rel_id`, `var_id`),
  INDEX `fk_relation_has_variant_variant1_idx` (`var_id` ASC),
  INDEX `fk_relation_has_variant_relation1_idx` (`rel_id` ASC),
  CONSTRAINT `fk_relation_has_variant_relation1`
    FOREIGN KEY (`rel_id`)
    REFERENCES `rdap`.`relation` (`rel_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_relation_has_variant_variant1`
    FOREIGN KEY (`var_id`)
    REFERENCES `rdap`.`variant` (`var_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`entity_contact`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`entity_contact` (
  `ent_id` BIGINT NOT NULL,
  `vca_id` BIGINT NOT NULL,
  PRIMARY KEY (`ent_id`, `vca_id`),
  INDEX `fk_entity_contact_vcard1_idx` (`vca_id` ASC),
  CONSTRAINT `fk_entity_contact_entity1`
    FOREIGN KEY (`ent_id`)
    REFERENCES `rdap`.`entity` (`ent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_entity_contact_vcard1`
    FOREIGN KEY (`vca_id`)
    REFERENCES `rdap`.`vcard` (`vca_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`nameserver_entity_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`nameserver_entity_roles` (
  `nse_id` BIGINT NOT NULL,
  `ent_id` BIGINT NOT NULL,
  `rol_id` TINYINT NOT NULL,
  PRIMARY KEY (`nse_id`, `ent_id`, `rol_id`),
  INDEX `fk_entity_roles_roles1_idx_1` (`rol_id` ASC),
  INDEX `fk_entity_roles_entity1_idx_1` (`ent_id` ASC),
  INDEX `fk_nameserver_entity_roles_nameserver1_idx` (`nse_id` ASC),
  CONSTRAINT `fk_entity_roles_roles10`
    FOREIGN KEY (`rol_id`)
    REFERENCES `rdap`.`roles` (`rol_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_entity_roles_entity10`
    FOREIGN KEY (`ent_id`)
    REFERENCES `rdap`.`entity` (`ent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_nameserver_entity_roles_nameserver1`
    FOREIGN KEY (`nse_id`)
    REFERENCES `rdap`.`nameserver` (`nse_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`entity_entity_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`entity_entity_roles` (
  `main_ent_id` BIGINT NOT NULL,
  `ent_id` BIGINT NOT NULL,
  `rol_id` TINYINT NOT NULL,
  PRIMARY KEY (`main_ent_id`, `ent_id`, `rol_id`),
  INDEX `fk_entity_roles_roles1_idx_2` (`rol_id` ASC),
  INDEX `fk_entity_roles_entity1_idx_2` (`ent_id` ASC),
  INDEX `fk_entity_entity_roles_entity1_idx` (`main_ent_id` ASC),
  CONSTRAINT `fk_entity_roles_roles100`
    FOREIGN KEY (`rol_id`)
    REFERENCES `rdap`.`roles` (`rol_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_entity_roles_entity100`
    FOREIGN KEY (`ent_id`)
    REFERENCES `rdap`.`entity` (`ent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_entity_entity_roles_entity1`
    FOREIGN KEY (`main_ent_id`)
    REFERENCES `rdap`.`entity` (`ent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`rdap_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`rdap_user` (
  `rus_id` BIGINT(200) NOT NULL AUTO_INCREMENT,
  `rus_name` VARCHAR(16) NOT NULL,
  `rus_pass` VARCHAR(200) NOT NULL,
  `rus_max_search_results` INT NULL,
  PRIMARY KEY (`rus_id`),
  INDEX `rus_name_index` (`rus_name` ASC),
  UNIQUE INDEX `rus_name_UNIQUE` (`rus_name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`rdap_user_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`rdap_user_role` (
  `rus_name` VARCHAR(16) NOT NULL,
  `rur_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`rus_name`, `rur_name`),
  CONSTRAINT `rdap_user_ir_fk`
    FOREIGN KEY (`rus_name`)
    REFERENCES `rdap`.`rdap_user` (`rus_name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`ip_network_entity_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`ip_network_entity_roles` (
  `ine_id` BIGINT UNSIGNED NOT NULL,
  `ent_id` BIGINT NOT NULL,
  `rol_id` TINYINT NOT NULL,
  PRIMARY KEY (`ine_id`, `ent_id`, `rol_id`),
  INDEX `fk_ipn_entity_roles_ip_network1_idx` (`ine_id` ASC),
  INDEX `fk_ipn_entity_roles_roles1_idx` (`rol_id` ASC),
  CONSTRAINT `fk_ipn_entity_roles_entity1`
    FOREIGN KEY (`ent_id`)
    REFERENCES `rdap`.`entity` (`ent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ipn_entity_roles_ip_network1`
    FOREIGN KEY (`ine_id`)
    REFERENCES `rdap`.`ip_network` (`ine_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ipn_entity_roles_roles1`
    FOREIGN KEY (`rol_id`)
    REFERENCES `rdap`.`roles` (`rol_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rdap`.`ip_network_parent_relation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`ip_network_parent_relation` (
  `ine_parent_handle` VARCHAR(255) NOT NULL,
  `ine_son_handle` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`ine_parent_handle`, `ine_son_handle`),
  INDEX `fk_ip_network_parent_relation_ip_network2_idx` (`ine_son_handle` ASC),
  UNIQUE INDEX `ine_son_handle_UNIQUE` (`ine_son_handle` ASC),
  CONSTRAINT `fk_ip_network_parent_relation_ip_network1`
    FOREIGN KEY (`ine_parent_handle`)
    REFERENCES `rdap`.`ip_network` (`ine_handle`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ip_network_parent_relation_ip_network2`
    FOREIGN KEY (`ine_son_handle`)
    REFERENCES `rdap`.`ip_network` (`ine_handle`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `rdap`.`link_lang`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`link_lang` (
  `lin_id` BIGINT(20) NOT NULL,
  `lan_hreflang` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`lin_id`, `lan_hreflang`),
  CONSTRAINT `fk_link_hreflang_link1` 
  	FOREIGN KEY (`lin_id`) 
  	REFERENCES `link` (`lin_id`) 
  	ON DELETE NO ACTION 
  	ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- -----------------------------------------------------
-- Table `rdap`.`entity_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rdap`.`entity_role` (
  `ent_id` bigint(20) NOT NULL,
  `rol_id` tinyint(4) NOT NULL,
  PRIMARY KEY (`ent_id`,`rol_id`),
  UNIQUE KEY `unique_ent_rol` (`ent_id`,`rol_id`),
  KEY `fk_entity_role_roles1_idx` (`rol_id`),
  CONSTRAINT `fk_entity_role_entity1` 
  	  FOREIGN KEY (`ent_id`) 
	  REFERENCES `entity` (`ent_id`) 
	  ON DELETE NO ACTION 
	  ON UPDATE NO ACTION,
  CONSTRAINT `fk_entity_role_roles1` 
	  FOREIGN KEY (`rol_id`) 
	  REFERENCES `roles` (`rol_id`) 
	  ON DELETE NO ACTION 
	  ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;





