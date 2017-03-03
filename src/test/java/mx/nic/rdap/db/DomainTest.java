package mx.nic.rdap.db;

import static org.junit.Assert.fail;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import mx.nic.rdap.core.catalog.EventAction;
import mx.nic.rdap.core.catalog.Rol;
import mx.nic.rdap.core.catalog.Status;
import mx.nic.rdap.core.catalog.VariantRelation;
import mx.nic.rdap.core.db.Domain;
import mx.nic.rdap.core.db.DsData;
import mx.nic.rdap.core.db.Entity;
import mx.nic.rdap.core.db.Event;
import mx.nic.rdap.core.db.IpAddress;
import mx.nic.rdap.core.db.Link;
import mx.nic.rdap.core.db.Nameserver;
import mx.nic.rdap.core.db.PublicId;
import mx.nic.rdap.core.db.Remark;
import mx.nic.rdap.core.db.RemarkDescription;
import mx.nic.rdap.core.db.SecureDNS;
import mx.nic.rdap.core.db.Variant;
import mx.nic.rdap.core.db.VariantName;
import mx.nic.rdap.core.db.struct.NameserverIpAddressesStruct;
import mx.nic.rdap.db.exception.ObjectNotFoundException;
import mx.nic.rdap.db.exception.RequiredValueNotFoundException;
import mx.nic.rdap.db.model.DomainModel;
import mx.nic.rdap.db.model.EntityModel;
import mx.nic.rdap.db.model.NameserverModel;
import mx.nic.rdap.db.model.ZoneModel;
import mx.nic.rdap.db.objects.DomainDbObj;
import mx.nic.rdap.db.objects.EntityDbObj;
import mx.nic.rdap.db.objects.EventDbObj;
import mx.nic.rdap.db.objects.IpAddressDbObj;
import mx.nic.rdap.db.objects.LinkDbObj;
import mx.nic.rdap.db.objects.NameserverDbObj;
import mx.nic.rdap.db.objects.PublicIdDbObj;
import mx.nic.rdap.db.objects.RemarkDbObj;
import mx.nic.rdap.db.objects.RemarkDescriptionDbObj;
import mx.nic.rdap.db.objects.VariantDbObj;

/**
 * Test for {@link DomainModel}
 * 
 */
public class DomainTest extends DatabaseTest {

	@Test
	public void insertAndGetSimpleDomain() {

		Domain dom = new DomainDbObj();
		dom.setHandle("dummyhandle");
		dom.setPunycodeName("ninio");

		Integer zoneId = null;
		String zoneName = "example";
		try {
			zoneId = ZoneModel.storeToDatabase(zoneName, connection);
		} catch (SQLException e1) {
			e1.printStackTrace();
			fail(e1.toString());
		}
		dom.setZone(zoneName);

		Long domId = null;
		try {
			domId = DomainModel.storeToDatabase(dom, false, connection);
		} catch (SQLException | RequiredValueNotFoundException | ObjectNotFoundException e) {
			e.printStackTrace();
			fail();
		}

		Domain domainById = null;
		Domain findByLdhName = null;
		try {
			domainById = DomainModel.getDomainById(domId, false, connection);
			findByLdhName = DomainModel.findByLdhName(dom.getLdhName(), zoneId, false, connection);
			System.out.println(findByLdhName.getLdhName());
		} catch (SQLException | ObjectNotFoundException e) {
			e.printStackTrace();
			fail();
		}

		// Compares the results
		Assert.assertTrue("getById fails", dom.equals(domainById));
		Assert.assertTrue("findByLdhName fails", dom.equals(findByLdhName));

		try {
			DomainModel.findByLdhName(dom.getLdhName(), zoneId, false, connection);
		} catch (SQLException | ObjectNotFoundException s) {
			s.printStackTrace();
			fail();
		}
	}

