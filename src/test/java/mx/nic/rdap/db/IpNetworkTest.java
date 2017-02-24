package mx.nic.rdap.db;

import static org.junit.Assert.fail;

import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import mx.nic.rdap.IpUtils;
import mx.nic.rdap.core.catalog.EventAction;
import mx.nic.rdap.core.catalog.IpVersion;
import mx.nic.rdap.core.catalog.Status;
import mx.nic.rdap.core.db.Event;
import mx.nic.rdap.core.db.IpNetwork;
import mx.nic.rdap.core.db.Link;
import mx.nic.rdap.core.db.Remark;
import mx.nic.rdap.core.db.RemarkDescription;
import mx.nic.rdap.db.exception.InvalidValueException;
import mx.nic.rdap.db.exception.ObjectNotFoundException;
import mx.nic.rdap.db.model.EntityModel;
import mx.nic.rdap.db.model.IpNetworkModel;
import mx.nic.rdap.db.objects.EventDbObj;
import mx.nic.rdap.db.objects.IpNetworkDbObj;
import mx.nic.rdap.db.objects.LinkDbObj;
import mx.nic.rdap.db.objects.RemarkDbObj;
import mx.nic.rdap.db.objects.RemarkDescriptionDbObj;

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
	public void insertAndGetSimpleObject() {
		// create local instances;
		IpNetwork ipNetwork;
		try {
			ipNetwork = createInstance(IpVersion.V4, "192.168.1.0", "192.168.1.255", "home-ip", "type", "MX", null, 24,
					"client-1234-home-ip");
		} catch (InvalidValueException e1) {
			throw new RuntimeException(e1);
		}

		try {
			IpNetworkModel.storeToDatabase(ipNetwork, connection);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// Query the database
		IpNetwork byIp = null;
		try {
			byIp = IpNetworkModel.getByInetAddress("192.168.1.1", 26, connection);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// Compares the results
		Assert.assertTrue("getByIp fails", ipNetwork.equals(byIp));

		// check if exists
		try {
			IpNetworkModel.existByInetAddress("192.168.1.0", connection);
		} catch (SQLException | ObjectNotFoundException | InvalidValueException e) {
			fail("fail existByInetAddress");
		}
	}

	/**
	 * Creates a simple ipNetwork object and store it in the database, then get
	 * the same ip object from the database by an IP, finally compares the first
	 * objects with the objects in the database
	 */
	@Test
	public void insertAndGetSimpleObjectIpv6() {
		// create local instances;
		IpNetwork ipNetwork;
		try {
			ipNetwork = createInstance(IpVersion.V6, "2001:BABA:CAFE:0003::", null, "home-ip", "type", "MX", null, 64,
					"client-1234-home-ip");
			ipNetwork.setEndAddress(IpUtils.getLastAddressFromNetwork(ipNetwork.getStartAddress(), 64));
		} catch (InvalidValueException | UnknownHostException e1) {
			throw new RuntimeException(e1);
		}

		// Store it in the database
		try {
			IpNetworkModel.storeToDatabase(ipNetwork, connection);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// Query the database
		IpNetwork byIp = null;
		try {
			byIp = IpNetworkModel.getByInetAddress("2001:BABA:CAFE:0003::", connection);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// Compares the results
		Assert.assertTrue("getByIp fails", ipNetwork.equals(byIp));

		// check if exists
		try {
			IpNetworkModel.existByInetAddress("2001:BABA:CAFE:0003::", connection);
		} catch (SQLException | ObjectNotFoundException | InvalidValueException e) {
			fail("fail existByInetAddress");
		}
	}

	/**
	 * Creates a simple ipNetwork object and store it in the database, then get
	 * the same ip object from the database by an IP, finally compares the first
	 * objects with the objects in the database
	 */
	@Test
	public void insertAndGetComplexObject() {
		// create local instances;
		IpNetwork ipNetwork;
		try {
			ipNetwork = createInstance(IpVersion.V4, "192.168.1.0", "192.168.1.255", "home-ip", "type", "MX", null, 24,
					"client-1234-home-ip");
		} catch (InvalidValueException e1) {
			throw new RuntimeException(e1);
		}

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
		try {
			IpNetworkModel.storeToDatabase(ipNetwork, connection);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// Query the database
		IpNetwork byIp = null;
		try {
			byIp = IpNetworkModel.getByInetAddress("192.168.1.1", 26, connection);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// Compares the results
		Assert.assertTrue("getByIp fails", ipNetwork.equals(byIp));
	}

	@Test
	public void insertAndGetComplexV6Object() {
		// create local instances;
		IpNetwork ipNetwork;
		try {
			ipNetwork = createInstance(IpVersion.V6, "2001:BABA:CAFE:0003::", null, "home-ip", "type", "MX", null, 64,
					"client-1234-home-ip");
			ipNetwork.setEndAddress(IpUtils.getLastAddressFromNetwork(ipNetwork.getStartAddress(), 64));
		} catch (InvalidValueException | UnknownHostException e1) {
			throw new RuntimeException(e1);
		}

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
		try {
			IpNetworkModel.storeToDatabase(ipNetwork, connection);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// Query the database
		IpNetwork byIp = null;
		try {
			byIp = IpNetworkModel.getByInetAddress("2001:BABA:CAFE:0003::", connection);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// Compares the results
		Assert.assertTrue("getByIp fails", ipNetwork.equals(byIp));
	}

	public static IpNetwork createInstance(IpVersion ipVersion, String startAddress, String endAddress, String name,
			String type, String country, String parentHandle, Integer cidr, String handle)
			throws InvalidValueException {
		IpNetwork ipNetwork = new IpNetworkDbObj();

		ipNetwork.setHandle(handle);
		ipNetwork.setIpVersion(ipVersion);
		if (startAddress != null)
			ipNetwork.setStartAddress(IpUtils.validateIpAddress(startAddress));
		if (endAddress != null)
			ipNetwork.setEndAddress(IpUtils.validateIpAddress(endAddress));
		ipNetwork.setName(name);
		ipNetwork.setType(type);
		ipNetwork.setCountry(country);
		ipNetwork.setParentHandle(parentHandle);
		ipNetwork.setCidr(cidr);
		return ipNetwork;
	}

}
