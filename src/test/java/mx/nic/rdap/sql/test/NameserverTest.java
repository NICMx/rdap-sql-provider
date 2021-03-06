package mx.nic.rdap.sql.test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import mx.nic.rdap.core.catalog.EventAction;
import mx.nic.rdap.core.catalog.Status;
import mx.nic.rdap.core.db.DomainLabel;
import mx.nic.rdap.core.db.DomainLabelException;
import mx.nic.rdap.core.db.Event;
import mx.nic.rdap.core.db.IpAddress;
import mx.nic.rdap.core.db.Link;
import mx.nic.rdap.core.db.Nameserver;
import mx.nic.rdap.core.db.Remark;
import mx.nic.rdap.core.db.RemarkDescription;
import mx.nic.rdap.core.db.struct.NameserverIpAddressesStruct;
import mx.nic.rdap.db.exception.http.NotImplementedException;
import mx.nic.rdap.sql.model.NameserverModel;
import mx.nic.rdap.sql.objects.EventDbObj;
import mx.nic.rdap.sql.objects.IpAddressDbObj;
import mx.nic.rdap.sql.objects.LinkDbObj;
import mx.nic.rdap.sql.objects.NameserverDbObj;
import mx.nic.rdap.sql.objects.RemarkDbObj;
import mx.nic.rdap.sql.objects.RemarkDescriptionDbObj;
import mx.nic.rdap.store.model.NameserverStoreModel;

/**
 * Test for the Nameserver object
 * 
 */
public class NameserverTest extends DatabaseTest {

	@Test
	public void insertMinimunNameServer() throws SQLException, DomainLabelException, NotImplementedException {

		// Nameserver base data
		Nameserver nameserver = new NameserverDbObj();
		nameserver.setHandle("xx1");
		nameserver.setLdhName("ns.xn--test-minumun.example");
		NameserverStoreModel.storeToDatabase(nameserver, connection);
		System.out.println(nameserver);

		DomainLabel label = new DomainLabel("ns.xn--test-minumun.example");
		NameserverModel.findByName(label, connection);
	}

	@Test
	public void insert() throws SQLException, UnknownHostException {

		// Nameserver base data
		Nameserver nameserver = new NameserverDbObj();
		nameserver.setHandle("XXX13");
		nameserver.setLdhName("ns1.xn--fo-5ja.example");
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
		NameserverStoreModel.storeToDatabase(nameserver, connection);

		assert true;
	}

}
