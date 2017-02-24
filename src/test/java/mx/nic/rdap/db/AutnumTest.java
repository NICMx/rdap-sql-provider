package mx.nic.rdap.db;

import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import mx.nic.rdap.core.catalog.EventAction;
import mx.nic.rdap.core.catalog.Rol;
import mx.nic.rdap.core.catalog.Status;
import mx.nic.rdap.core.db.Autnum;
import mx.nic.rdap.core.db.Entity;
import mx.nic.rdap.core.db.Event;
import mx.nic.rdap.core.db.Link;
import mx.nic.rdap.core.db.Remark;
import mx.nic.rdap.core.db.RemarkDescription;
import mx.nic.rdap.db.exception.ObjectNotFoundException;
import mx.nic.rdap.db.exception.RequiredValueNotFoundException;
import mx.nic.rdap.db.model.AutnumModel;
import mx.nic.rdap.db.model.EntityModel;
import mx.nic.rdap.db.objects.AutnumDbObj;
import mx.nic.rdap.db.objects.EntityDbObj;
import mx.nic.rdap.db.objects.EventDbObj;
import mx.nic.rdap.db.objects.LinkDbObj;
import mx.nic.rdap.db.objects.RemarkDbObj;
import mx.nic.rdap.db.objects.RemarkDescriptionDbObj;

/**
 * Test for {@link Autnum}
 * 
 */
public class AutnumTest extends DatabaseTest {

	// @Test
	public void existByRange() {
		try {
			AutnumModel.existByRange(10l, connection);
		} catch (SQLException | ObjectNotFoundException s) {
			fail();
		}
	}

	@Test
	public void insertAndGetAutnum() {
		Entity registrant = new EntityDbObj();
		registrant.setHandle("testHandler");
		registrant.setPort43("testestestest");
		registrant.getRoles().add(Rol.REGISTRANT);

		try {
			EntityModel.storeToDatabase(registrant, connection);
		} catch (RequiredValueNotFoundException | SQLException e) {
			e.printStackTrace();
			fail();
		}
		Link link = new LinkDbObj();
		link.setHref("dummy.com.mx");
		link.setValue("http://dummy.net/ASN");
		link.setType("application/rdap+json");
		link.setRel("self");

		Event event = new EventDbObj();
		event.setEventAction(EventAction.REGISTRATION);
		event.setEventDate(new Date());
		event.setEventActor("");

		Remark remark = new RemarkDbObj();
		remark.setLanguage("ES");
		remark.setTitle("Prueba");
		remark.setType("PruebaType");

		List<RemarkDescription> descriptions = new ArrayList<RemarkDescription>();
		RemarkDescription description1 = new RemarkDescriptionDbObj();
		description1.setOrder(1);
		description1.setDescription("She sells sea shells down by the sea shore.");

		RemarkDescription description2 = new RemarkDescriptionDbObj();
		description2.setOrder(2);
		description2.setDescription("Originally written by Terry Sullivan.");

		descriptions.add(description1);
		descriptions.add(description2);
		remark.setDescriptions(descriptions);

		Autnum autnum = new AutnumDbObj();
		autnum.setCountry("MX");
		autnum.setStartAutnum(100L);
		autnum.setEndAutnum(101L);
		autnum.setName("testName");
		autnum.setType("testType");
		autnum.getEntities().add(registrant);
		autnum.setPort43("dummy.dummy.mx");
		autnum.getStatus().add(Status.ACTIVE);
		autnum.getLinks().add(link);
		autnum.getEvents().add(event);
		autnum.getRemarks().add(remark);
		autnum.setHandle("dummyASN");
		try {
			AutnumModel.storeToDatabase(autnum, connection);
		} catch (SQLException | RequiredValueNotFoundException e) {
			e.printStackTrace();
			fail();
		}

		Autnum getById = null;
		Autnum getByRange = null;

		try {
			getByRange = AutnumModel.getByRange(autnum.getStartAutnum(), connection);
		} catch (SQLException | ObjectNotFoundException e) {
			e.printStackTrace();
			fail();
		}

		autnum.equals(getByRange);

		try {
			getById = AutnumModel.getAutnumById(autnum.getId(), connection);
		} catch (SQLException | ObjectNotFoundException e) {
			e.printStackTrace();
			fail();
		}
		autnum.equals(getById);

		try {
			AutnumModel.existByRange(101L, connection);
		} catch (SQLException | ObjectNotFoundException s) {
			fail();
		}
	}
}
