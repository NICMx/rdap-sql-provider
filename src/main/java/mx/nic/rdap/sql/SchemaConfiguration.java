package mx.nic.rdap.sql;

import java.util.Properties;

import mx.nic.rdap.sql.model.AutnumModel;
import mx.nic.rdap.sql.model.CountryCodeModel;
import mx.nic.rdap.sql.model.DomainModel;
import mx.nic.rdap.sql.model.DsDataModel;
import mx.nic.rdap.sql.model.EntityModel;
import mx.nic.rdap.sql.model.EventModel;
import mx.nic.rdap.sql.model.IpAddressModel;
import mx.nic.rdap.sql.model.IpNetworkModel;
import mx.nic.rdap.sql.model.KeyDataModel;
import mx.nic.rdap.sql.model.LinkModel;
import mx.nic.rdap.sql.model.NameserverModel;
import mx.nic.rdap.sql.model.PublicIdModel;
import mx.nic.rdap.sql.model.RdapUserModel;
import mx.nic.rdap.sql.model.RdapUserRoleModel;
import mx.nic.rdap.sql.model.RemarkDescriptionModel;
import mx.nic.rdap.sql.model.RemarkModel;
import mx.nic.rdap.sql.model.RoleModel;
import mx.nic.rdap.sql.model.SecureDNSModel;
import mx.nic.rdap.sql.model.StatusModel;
import mx.nic.rdap.sql.model.VCardModel;
import mx.nic.rdap.sql.model.VCardPostalInfoModel;
import mx.nic.rdap.sql.model.VariantModel;
import mx.nic.rdap.sql.model.ZoneModel;

public class SchemaConfiguration {

	public static void init(Properties serverProperties) {
		String schema;

		SQLProviderConfiguration.initForServer(serverProperties);
		// get schema from configuration file.
		schema = SQLProviderConfiguration.getDefaultSchema();

		initAllModels(schema);
	}

	public static void initAllModels(String schema) {
		AutnumModel.loadQueryGroup(schema);
		CountryCodeModel.loadQueryGroup(schema);
		DomainModel.loadQueryGroup(schema);
		DsDataModel.loadQueryGroup(schema);
		KeyDataModel.loadQueryGroup(schema);
		EntityModel.loadQueryGroup(schema);
		EventModel.loadQueryGroup(schema);
		IpAddressModel.loadQueryGroup(schema);
		IpNetworkModel.loadQueryGroup(schema);
		LinkModel.loadQueryGroup(schema);
		NameserverModel.loadQueryGroup(schema);
		PublicIdModel.loadQueryGroup(schema);
		RdapUserModel.loadQueryGroup(schema);
		RdapUserRoleModel.loadQueryGroup(schema);
		RemarkModel.loadQueryGroup(schema);
		RemarkDescriptionModel.loadQueryGroup(schema);
		RoleModel.loadQueryGroup(schema);
		SecureDNSModel.loadQueryGroup(schema);
		StatusModel.loadQueryGroup(schema);
		VariantModel.loadQueryGroup(schema);
		VCardModel.loadQueryGroup(schema);
		VCardPostalInfoModel.loadQueryGroup(schema);
		ZoneModel.loadQueryGroup(schema);
	}
}
