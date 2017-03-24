package mx.nic.rdap.db;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import mx.nic.rdap.core.catalog.EventAction;
import mx.nic.rdap.core.catalog.Role;
import mx.nic.rdap.core.catalog.Status;
import mx.nic.rdap.core.db.Entity;
import mx.nic.rdap.core.db.Event;
import mx.nic.rdap.core.db.Link;
import mx.nic.rdap.core.db.PublicId;
import mx.nic.rdap.core.db.Remark;
import mx.nic.rdap.core.db.RemarkDescription;
import mx.nic.rdap.core.db.VCard;
import mx.nic.rdap.core.db.VCardPostalInfo;
import mx.nic.rdap.db.model.EntityModel;
import mx.nic.rdap.db.objects.EntityDbObj;
import mx.nic.rdap.db.objects.EventDbObj;
import mx.nic.rdap.db.objects.LinkDbObj;
import mx.nic.rdap.db.objects.PublicIdDbObj;
import mx.nic.rdap.db.objects.RemarkDbObj;
import mx.nic.rdap.db.objects.RemarkDescriptionDbObj;

/**
 * Tests for the {@link EntityModel}
 * 
 */
public class EntityTest extends DatabaseTest {

	@Test
	public void insertMinimunEntity() {
		Entity entity = createEntity(null, "minimunEntity", "www.rardhfelix.mx");
		try {
			EntityModel.storeToDatabase(entity, connection);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// check if exists
		try {
			EntityModel.getByHandle("minimunEntity", connection);
		} catch (SQLException s) {
			fail();
		}
	}

	/**
	 * Creates a simple entity object and store it in the database, then get the
	 * same entity from the database by its ID and Handle, finally compares the
	 * first objects with the objects in the database
	 */
	@Test
	public void insertAndGetSimpleEntity() {
		Random random = new Random();
		int randomInt = random.nextInt();

		// create local instances;
		Entity entity = createEntity(null, "rar_dhfelix" + randomInt, "www.rardhfelix" + randomInt + ".mx");

		// Status data
		List<Status> statusList = new ArrayList<Status>();
		statusList.add(Status.ACTIVE);
		statusList.add(Status.ASSOCIATED);
		entity.setStatus(statusList);

		// Remarks data
		List<Remark> remarks = new ArrayList<Remark>();
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
		remarks.add(remark);
		entity.getRemarks().addAll(remarks);

		// Links data
		List<Link> links = new ArrayList<Link>();
		Link link = new LinkDbObj();
		link.setValue("http://example.net/nameserver/xxxx");
		link.setRel("self");
		link.setHref("http://example.net/nameserver/xxxx");
		link.setType("application/rdap+json");
		links.add(link);
		entity.getLinks().addAll(links);

		// Events Data
		List<Event> events = new ArrayList<Event>();
		Event event1 = new EventDbObj();
		event1.setEventAction(EventAction.REGISTRATION);
		event1.setEventDate(new Date());

		Event event2 = new EventDbObj();
		event2.setEventAction(EventAction.LAST_CHANGED);
		event2.setEventDate(new Date());
		event2.setEventActor("joe@example.com");

		// event links data
		List<Link> eventLinks = new ArrayList<Link>();
		Link eventLink = new LinkDbObj();
		eventLink.setValue("eventLink1");
		eventLink.setRel("eventlink");
		eventLink.setHref("http://example.net/eventlink/xxxx");
		eventLink.setType("application/rdap+json");
		eventLinks.add(eventLink);
		event2.setLinks(eventLinks);

		events.add(event1);
		events.add(event2);
		entity.getEvents().addAll(events);

		// PublicId data
		List<PublicId> listPids = new ArrayList<>();
		PublicId pid = new PublicIdDbObj();
		pid.setPublicId("dumy pid 1");
		pid.setType("dummy iana");
		PublicId pid2 = new PublicIdDbObj();
		pid.setPublicId("dumy pid 2");
		pid.setType("dummy IETF");
		listPids.add(pid);
		listPids.add(pid2);

		entity.getPublicIds().addAll(listPids);

		// Vcard data
		VCard vCard = VCardTest.createVCardDao(null, "mi nombre" + randomInt, "company" + randomInt,
				"www.companytest" + randomInt + ".com", "correo" + randomInt + "@correo.com", "818282569" + randomInt,
				"520448114561234" + randomInt, null, null);

		List<VCardPostalInfo> postalInfoList = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			postalInfoList.add(VCardTest.createVCardPostalInfo(null, null, "mytype" + random.nextInt(), "MX",
					"monterrey", "Luis Elizondo", null, null, "NL", "66666"));
		}
		vCard.setPostalInfo(postalInfoList);
		entity.getVCardList().add(vCard);

		// Store it in the database
		try {
			EntityModel.storeToDatabase(entity, connection);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// Query the database
		Entity byHandle = null;
		try {
			byHandle = EntityModel.getByHandle(entity.getHandle(), connection);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// Compares the results
		Assert.assertTrue("getByHandle fails", entity.equals(byHandle));
	}

	@Test
	public void createAndInsertComplexObject() {
		Random random = new Random();
		int randomInt = random.nextInt();

		// ----- START OF REGISTRAR -----
		// first we create a RAR
		Entity ent = new EntityDbObj();

		ent.setHandle("rar_dhfelix");
		ent.setPort43("whois.dhfelixrar.mx");

		ent.getStatus().add(Status.ACTIVE);
		ent.getStatus().add(Status.VALIDATED);

		ent.getRoles().add(Role.SPONSOR);

		PublicId pid = new PublicIdDbObj();
		pid.setPublicId("Dhfelix_rar_from_mx");
		pid.setType("DUMMY REGISTRARS PUBLIC IDS");
		ent.getPublicIds().add(pid);

		// Vcard data
		VCard vCard = VCardTest.createVCardDao(null, "mi nombre" + randomInt, "company" + randomInt,
				"www.companytest" + randomInt + ".com", "correo" + randomInt + "@correo.com", "818282569" + randomInt,
				"520448114561234" + randomInt, null, null);
		List<VCardPostalInfo> postalInfoList = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			postalInfoList.add(VCardTest.createVCardPostalInfo(null, null, "mytype" + randomInt, "MX", "monterrey",
					"Luis Elizondo", null, null, "NL", "66666"));
		}

		ent.getVCardList().add(vCard);
		// ----- END OF REGISTRAR -----
		// ----- START OF ENT 1 ------
		// create local instances;
		Entity entity = createEntity(null, "rar_dhfelix" + randomInt, "www.rardhfelix" + randomInt + ".mx");

		// Status data
		List<Status> statusList = new ArrayList<Status>();
		statusList.add(Status.ACTIVE);
		statusList.add(Status.ASSOCIATED);
		entity.setStatus(statusList);

		// Remarks data
		List<Remark> remarks = new ArrayList<Remark>();
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
		remarks.add(remark);
		entity.getRemarks().addAll(remarks);

		// Links data
		List<Link> links = new ArrayList<Link>();
		Link link = new LinkDbObj();
		link.setValue("http://example.net/nameserver/xxxx");
		link.setRel("self");
		link.setHref("http://example.net/nameserver/xxxx");
		link.setType("application/rdap+json");
		links.add(link);
		entity.getLinks().addAll(links);

		// Events Data
		List<Event> events = new ArrayList<Event>();
		Event event1 = new EventDbObj();
		event1.setEventAction(EventAction.REGISTRATION);
		event1.setEventDate(new Date());

		Event event2 = new EventDbObj();
		event2.setEventAction(EventAction.LAST_CHANGED);
		event2.setEventDate(new Date());
		event2.setEventActor("joe@example.com");

		// event links data
		List<Link> eventLinks = new ArrayList<Link>();
		Link eventLink = new LinkDbObj();
		eventLink.setValue("eventLink1");
		eventLink.setRel("eventlink");
		eventLink.setHref("http://example.net/eventlink/xxxx");
		eventLink.setType("application/rdap+json");
		eventLinks.add(eventLink);
		event2.setLinks(eventLinks);

		events.add(event1);
		events.add(event2);
		entity.getEvents().addAll(events);

		randomInt = random.nextInt();
		// Vcard data
		VCard vCardEnt = VCardTest.createVCardDao(null, "mi nombre" + randomInt, "company" + randomInt,
				"www.companytest" + randomInt + ".com", "correo" + randomInt + "@correo.com", "818282569" + randomInt,
				"520448114561234" + randomInt, null, null);

		List<VCardPostalInfo> postalInfoListEnt = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			postalInfoListEnt.add(VCardTest.createVCardPostalInfo(null, null, "mytype" + random.nextInt(), "MX",
					"monterrey", "Luis Elizondo", null, null, "NL", "66666"));
		}
		vCard.setPostalInfo(postalInfoListEnt);
		entity.getVCardList().add(vCardEnt);
		// ----- END OF ENT 1 ------
		// ----- START OF ENT 2 ------
		// create local instances;
		Entity entity2 = createEntity(null, "rar_dhfelix" + randomInt, "www.rardhfelix" + randomInt + ".mx");

		// Status data
		List<Status> statusList2 = new ArrayList<Status>();
		statusList2.add(Status.ACTIVE);
		statusList2.add(Status.ASSOCIATED);
		entity2.setStatus(statusList2);

		// Remarks data
		List<Remark> remList = new ArrayList<Remark>();
		Remark rem = new RemarkDbObj();
		rem.setLanguage("ES");
		rem.setTitle("Prueba");
		rem.setType("PruebaType");

		List<RemarkDescription> descList2 = new ArrayList<RemarkDescription>();
		RemarkDescription desc1 = new RemarkDescriptionDbObj();
		desc1.setOrder(1);
		desc1.setDescription("She sells sea shells down by the sea shore.");

		RemarkDescription desc2 = new RemarkDescriptionDbObj();
		desc2.setOrder(2);
		desc2.setDescription("Originally written by Terry Sullivan.");

		descList2.add(desc1);
		descList2.add(desc2);
		rem.setDescriptions(descList2);
		remList.add(rem);
		entity2.getRemarks().addAll(remList);

		// Links data
		List<Link> linksList = new ArrayList<Link>();
		Link link2 = new LinkDbObj();
		link2.setValue("http://example.net/nameserver/xxxx");
		link2.setRel("self");
		link2.setHref("http://example.net/nameserver/xxxx");
		link2.setType("application/rdap+json");
		linksList.add(link2);
		entity2.getLinks().addAll(linksList);

		// Events Data
		List<Event> eventList = new ArrayList<Event>();
		Event eve1 = new EventDbObj();
		eve1.setEventAction(EventAction.REGISTRATION);
		eve1.setEventDate(new Date());

		Event eve2 = new EventDbObj();
		eve2.setEventAction(EventAction.LAST_CHANGED);
		eve2.setEventDate(new Date());
		eve2.setEventActor("joe@example.com");

		// event links data
		List<Link> eventLinksList = new ArrayList<Link>();
		Link eventLink2 = new LinkDbObj();
		eventLink2.setValue("eventLink1");
		eventLink2.setRel("eventlink");
		eventLink2.setHref("http://example.net/eventlink/xxxx");
		eventLink2.setType("application/rdap+json");
		eventLinksList.add(eventLink2);
		eve2.setLinks(eventLinksList);

		eventList.add(eve1);
		eventList.add(eve2);
		entity2.getEvents().addAll(eventList);

		randomInt = random.nextInt();
		// Vcard data
		VCard vCardEnt2 = VCardTest.createVCardDao(null, "mi nombre" + randomInt, "company" + randomInt,
				"www.companytest" + randomInt + ".com", "correo" + randomInt + "@correo.com", "818282569" + randomInt,
				"520448114561234" + randomInt, null, null);

		List<VCardPostalInfo> postalInfoListEnt2 = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			postalInfoListEnt2.add(VCardTest.createVCardPostalInfo(null, null, "mytype" + random.nextInt(), "MX",
					"monterrey", "Luis Elizondo", null, null, "NL", "66666"));
		}
		vCardEnt2.getPostalInfo().addAll(postalInfoListEnt2);
		entity2.getVCardList().add(vCardEnt2);
		// ----- END OF ENT 2 ------

