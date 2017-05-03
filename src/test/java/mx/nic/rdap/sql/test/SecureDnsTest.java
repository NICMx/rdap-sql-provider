package mx.nic.rdap.sql.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.junit.Test;

import mx.nic.rdap.core.catalog.EventAction;
import mx.nic.rdap.core.catalog.Role;
import mx.nic.rdap.core.db.Domain;
import mx.nic.rdap.core.db.DsData;
import mx.nic.rdap.core.db.Entity;
import mx.nic.rdap.core.db.Event;
import mx.nic.rdap.core.db.KeyData;
import mx.nic.rdap.core.db.Link;
import mx.nic.rdap.core.db.SecureDNS;
import mx.nic.rdap.sql.model.SecureDNSModel;
import mx.nic.rdap.sql.objects.DomainDbObj;
import mx.nic.rdap.sql.objects.DsDataDbObj;
import mx.nic.rdap.sql.objects.EntityDbObj;
import mx.nic.rdap.sql.objects.EventDbObj;
import mx.nic.rdap.sql.objects.KeyDataDbObj;
import mx.nic.rdap.sql.objects.LinkDbObj;
import mx.nic.rdap.sql.objects.SecureDNSDbObj;
import mx.nic.rdap.store.model.DomainStoreModel;
import mx.nic.rdap.store.model.EntityStoreModel;
import mx.nic.rdap.store.model.SecureDNSStoreModel;
import mx.nic.rdap.store.model.ZoneStoreModel;

/**
 * Test for the class SecureDNS
 */
public class SecureDnsTest extends DatabaseTest {

	@Test
	public void insertAndGetMinimum() throws SQLException {
		Domain dom = createSimpleDomain();
		Long domainId = dom.getId();

		SecureDNS secureDns = getSecureDns(null, domainId, true, false, null, null);
		SecureDNSStoreModel.storeToDatabase(secureDns, connection);

		SecureDNS byDomain = SecureDNSModel.getByDomain(domainId, connection);
		Assert.assertTrue("getByName fails", secureDns.equals(byDomain));
	}

	@Test
	public void insertAndGetComplex() throws SQLException {
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
		SecureDNSStoreModel.storeToDatabase(secureDns, connection);

		SecureDNS byDomain = SecureDNSModel.getByDomain(domainId, connection);
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

		// SecureDNSDAO secureDNS = SecureDnsTest.getSecureDns(null, null,
		// false, false, null);
		// dom.setSecureDNS(secureDNS);

		DomainStoreModel.storeToDatabase(dom, false, connection);

		return dom;
	}
}
