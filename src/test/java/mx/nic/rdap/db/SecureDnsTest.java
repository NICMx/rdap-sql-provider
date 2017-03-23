package mx.nic.rdap.db;

import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.junit.Test;

import mx.nic.rdap.core.catalog.EventAction;
import mx.nic.rdap.core.catalog.Rol;
import mx.nic.rdap.core.db.Domain;
import mx.nic.rdap.core.db.DsData;
import mx.nic.rdap.core.db.Entity;
import mx.nic.rdap.core.db.Event;
import mx.nic.rdap.core.db.KeyData;
import mx.nic.rdap.core.db.Link;
import mx.nic.rdap.core.db.SecureDNS;
import mx.nic.rdap.db.exception.RequiredValueNotFoundException;
import mx.nic.rdap.db.model.DomainModel;
import mx.nic.rdap.db.model.EntityModel;
import mx.nic.rdap.db.model.SecureDNSModel;
import mx.nic.rdap.db.model.ZoneModel;
import mx.nic.rdap.db.objects.DomainDbObj;
import mx.nic.rdap.db.objects.DsDataDbObj;
import mx.nic.rdap.db.objects.EntityDbObj;
import mx.nic.rdap.db.objects.EventDbObj;
import mx.nic.rdap.db.objects.KeyDataDbObj;
import mx.nic.rdap.db.objects.LinkDbObj;
import mx.nic.rdap.db.objects.SecureDNSDbObj;

/**
 * Test for the class SecureDNS
 */
public class SecureDnsTest extends DatabaseTest {

	@Test
	public void insertAndGetMinimum() {
		Domain dom = createSimpleDomain();
		Long domainId = dom.getId();

		SecureDNS secureDns = getSecureDns(null, domainId, true, false, null, null);

		try {
			SecureDNSModel.storeToDatabase(secureDns, connection);
		} catch (SQLException | RequiredValueNotFoundException e) {
			e.printStackTrace();
			fail();
		}

		SecureDNS byDomain = null;
		try {
			byDomain = SecureDNSModel.getByDomain(domainId, connection);
		} catch (SQLException e) {
			e.printStackTrace();
			fail();
		}

		Assert.assertTrue("getByName fails", secureDns.equals(byDomain));
	}

	@Test
	public void insertAndGetComplex() {
		Domain dom = createSimpleDomain();
		Long domainId = dom.getId();

		List<DsData> dsDataList = new ArrayList<>();

		// ******** DS DATA INFO *******
		// Links data
		List<Link> links = new ArrayList<Link>();
		Link link = new LinkDbObj();
		link.setValue("http://example.net/nameserver/xxxx");
		link.setRel("self");
		link.setHref("http://example.net/nameserver/xxxx");
		link.setType("application/rdap+json");
		links.add(link);

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

		DsData dsData = getDsData(null, null, 66612, 1, "ABCDEF1234", 1, links, events);
		DsData dsData2 = getDsData(null, null, 1234, 1, "abcd5432", 1, null, null);
		dsDataList.add(dsData);
		dsDataList.add(dsData2);
		// ******** ENDS DS DATA INFO *******

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

		KeyData keyData1 = getKeyData(null, null, 1, 2, "publickkey1", 3, keyLinks, keyEvents);
		KeyData keyData2 = getKeyData(null, null, 2, 3, "publicKey2", 4, null, null);
		List<KeyData> keyDataList = new ArrayList<>();
		keyDataList.add(keyData1);
		keyDataList.add(keyData2);
		// -------------- ENDS KEY DATA INFO ----------

		SecureDNS secureDns = getSecureDns(null, domainId, true, true, dsDataList, keyDataList);

		try {
			SecureDNSModel.storeToDatabase(secureDns, connection);
		} catch (SQLException | RequiredValueNotFoundException e) {
			e.printStackTrace();
			fail();
		}

		SecureDNS byDomain = null;
		try {
			byDomain = SecureDNSModel.getByDomain(domainId, connection);
		} catch (SQLException e) {
			e.printStackTrace();
			fail();
		}

		Assert.assertTrue("getByDomainId fails", secureDns.equals(byDomain));

	}

