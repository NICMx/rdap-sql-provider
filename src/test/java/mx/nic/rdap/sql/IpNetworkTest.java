package mx.nic.rdap.sql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import mx.nic.rdap.core.catalog.EventAction;
import mx.nic.rdap.core.catalog.Status;
import mx.nic.rdap.core.db.Event;
import mx.nic.rdap.core.db.IpNetwork;
import mx.nic.rdap.core.db.Link;
import mx.nic.rdap.core.db.Remark;
import mx.nic.rdap.core.db.RemarkDescription;
import mx.nic.rdap.core.ip.AddressBlock;
import mx.nic.rdap.core.ip.IpAddressFormatException;
import mx.nic.rdap.core.ip.IpUtils;
import mx.nic.rdap.sql.model.EntityModel;
import mx.nic.rdap.sql.model.IpNetworkModel;
import mx.nic.rdap.sql.objects.EventDbObj;
import mx.nic.rdap.sql.objects.IpNetworkDbObj;
import mx.nic.rdap.sql.objects.LinkDbObj;
import mx.nic.rdap.sql.objects.RemarkDbObj;
import mx.nic.rdap.sql.objects.RemarkDescriptionDbObj;

/**
 * Tests for the {@link EntityModel}
 * 
 */
public class IpNetworkTest extends DatabaseTest {

	/**
	 * Creates a simple ipNetwork object and store it in the database, then get
	 * the same ip object from the database by an IP, finally compares the first
	 * objects with the objects in the database
	 */
	@Test
	public void insertAndGetSimpleObject() throws SQLException, IpAddressFormatException {
		// create local instances;
		IpNetwork ipNetwork = createInstance("192.168.1.0", 24, "home-ip", "type", "MX", null, "client-1234-home-ip");

		IpNetworkModel.storeToDatabase(ipNetwork, connection);

		// Query the database
		IpNetwork byIp = IpNetworkModel.getByAddressBlock(new AddressBlock("192.168.1.0", 26), connection);

		// Compares the results
		Assert.assertTrue("getByIp fails", ipNetwork.equals(byIp));
	}

	/**
	 * Creates a simple ipNetwork object and store it in the database, then get
	 * the same ip object from the database by an IP, finally compares the first
	 * objects with the objects in the database
	 */
	@Test
	public void insertAndGetSimpleObjectIpv6() throws SQLException, IpAddressFormatException {
		// create local instances;
		IpNetwork ipNetwork = createInstance("2001:BABA:CAFE:0003::", 64, "home-ip", "type", "MX", null,
				"client-1234-home-ip");

		// Store it in the database
		IpNetworkModel.storeToDatabase(ipNetwork, connection);

		// Query the database
		IpNetwork byIp = IpNetworkModel.getByAddressBlock(new AddressBlock("2001:BABA:CAFE:0003::"), connection);

		// Compares the results
		Assert.assertTrue("getByIp fails", ipNetwork.equals(byIp));
	}

	/**
	 * Creates a simple ipNetwork object and store it in the database, then get
	 * the same ip object from the database by an IP, finally compares the first
	 * objects with the objects in the database
	 */
	@Test
	public void insertAndGetComplexObject() throws SQLException, IpAddressFormatException {
		// create local instances;
		IpNetwork ipNetwork = createInstance("192.168.1.0", 24, "home-ip", "type", "MX", null, "client-1234-home-ip");

		// Status data
		List<Status> statusList = new ArrayList<Status>();
		statusList.add(Status.ACTIVE);
		statusList.add(Status.ASSOCIATED);
		ipNetwork.setStatus(statusList);

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
		ipNetwork.getRemarks().addAll(remarks);

		// Links data
		List<Link> links = new ArrayList<Link>();
		Link link = new LinkDbObj();
		link.setValue("http://example.net/ip/192.168.1.0/24");
		link.setRel("self");
		link.setHref("http://example.net/ip/192.168.1.0/24");
		link.setType("application/rdap+json");
		links.add(link);
		ipNetwork.getLinks().addAll(links);

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
		ipNetwork.getEvents().addAll(events);

		// Store it in the database
		IpNetworkModel.storeToDatabase(ipNetwork, connection);

		// Query the database
		IpNetwork byIp = IpNetworkModel.getByAddressBlock(new AddressBlock("192.168.1.0", 26), connection);

		// Compares the results
		Assert.assertTrue("getByIp fails", ipNetwork.equals(byIp));
	}

	@Test
	public void insertAndGetComplexV6Object() throws SQLException, IpAddressFormatException {
		// create local instances;
		IpNetwork ipNetwork = createInstance("2001:BABA:CAFE:0003::", 64, "home-ip", "type", "MX", null,
				"client-1234-home-ip");

		// Status data
		List<Status> statusList = new ArrayList<Status>();
		statusList.add(Status.ACTIVE);
		statusList.add(Status.ASSOCIATED);
		ipNetwork.setStatus(statusList);

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
		ipNetwork.getRemarks().addAll(remarks);

		// Links data
		List<Link> links = new ArrayList<Link>();
		Link link = new LinkDbObj();
		link.setValue("http://example.net/ip/192.168.1.0/24");
		link.setRel("self");
		link.setHref("http://example.net/ip/192.168.1.0/24");
		link.setType("application/rdap+json");
		links.add(link);
		ipNetwork.getLinks().addAll(links);

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
		ipNetwork.getEvents().addAll(events);

		// Store it in the database
		IpNetworkModel.storeToDatabase(ipNetwork, connection);

		// Query the database
		IpNetwork byIp = IpNetworkModel.getByAddressBlock(new AddressBlock("2001:BABA:CAFE:0003::"), connection);

		// Compares the results
		Assert.assertTrue("getByIp fails", ipNetwork.equals(byIp));
	}

	private static IpNetwork createInstance(String startAddress, Integer cidr, String name, String type, String country,
			String parentHandle, String handle) throws IpAddressFormatException {
		IpNetwork ipNetwork = new IpNetworkDbObj();
		AddressBlock block = new AddressBlock(IpUtils.parseAddress(startAddress), cidr);

		ipNetwork.setHandle(handle);
		ipNetwork.setIpVersion(block.getIpVersion());
		ipNetwork.setStartAddress(block.getAddress());
		ipNetwork.setEndAddress(block.getLastAddress());
		ipNetwork.setName(name);
		ipNetwork.setType(type);
		ipNetwork.setCountry(country);
		ipNetwork.setParentHandle(parentHandle);
		ipNetwork.setPrefix(block.getPrefix());
		return ipNetwork;
	}

}
