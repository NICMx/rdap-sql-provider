package mx.nic.rdap.sql.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import mx.nic.rdap.core.catalog.Role;
import mx.nic.rdap.core.db.Domain;
import mx.nic.rdap.core.db.Entity;
import mx.nic.rdap.core.db.PublicId;
import mx.nic.rdap.sql.model.PublicIdModel;
import mx.nic.rdap.sql.objects.DomainDbObj;
import mx.nic.rdap.sql.objects.EntityDbObj;
import mx.nic.rdap.sql.objects.PublicIdDbObj;
import mx.nic.rdap.sql.objects.SecureDNSDbObj;
import mx.nic.rdap.store.model.DomainStoreModel;
import mx.nic.rdap.store.model.EntityStoreModel;
import mx.nic.rdap.store.model.PublicIdStoreModel;
import mx.nic.rdap.store.model.ZoneStoreModel;

/**
 * Test for the PublicId object
 * 
 */
public class PublicIdTest extends DatabaseTest {

	@Test
	public void insertAndGetByDomain() throws SQLException {
		Long domainId = createSimpleDomain().getId();

		Random random = new Random();
		Long rndPublicId = random.nextLong();
		List<PublicId> publicIds = new ArrayList<PublicId>();
		PublicIdDbObj publicId = createPublicId("dummy" + rndPublicId, "dummy IETF");
		publicIds.add(publicId);
		PublicIdStoreModel.storePublicIdByDomain(publicIds, domainId, connection);

		List<PublicId> byDomainId = PublicIdModel.getByDomain(domainId, connection);
		publicId.equals(byDomainId.get(byDomainId.size() - 1));
	}

	public static PublicIdDbObj createPublicId(String publicId, String type) {
		PublicIdDbObj pi = new PublicIdDbObj();
		pi.setPublicId(publicId);
		pi.setType(type);
		return pi;
	}

	private static Domain createSimpleDomain() throws SQLException {

		Entity registrar = new EntityDbObj();
		registrar.setHandle("whois");
		registrar.setPort43("whois.mx");
		registrar.getRoles().add(Role.SPONSOR);

		Entity ent = new EntityDbObj();
		ent.setHandle("usr_evaldez");
		ent.getRoles().add(Role.REGISTRANT);
		ent.getRoles().add(Role.ADMINISTRATIVE);
		ent.getRoles().add(Role.TECHNICAL);

		EntityStoreModel.storeToDatabase(registrar, connection);
		EntityStoreModel.storeToDatabase(ent, connection);

		EntityStoreModel.storeToDatabase(registrar, connection);
		EntityStoreModel.storeToDatabase(ent, connection);

		Domain dom = new DomainDbObj();
		dom.getEntities().add(ent);
		dom.getEntities().add(registrar);
		dom.setHandle("domcommx");
		dom.setLdhName("mydomaintest.mx");

		String zoneName = "mx";
		ZoneStoreModel.storeToDatabase(zoneName, connection);
		dom.setZone(zoneName);

		SecureDNSDbObj secureDNS = SecureDnsTest.getSecureDns(null, null, false, false, null, null);
		dom.setSecureDNS(secureDNS);

		DomainStoreModel.storeToDatabase(dom, false, connection);

		return dom;
	}
}