	public static SecureDNSDbObj getSecureDns(Long id, Long domainId, boolean zoneSigned, boolean delegationSigned,
			List<DsData> dsData, List<KeyData> keyDataList) {
		SecureDNSDbObj sDns = new SecureDNSDbObj();
		sDns.setId(id);
		sDns.setZoneSigned(zoneSigned);
		sDns.setDelegationSigned(delegationSigned);
		sDns.setDomainId(domainId);
		if (dsData != null)
			sDns.getDsData().addAll(dsData);

		if (Objects.nonNull(keyDataList)) {
			sDns.getKeyData().addAll(keyDataList);
		}

		return sDns;
	}

	public static DsDataDbObj getDsData(Long id, Long secureDNSId, Integer keytag, Integer algorithm, String digest,
			Integer digestType, List<Link> links, List<Event> events) {
		DsDataDbObj ds = new DsDataDbObj();

		ds.setId(id);
		ds.setSecureDNSId(secureDNSId);
		ds.setKeytag(keytag);
		ds.setAlgorithm(algorithm);
		ds.setDigest(digest);
		ds.setDigestType(digestType);
		if (events != null)
			ds.getEvents().addAll(events);
		if (links != null)
			ds.getLinks().addAll(links);

		return ds;
	}

	public static KeyDataDbObj getKeyData(Long id, Long secureDNSid, Integer flags, Integer protocol, String key,
			Integer algorithm, List<Link> links, List<Event> events) {

		KeyDataDbObj keyData = new KeyDataDbObj();

		keyData.setId(id);
		keyData.setSecureDNSId(secureDNSid);
		keyData.setFlags(flags);
		keyData.setProtocol(protocol);
		keyData.setPublicKey(key);
		keyData.setAlgorithm(algorithm);

		if (Objects.nonNull(links)) {
			keyData.getLinks().addAll(links);
		}

		if (Objects.nonNull(events)) {
			keyData.getEvents().addAll(events);
		}

		return keyData;
	}

	public static SecureDNSDbObj createDefaultSDNS() {
		List<DsData> dsDataList = new ArrayList<>();

		// Links data
		List<Link> links = new ArrayList<Link>();
		Link link = new LinkDbObj();
		link.setValue("http://example.net/nameserver/xxxx");
		link.setRel("self");
		link.setHref("http://example.net/nameserver/xxxx");
		link.setType("application/rdap+json");
		links.add(link);

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

		DsData dsData = getDsData(null, null, 66612, 1, "ABCDEF1234", 1, links, events);
		DsData dsData2 = getDsData(null, null, 1234, 1, "abcd5432", 1, null, null);
		dsDataList.add(dsData);
		dsDataList.add(dsData2);

		KeyData keyData1 = getKeyData(null, null, 1, 2, "publickkey1", 3, links, events);
		KeyData keyData2 = getKeyData(null, null, 2, 3, "publicKey2", 4, null, null);
		List<KeyData> keyDataList = new ArrayList<>();
		keyDataList.add(keyData1);
		keyDataList.add(keyData2);

		SecureDNS secureDns = getSecureDns(null, null, true, true, dsDataList, keyDataList);
		return (SecureDNSDbObj) secureDns;
	}

	private static Domain createSimpleDomain() {

		Entity registrar = new EntityDbObj();
		registrar.setHandle("whois");
		registrar.setPort43("whois.mx");
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

		Domain dom = new DomainDbObj();
		dom.getEntities().add(ent);
		dom.getEntities().add(registrar);
		dom.setHandle("domcommx");
		dom.setLdhName("mydomaintest.mx");

		String zoneName = "mx";
		try {
			ZoneModel.storeToDatabase(zoneName, connection);
		} catch (SQLException e1) {
			e1.printStackTrace();
			fail(e1.toString());
		}
		dom.setZone(zoneName);

		// SecureDNSDAO secureDNS = SecureDnsTest.getSecureDns(null, null,
		// false, false, null);
		// dom.setSecureDNS(secureDNS);

		try {
			DomainModel.storeToDatabase(dom, false, connection);
		} catch (SQLException | RequiredValueNotFoundException e) {
			e.printStackTrace();
			fail();
		}

		return dom;
	}
}