	@Test
	/**
	 * Inserts a domain and retrieves it
	 */
	public void insertDomainAndGet() {
		Random random = new Random();
		int randomInt = random.nextInt();

		String domainName = "foo" + randomInt;
		DomainDbObj domain = new DomainDbObj();

		Entity registrar = new EntityDbObj();
		registrar.setHandle("rar_dhfelix");
		registrar.setPort43("whois.dhfelixrar.mx");
		registrar.getRoles().add(Rol.SPONSOR);

		Entity ent = new EntityDbObj();
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

		domain.getEntities().add(ent);
		domain.getEntities().add(registrar);
		List<Nameserver> nameservers = new ArrayList<Nameserver>();
		try {
			nameservers = createDefaultNameservers(randomInt);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		domain.setNameServers(nameservers);

		// Creates and inserts a zone
		Integer zoneId = null;
		try {
			zoneId = ZoneModel.storeToDatabase("mx", connection);
		} catch (SQLException e1) {
			e1.printStackTrace();
			fail(e1.toString());
		}

		domain.setZone("mx");
		domain.setPunycodeName(domainName);
		domain.setSecureDNS(SecureDnsTest.createDefaultSDNS());

		// Creates and inserts a list of variants into the domain
		List<Variant> variants = new ArrayList<Variant>();

		List<VariantRelation> relations1 = new ArrayList<VariantRelation>();
		relations1.add(VariantRelation.REGISTERED);
		relations1.add(VariantRelation.CONJOINED);
		List<VariantName> variantNames1 = new ArrayList<VariantName>();
		variantNames1.add(DomainTest.createVariantName("xn--fo-cka" + randomInt + ".mx"));
		variantNames1.add(DomainTest.createVariantName("xn--fo-fka" + randomInt + ".mx"));

		List<VariantRelation> relations2 = new ArrayList<VariantRelation>();
		relations2.add(VariantRelation.UNREGISTERED);
		relations2.add(VariantRelation.REGISTRATION_RESTRICTED);
		List<VariantName> variantNames2 = new ArrayList<VariantName>();
		variantNames2.add(DomainTest.createVariantName("xn--fo-8ja" + randomInt + ".mx"));

		variants.add(DomainTest.createVariant(null, relations1, variantNames1, null, null));
		variants.add(DomainTest.createVariant(null, relations2, variantNames2, null, ".EXAMPLE Spanish"));

		domain.getVariants().addAll(variants);

		domain.getStatus().add(Status.ACTIVE);
		domain.getStatus().add(Status.TRANSFER_PROHIBITED);

		// Creates and inserts default public id
		List<PublicId> listPids = new ArrayList<>();
		PublicId pid = new PublicIdDbObj();
		pid.setPublicId("dumy pid 1");
		pid.setType("dummy iana");
		PublicId pid2 = new PublicIdDbObj();
		pid.setPublicId("dumy pid 2");
		pid.setType("dummy IETF");
		listPids.add(pid);
		listPids.add(pid2);

		domain.getPublicIds().addAll(listPids);

		// Creates and inserts Remark data
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
		domain.getRemarks().addAll(remarks);

		// Links data
		List<Link> links = new ArrayList<Link>();
		Link link = new LinkDbObj();
		link.setValue("http://example.net/domain/xxxx");
		link.setRel("other");
		link.setHref("http://example.net/domain/xxxx");
		link.setType("application/rdap+json");
		links.add(link);
		domain.getLinks().addAll(links);

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
		domain.getEvents().addAll(events);

		domain.setHandle(domainName + "." + domain.getZone());

		List<DsData> dsDataList = new ArrayList<>();
		DsData dsData = SecureDnsTest.getDsData(null, null, 66612, 1, "ABCDEF1234", 1, null, null);
		DsData dsData2 = SecureDnsTest.getDsData(null, null, 1234, 1, "abcd5432", 1, null, null);
		dsDataList.add(dsData);
		dsDataList.add(dsData2);

		SecureDNS secureDns = SecureDnsTest.getSecureDns(null, null, true, true, dsDataList);
		domain.setSecureDNS(secureDns);

		Long domainId = null;
		try {
			domainId = DomainModel.storeToDatabase(domain, false, connection);
		} catch (SQLException | RequiredValueNotFoundException | ObjectNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		try (Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery("SELECT * FROM rdap.domain_entity_roles");
			resultSet.next();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		// Get domain By its id
		Domain domainById = null;
		Domain findByLdhName = null;
		try {
			domainById = DomainModel.getDomainById(domainId, false, connection);
			findByLdhName = DomainModel.findByLdhName(domain.getLdhName(), zoneId, false, connection);
		} catch (SQLException | ObjectNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// Compares the results
		Assert.assertTrue("getById fails", domain.equals(domainById));
		Assert.assertTrue("findByLdhName fails", domain.equals(findByLdhName));

	}

	public static List<Nameserver> createDefaultNameservers(int randomInt) throws UnknownHostException {
		List<Nameserver> nameservers = new ArrayList<Nameserver>();
		Nameserver nameserver = new NameserverDbObj();
		nameserver.setHandle("XXXX73532" + randomInt);
		nameserver.setPunycodeName("ns1.xn--fo-5ja" + randomInt + ".example");
		nameserver.setPort43("whois.example.net");

		// IpAddressStruct data
		NameserverIpAddressesStruct ipAddresses = new NameserverIpAddressesStruct();

		IpAddress ipv41 = new IpAddressDbObj();
		ipv41.setAddress(InetAddress.getByName("192.0.2.1"));
		ipv41.setType(4);
		ipAddresses.getIpv4Adresses().add(ipv41);

		IpAddress ipv42 = new IpAddressDbObj();
		ipv42.setAddress(InetAddress.getByName("192.0.2.2"));
		ipv42.setType(4);
		ipAddresses.getIpv4Adresses().add(ipv42);

		IpAddress ipv6 = new IpAddressDbObj();
		ipv6.setAddress(InetAddress.getByName("2001:db8::123"));
		ipv6.setType(6);
		ipAddresses.getIpv6Adresses().add(ipv6);
		nameserver.setIpAddresses(ipAddresses);

		// Status data
		List<Status> statusList = new ArrayList<Status>();
		statusList.add(Status.ACTIVE);
		statusList.add(Status.ASSOCIATED);
		nameserver.setStatus(statusList);

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
		nameserver.setRemarks(remarks);

		// Links data
		List<Link> links = new ArrayList<Link>();
		Link link = new LinkDbObj();
		link.setValue("http://example.net/nameserver/xxxx");
		link.setRel("self");
		link.setHref("http://example.net/nameserver/xxxx");
		link.setType("application/rdap+json");
		links.add(link);
		nameserver.setLinks(links);

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
		nameserver.setEvents(events);
		nameservers.add(nameserver);
		try {
			NameserverModel.storeToDatabase(nameserver, connection);
		} catch (SQLException | RequiredValueNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		return nameservers;
	}

	public static Entity createDefaultEntity(Connection connection) {
		// Entity base data

		// Create local instances
		Entity entity = createEntity(null, "ent_dhfelix", null, null, null);

		try {
			Entity byHandle = EntityModel.getByHandle(entity.getHandle(), connection);
			return byHandle;
		} catch (SQLException | ObjectNotFoundException e) {
			e.printStackTrace();
			fail();
		}
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

		List<Rol> listRoles = new ArrayList<>();
		Rol rol = Rol.REGISTRAR;
		listRoles.add(rol);
		return entity;
	}

	public static EntityDbObj createEntity(Long id, String handle, String port43, Long rarId, Long vCardId) {
		EntityDbObj e = new EntityDbObj();
		e.setId(id);
		e.setHandle(handle);
		e.setPort43(port43);
		return e;
	}

	public static VariantDbObj createVariant(Long id, List<VariantRelation> relations, List<VariantName> variantNames,
			Long domainId, String idnTable) {
		VariantDbObj variant = new VariantDbObj();
		variant.setId(id);
		variant.setIdnTable(idnTable);
		variant.getRelations().addAll(relations);
		variant.getVariantNames().addAll(variantNames);
		variant.setDomainId(domainId);
		return variant;
	}

	public static VariantName createVariantName(String punycode) {
		VariantName variantName = new VariantName();
		variantName.setLdhName(punycode);
		return variantName;
	}

}
