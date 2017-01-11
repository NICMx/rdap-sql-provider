package mx.nic.rdap.db;

import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import mx.nic.rdap.core.catalog.Rol;
import mx.nic.rdap.core.db.Domain;
import mx.nic.rdap.core.db.Entity;
import mx.nic.rdap.core.db.PublicId;
import mx.nic.rdap.db.exception.ObjectNotFoundException;
import mx.nic.rdap.db.exception.RequiredValueNotFoundException;
import mx.nic.rdap.db.model.DomainModel;
import mx.nic.rdap.db.model.EntityModel;
import mx.nic.rdap.db.model.PublicIdModel;
import mx.nic.rdap.db.model.ZoneModel;
import mx.nic.rdap.db.objects.DomainDAO;
import mx.nic.rdap.db.objects.EntityDAO;
import mx.nic.rdap.db.objects.PublicIdDAO;
import mx.nic.rdap.db.objects.SecureDNSDAO;

/**
 * Test for the PublicId object
 * 
 */
public class PublicIdTest extends DatabaseTest {

	@Test
	public void insertAndGetByDomain() {
		Long domainId = createSimpleDomain().getId();

		Random random = new Random();
		Long rndPublicId = random.nextLong();
		List<PublicId> publicIds = new ArrayList<PublicId>();
		PublicIdDAO publicId = createPublicId("dummy" + rndPublicId, "dummy IETF");
		publicIds.add(publicId);
		try {
			PublicIdModel.storePublicIdByDomain(publicIds, domainId, connection);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		List<PublicId> byDomainId = new ArrayList<PublicId>();
		try {
			byDomainId = PublicIdModel.getByDomain(domainId, connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		publicId.equals(byDomainId.get(byDomainId.size() - 1));
	}

	public static PublicIdDAO createPublicId(String publicId, String type) {
		PublicIdDAO pi = new PublicIdDAO();
		pi.setPublicId(publicId);
		pi.setType(type);
		return pi;
	}

	private static Domain createSimpleDomain() {

		Entity registrar = new EntityDAO();
		registrar.setHandle("whois");
		registrar.setPort43("whois.mx");
		registrar.getRoles().add(Rol.SPONSOR);

		Entity ent = new EntityDAO();
		ent.setHandle("usr_evaldez");
		ent.getRoles().add(Rol.REGISTRANT);
		ent.getRoles().add(Rol.ADMINISTRATIVE);
		ent.getRoles().add(Rol.TECHNICAL);

		try {
			EntityModel.storeToDatabase(registrar, connection);
			EntityModel.storeToDatabase(ent, connection);
		} catch (SQLException | RequiredValueNotFoundException e1) {
			e1.printStackTrace();
			fail();
		}

		try {
			EntityModel.storeToDatabase(registrar, connection);
			EntityModel.storeToDatabase(ent, connection);
		} catch (SQLException | RequiredValueNotFoundException e1) {
			e1.printStackTrace();
			fail();
		}

		Domain dom = new DomainDAO();
		dom.getEntities().add(ent);
		dom.getEntities().add(registrar);
		dom.setHandle("domcommx");
		dom.setPunycodeName("mydomaintest.mx");

		String zoneName = "mx";
		try {
			ZoneModel.storeToDatabase(zoneName, connection);
		} catch (SQLException e1) {
			e1.printStackTrace();
			fail(e1.toString());
		}
		dom.setZone(zoneName);

		SecureDNSDAO secureDNS = SecureDnsTest.getSecureDns(null, null, false, false, null);
		dom.setSecureDNS(secureDNS);

		try {
			DomainModel.storeToDatabase(dom, false, connection);
		} catch (SQLException | RequiredValueNotFoundException | ObjectNotFoundException e) {
			e.printStackTrace();
			fail();
		}

		return dom;
	}
}
