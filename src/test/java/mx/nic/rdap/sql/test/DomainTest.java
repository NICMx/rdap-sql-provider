package mx.nic.rdap.sql.test;

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
import mx.nic.rdap.core.catalog.Role;
import mx.nic.rdap.core.catalog.Status;
import mx.nic.rdap.core.catalog.VariantRelation;
import mx.nic.rdap.core.db.Domain;
import mx.nic.rdap.core.db.DsData;
import mx.nic.rdap.core.db.Entity;
import mx.nic.rdap.core.db.Event;
import mx.nic.rdap.core.db.IpAddress;
import mx.nic.rdap.core.db.KeyData;
import mx.nic.rdap.core.db.Link;
import mx.nic.rdap.core.db.Nameserver;
import mx.nic.rdap.core.db.PublicId;
import mx.nic.rdap.core.db.Remark;
import mx.nic.rdap.core.db.RemarkDescription;
import mx.nic.rdap.core.db.SecureDNS;
import mx.nic.rdap.core.db.Variant;
import mx.nic.rdap.core.db.VariantName;
import mx.nic.rdap.core.db.struct.NameserverIpAddressesStruct;
import mx.nic.rdap.db.exception.http.NotImplementedException;
import mx.nic.rdap.sql.model.DomainModel;
import mx.nic.rdap.sql.model.EntityModel;
import mx.nic.rdap.sql.objects.DomainDbObj;
import mx.nic.rdap.sql.objects.EntityDbObj;
import mx.nic.rdap.sql.objects.EventDbObj;
import mx.nic.rdap.sql.objects.IpAddressDbObj;
import mx.nic.rdap.sql.objects.LinkDbObj;
import mx.nic.rdap.sql.objects.NameserverDbObj;
import mx.nic.rdap.sql.objects.PublicIdDbObj;
import mx.nic.rdap.sql.objects.RemarkDbObj;
import mx.nic.rdap.sql.objects.RemarkDescriptionDbObj;
import mx.nic.rdap.sql.objects.VariantDbObj;
import mx.nic.rdap.store.model.DomainStoreModel;
import mx.nic.rdap.store.model.EntityStoreModel;
import mx.nic.rdap.store.model.NameserverStoreModel;
import mx.nic.rdap.store.model.ZoneStoreModel;

/**
 * Test for {@link DomainModel}
 * 
 */
public class DomainTest extends DatabaseTest {

	@Test
	public void insertAndGetSimpleDomain() throws SQLException, NotImplementedException {

		Domain dom = new DomainDbObj();
		dom.setHandle("dummyhandle");
		dom.setLdhName("ninio");

		String zoneName = "example";
		Integer zoneId = ZoneStoreModel.storeToDatabase(zoneName, connection);
		dom.setZone(zoneName);

		DomainStoreModel.storeToDatabase(dom, false, connection);

		Domain findByLdhName = DomainModel.findByLdhName(dom.getLdhName(), zoneId, false, connection);
		System.out.println(findByLdhName.getLdhName());

		// Compares the results
		Assert.assertTrue("findByLdhName fails", dom.equals(findByLdhName));
	}

	@Test
	/**
	 * Inserts a domain and retrieves it
	 */
	public void insertDomainAndGet() throws SQLException, UnknownHostException, NotImplementedException {
		Random random = new Random();
		int randomInt = random.nextInt();

		String domainName = "foo" + randomInt;
		DomainDbObj domain = new DomainDbObj();

		Entity registrar = new EntityDbObj();
		registrar.setHandle("rar_dhfelix");
		registrar.setPort43("whois.dhfelixrar.mx");
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

		domain.getEntities().add(ent);
		domain.getEntities().add(registrar);
		List<Nameserver> nameservers = new ArrayList<Nameserver>();
		nameservers = createDefaultNameservers(randomInt);
		domain.setNameServers(nameservers);

		// Creates and inserts a zone
		Integer zoneId = ZoneStoreModel.storeToDatabase("mx", connection);

		domain.setZone("mx");
		domain.setLdhName(domainName);
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

		// -------------- KEY DATA INFO ----------
		// Links data
		List<Link> keyLinks = new ArrayList<Link>();
		Link keyLink = new LinkDbObj();
		keyLink.setValue("http://example.net/nameserver/xxxx");
		keyLink.setRel("self");
		keyLink.setHref("http://example.net/nameserver/xxxx");
		keyLink.setType("application/rdap+json");
		keyLinks.add(keyLink);

		// Events Data
		List<Event> keyEvents = new ArrayList<Event>();
		Event keyEvent1 = new EventDbObj();
		keyEvent1.setEventAction(EventAction.REGISTRATION);
		keyEvent1.setEventDate(new Date());

		Event keyEvent2 = new EventDbObj();
		keyEvent2.setEventAction(EventAction.LAST_CHANGED);
		keyEvent2.setEventDate(new Date());
		keyEvent2.setEventActor("joe@example.com");

		// event links data
		List<Link> keyEventLinks = new ArrayList<Link>();
		Link keyEventLink = new LinkDbObj();
		keyEventLink.setValue("eventLink1");
		keyEventLink.setRel("eventlink");
		keyEventLink.setHref("http://example.net/eventlink/xxxx");
		keyEventLink.setType("application/rdap+json");
		keyEventLinks.add(keyEventLink);
		keyEvent2.setLinks(keyEventLinks);

		keyEvents.add(keyEvent1);
		keyEvents.add(keyEvent2);

		KeyData keyData1 = SecureDnsTest.getKeyData(null, null, 2, 3, "publicKey2", 4, null, null);
		KeyData keyData2 = SecureDnsTest.getKeyData(null, null, 2, 3, "publicKey2", 4, null, null);
		List<KeyData> keyDataList = new ArrayList<>();
		keyDataList.add(keyData1);
		keyDataList.add(keyData2);
		// -------------- ENDS KEY DATA INFO ----------

		SecureDNS secureDns = SecureDnsTest.getSecureDns(null, null, true, true, dsDataList, keyDataList);
		domain.setSecureDNS(secureDns);

		DomainStoreModel.storeToDatabase(domain, false, connection);

		try (Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery("SELECT * FROM rdap.domain_entity_roles");
			resultSet.next();
		}

		Domain findByLdhName = DomainModel.findByLdhName(domain.getLdhName(), zoneId, false, connection);
		// Compares the results
		Assert.assertTrue("findByLdhName fails", domain.equals(findByLdhName));

	}

	public static List<Nameserver> createDefaultNameservers(int randomInt) throws UnknownHostException, SQLException {
		List<Nameserver> nameservers = new ArrayList<Nameserver>();
		Nameserver nameserver = new NameserverDbObj();
		nameserver.setHandle("XXXX73532" + randomInt);
		nameserver.setLdhName("ns1.myns" + randomInt + ".example");
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
		NameserverStoreModel.storeToDatabase(nameserver, connection);
		return nameservers;
	}

	public static Entity createDefaultEntity(Connection connection) throws NotImplementedException {
		// Entity base data

		// Create local instances
		Entity entity = createEntity(null, "ent_dhfelix", null, null, null);

		try {
			Entity byHandle = EntityModel.getByHandle(entity.getHandle(), connection);
			return byHandle;
		} catch (SQLException e) {
			e.printStackTrace();
			fail();
		}

		// TODO WTF? this is all unreachable code.

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

		List<Role> listRoles = new ArrayList<>();
		Role role = Role.REGISTRAR;
		listRoles.add(role);
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

	public static VariantName createVariantName(String ldhName) {
		VariantName variantName = new VariantName();
		variantName.setLdhName(ldhName);
		return variantName;
	}

}