		// Store it in the database
		try {
			EntityModel.storeToDatabase(entity, connection);
			EntityModel.storeToDatabase(entity2, connection);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// Query the database
		Entity byHandle = null;
		Entity byHandle2 = null;
		try {
			byHandle = EntityModel.getByHandle(entity.getHandle(), connection);
			byHandle2 = EntityModel.getByHandle(entity2.getHandle(), connection);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// Compares the results
		Assert.assertTrue("getByHandle fails", entity.equals(byHandle));
		Assert.assertTrue("getByHandle2 fails", entity2.equals(byHandle2));

	}

	@Test
	public void createAndInsertRegistrar() {
		Entity entity = createEntity(null, "rar_test", "whois.rar_test.com");
		// entity.getRoles().add(Role.REGISTRAR);

		Entity legal = createEntity(null, "legal_contact", null);
		legal.getRoles().add(Role.ADMINISTRATIVE);

		Entity tech = createEntity(null, "tech_contact", null);
		tech.getRoles().add(Role.TECHNICAL);

		entity.getEntities().add(tech);
		entity.getEntities().add(legal);

		// Store it in the database
		try {
			EntityModel.storeToDatabase(entity, connection);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// Query the database
		Entity byHandle = null;
		try {
			byHandle = EntityModel.getByHandle(entity.getHandle(), connection);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// Compares the results
		Assert.assertTrue("getByHandle fails", entity.equals(byHandle));

	}

	/**
	 * Create a new instance, and set the incoming parameters. (Does not store
	 * the instance in the Database).
	 * 
	 * @param id
	 *            The id of the entity
	 * @param handle
	 *            roid of the entity
	 * @param port43
	 * @param rarId
	 * @param vCardId
	 * @return
	 */
	public static EntityDbObj createEntity(Long id, String handle, String port43) {
		EntityDbObj e = new EntityDbObj();
		e.setId(id);
		e.setHandle(handle);
		e.setPort43(port43);
		return e;
	}

	public static Entity createDefaultEntity(Connection connection) {
		// Entity base data
		Random random = new Random();
		int randomInt = random.nextInt();

		// Create local instances
		Entity entity = createEntity(null, "ent_dhfelix", null);

		try {
			Entity byHandle = EntityModel.getByHandle(entity.getHandle(), connection);
			if (byHandle != null)
				return byHandle;
			// if not found, continue;
		} catch (SQLException e) {
			e.printStackTrace();
			fail();
		}

		VCard vCard = VCardTest.createVCardDao(null, "mi nombre" + randomInt, "company" + randomInt,
				"www.companytest" + randomInt + ".com", "correo" + randomInt + "@correo.com", "818282569" + randomInt,
				"520448114561234" + randomInt, null, null);

		List<VCardPostalInfo> postalInfoList = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			postalInfoList.add(VCardTest.createVCardPostalInfo(null, null, "mytype" + random.nextInt(), "MX",
					"monterrey", "Luis Elizondo", null, null, "NL", "66666"));
		}
		vCard.setPostalInfo(postalInfoList);

		entity.getVCardList().add(vCard);

		// Status data
		List<Status> statusList = new ArrayList<Status>();
		statusList.add(Status.ACTIVE);
		statusList.add(Status.ASSOCIATED);
		entity.setStatus(statusList);

		// Remarks data
		List<Remark> remarks = new ArrayList<Remark>();
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
		remarks.add(remark);
		entity.getRemarks().addAll(remarks);

		// Links data
		List<Link> links = new ArrayList<Link>();
		Link link = new LinkDbObj();
		link.setValue("http://example.net/nameserver/xxxx");
		link.setRel("self");
		link.setHref("http://example.net/nameserver/xxxx");
		link.setType("application/rdap+json");
		links.add(link);
		entity.getLinks().addAll(links);

		// Events Data
		List<Event> events = new ArrayList<Event>();
		Event event1 = new EventDbObj();
		event1.setEventAction(EventAction.REGISTRATION);
		event1.setEventDate(new Date());

		Event event2 = new EventDbObj();
		event2.setEventAction(EventAction.LAST_CHANGED);
		event2.setEventDate(new Date());
		event2.setEventActor("joe@example.com");

		// event links data
		List<Link> eventLinks = new ArrayList<Link>();
		Link eventLink = new LinkDbObj();
		eventLink.setValue("eventLink1");
		eventLink.setRel("eventlink");
		eventLink.setHref("http://example.net/eventlink/xxxx");
		eventLink.setType("application/rdap+json");
		eventLinks.add(eventLink);
		event2.setLinks(eventLinks);

		events.add(event1);
		events.add(event2);
		entity.getEvents().addAll(events);

		// PublicId data
		List<PublicId> listPids = new ArrayList<>();
		PublicId pid = new PublicIdDbObj();
		pid.setPublicId("dumy pid 1");
		pid.setType("dummy iana");
		PublicId pid2 = new PublicIdDbObj();
		pid.setPublicId("dumy pid 2");
		pid.setType("dummy IETF");
		listPids.add(pid);
		listPids.add(pid2);

		entity.getPublicIds().addAll(listPids);
		return entity;
	}

}
