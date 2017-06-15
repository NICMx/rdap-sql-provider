package mx.nic.rdap;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import mx.nic.rdap.core.catalog.EventAction;
import mx.nic.rdap.core.catalog.Role;
import mx.nic.rdap.core.catalog.Status;
import mx.nic.rdap.core.catalog.VariantRelation;
import mx.nic.rdap.core.db.DomainLabel;
import mx.nic.rdap.core.db.Entity;
import mx.nic.rdap.core.db.IpAddress;
import mx.nic.rdap.core.db.Nameserver;
import mx.nic.rdap.core.db.VariantName;
import mx.nic.rdap.core.db.struct.NameserverIpAddressesStruct;
import mx.nic.rdap.db.exception.InitializationException;
import mx.nic.rdap.sql.objects.DomainDbObj;
import mx.nic.rdap.sql.objects.EntityDbObj;
import mx.nic.rdap.sql.objects.EventDbObj;
import mx.nic.rdap.sql.objects.IpAddressDbObj;
import mx.nic.rdap.sql.objects.LinkDbObj;
import mx.nic.rdap.sql.objects.NameserverDbObj;
import mx.nic.rdap.sql.objects.PublicIdDbObj;
import mx.nic.rdap.sql.objects.RemarkDbObj;
import mx.nic.rdap.sql.objects.RemarkDescriptionDbObj;
import mx.nic.rdap.sql.objects.VCardDbObj;
import mx.nic.rdap.sql.objects.VCardPostalInfoDbObj;
import mx.nic.rdap.sql.objects.VariantDbObj;
import mx.nic.rdap.sql.test.DatabaseTest;
import mx.nic.rdap.store.model.DomainStoreModel;
import mx.nic.rdap.store.model.EntityStoreModel;
import mx.nic.rdap.store.model.NameserverStoreModel;
import mx.nic.rdap.store.model.ZoneStoreModel;

public class DummyDataBatch extends DatabaseTest {

	private static final String[] countryNames = { "mx", "us", "jp", "co", "cl", "kr", "ca", "es", "uk", "fr", "br" };
	private static String[] zones = { "mx", "lat", "com", "example", "org" };
	private static String[] domainNames = { "domain", "hol", "foo", "bar", "store" };
	private static final String[] IDNS = { "á", "é", "í", "ó", "ú", "ア", "イ", "ウ", "エ", "オ" };

	private static final String[] variantsA = { "ä", "à", "â" };
	private static final String[] variantsE = { "ë", "è", "ê" };
	private static final String[] variantsI = { "ï", "ì", "î" };
	private static final String[] variantsO = { "ö", "ò", "ô" };
	private static final String[] variantsU = { "ü", "ù", "û" };

	private static Map<String, String[]> variants;
	static {
		variants = new HashMap<>();
		variants.put("á", variantsA);
		variants.put("é", variantsE);
		variants.put("í", variantsI);
		variants.put("ó", variantsO);
		variants.put("ú", variantsU);
	}

	public static void main(String[] args) throws SQLException, InitializationException, IOException {
		DummyDataBatch batch = new DummyDataBatch();

		DummyDataBatch.init();
		batch.before();
		for (String zone : zones) {
			ZoneStoreModel.storeToDatabase(zone, connection);
		}
		try {
			batch.start();
		} catch (Exception e) {
			System.out.println("Error");
			e.printStackTrace();
			connection.rollback();
		}
		connection.commit();
		batch.after();
		DummyDataBatch.end();
	}

	private int entitiesIndex = 10;
	private int domainIndex = 10;
	private int RegistrarsIndex = 10;

	public void start() throws SQLException, UnknownHostException {

		int maxRegistrars = 30;
		for (int i = 0; i < maxRegistrars; i++) {
			createRegistrarData();
		}
	}

	private void createRegistrarData() throws SQLException, UnknownHostException {
		int registrantPerSponsor = 40;
		int domainPerRegistrant = 4;

		String zone = zones[ThreadLocalRandom.current().nextInt(zones.length)];
		Entity sponsor = createRegistrarsEntities(RegistrarsIndex++, zone);

		List<Entity> registrants = new ArrayList<>();
		for (int i = 0; i < registrantPerSponsor; i++) {
			Entity registrant = createRegistrants(sponsor, entitiesIndex++);
			registrants.add(registrant);
			for (int j = 0; j < domainPerRegistrant; j++) {
				createDomains(domainNames[ThreadLocalRandom.current().nextInt(domainNames.length)], zone, registrant,
						sponsor, domainIndex++);
			}
		}
	}

	private Entity createRegistrants(Entity sponsor, Integer index) throws SQLException {
		EntityDbObj entity = new EntityDbObj();
		String country = getRandomCountry();

		// HANDLE
		entity.setHandle("RGTRANT_" + index + "_" + getPositiveRandom());

		// STATUS
		entity.getStatus().add(Status.ACTIVE);

		// Remark Description
		RemarkDbObj remark = new RemarkDbObj();
		remark.setLanguage("en");
		remark.setTitle("Registrant TITLE " + index);

		RemarkDescriptionDbObj description = new RemarkDescriptionDbObj();
		description.setDescription("This is a  contact for RAR_" + index);
		description.setOrder(1);
		remark.getDescriptions().add(description);

		description = new RemarkDescriptionDbObj();
		description.setDescription("And other stuff about this instance");
		description.setOrder(2);
		remark.getDescriptions().add(description);

		entity.getRemarks().add(remark);

		// VCard
		VCardDbObj vcard = new VCardDbObj();
		vcard.setName("RGTRANT" + " " + index + " " + country);
		vcard.setCompanyName("RAR" + index + "_" + country);
		vcard.setVoice(getRandomVoice());
		VCardPostalInfoDbObj postalInfo = new VCardPostalInfoDbObj();
		postalInfo.setCountry(country);
		postalInfo.setCity(country + " city");
		postalInfo.setStreet1("street " + index + " " + country);
		postalInfo.setType("WORK");
		vcard.setPostalInfo(postalInfo);
		entity.getVCardList().add(vcard);

		// *****
		// List<Role> roles = sponsor.getRoles();

		List<Role> sponsorRoles = new ArrayList<>();
		sponsorRoles.add(Role.SPONSOR);
		sponsor.setRoles(sponsorRoles);

		entity.getEntities().add(sponsor);
		// *****

		EntityStoreModel.storeToDatabase(entity, connection);

		entity.getRoles().add(Role.REGISTRANT);

		return entity;
	}

	private void createDomains(String domainName, String tld, Entity registrant, Entity sponsor, Integer index)
			throws UnknownHostException, SQLException {
		boolean isIDN = getRandomBoolean();

		DomainDbObj domain = new DomainDbObj();
		domain.getEntities().add(registrant);
		domain.getEntities().add(sponsor);

		EventDbObj event = new EventDbObj();
		event.setEventAction(EventAction.REGISTRATION);
		event.setEventActor(registrant.getHandle());
		event.setEventDate(getRandomDate());
		domain.getEvents().add(event);

		domain.setHandle(domainName + index);

		String idnChar = IDNS[ThreadLocalRandom.current().nextInt(0, IDNS.length)];
		String ldhName = null;
		if (isIDN) {
			String unicodeName = domainName + idnChar + index;
			ldhName = DomainLabel.nameToASCII(unicodeName);
			domain.setLdhName(ldhName);
			domain.setUnicodeName(unicodeName);
		} else {
			ldhName = domainName + index;
			domain.setUnicodeName(ldhName);
			domain.setLdhName(ldhName);
		}
		domain.setZone(tld);

		domain.getStatus().add(Status.ACTIVE);

		int variantsSize = ThreadLocalRandom.current().nextInt(3 + 1);
		if (isIDN && variants.containsKey(idnChar) && variantsSize > 0) {
			VariantDbObj variant = new VariantDbObj();
			variant.getRelations().add(VariantRelation.REGISTRATION_RESTRICTED);
			variant.getRelations().add(VariantRelation.UNREGISTERED);
			for (int i = 0; i < variantsSize; i++) {
				VariantName vn = new VariantName();
				String[] variantsString = variants.get(idnChar);
				String variantName = domainName + variantsString[i] + index + "." + tld;
				vn.setLdhName(DomainLabel.nameToASCII(variantName));
				variant.getVariantNames().add(vn);
			}
			domain.getVariants().add(variant);
		}

		List<Nameserver> createNameservers = createNameservers(ldhName, tld, registrant);
		domain.setNameServers(createNameservers);

		DomainStoreModel.storeToDatabase(domain, false, connection);
	}

	private List<Nameserver> createNameservers(String domainName, String tld, Entity registrant)
			throws SQLException, UnknownHostException {
		List<Nameserver> nameservers = new ArrayList<Nameserver>();

		int numberOfNameservers = ThreadLocalRandom.current().nextInt(3);
		for (int index = 0; index < numberOfNameservers; index++) {
			Nameserver nameserver = new NameserverDbObj();
			String nsName = "ns" + index + "." + domainName + "." + tld;
			String ldhName = DomainLabel.nameToASCII(nsName);
			String unicodeName = DomainLabel.nameToUnicode(nsName);
			nameserver.setLdhName(ldhName);
			nameserver.setUnicodeName(unicodeName);
			nameserver.setHandle("NS_" + ldhName + index);

			List<Status> statusList = new ArrayList<Status>();
			statusList.add(Status.ACTIVE);
			if (getRandomBoolean())
				statusList.add(Status.ASSOCIATED);
			nameserver.setStatus(statusList);

			// IpAddressStruct data
			NameserverIpAddressesStruct ipAddresses = new NameserverIpAddressesStruct();
			int numberOfIp4 = ThreadLocalRandom.current().nextInt(0, 3);
			for (int indexIP = 0; indexIP < numberOfIp4; indexIP++) {
				IpAddress ipv41 = new IpAddressDbObj();
				ipv41.setAddress(InetAddress.getByName(getRandomIp()));
				ipv41.setType(4);
				ipAddresses.getIpv4Adresses().add(ipv41);
			}

			int numberOfIp6 = ThreadLocalRandom.current().nextInt(0, 3);
			for (int indexIP = 0; indexIP < numberOfIp6; indexIP++) {
				IpAddress ipv6 = new IpAddressDbObj();
				ipv6.setAddress(InetAddress.getByName("2001:db8::" + getRandomIp()));
				ipv6.setType(6);
				ipAddresses.getIpv6Adresses().add(ipv6);
				nameserver.setIpAddresses(ipAddresses);
			}

			nameserver.getEntities().add(registrant);

			NameserverStoreModel.storeToDatabase(nameserver, connection);
			nameservers.add(nameserver);
		}
		return nameservers;
	}

	private Entity createRegistrarsEntities(Integer index, String tld) throws SQLException {
		EntityDbObj registrar = new EntityDbObj();

		// HANDLE
		registrar.setHandle("RAR_" + index + "_" + getPositiveRandom());

		// LINKS
		LinkDbObj link = new LinkDbObj();
		link.setHref("www.rar-" + index + "." + tld);
		link.addHreflang("en");
		link.setValue("TOS");
		link.setType("link type");
		registrar.getLinks().add(link);

		registrar.setPort43("www.rar-" + index + "." + tld + "/whois");
		registrar.getRoles().add(Role.REGISTRAR);

		// Public ID
		PublicIdDbObj publicId = new PublicIdDbObj();
		publicId.setPublicId("RAR_" + index + "_" + tld);
		publicId.setType("DEVL_EXAMPLE");
		registrar.getPublicIds().add(publicId);

		// STATUS
		registrar.getStatus().add(Status.ACTIVE);

		// Remark Description
		RemarkDbObj remark = new RemarkDbObj();
		remark.setLanguage("en");
		remark.setTitle("RAR TITLE " + index);

		RemarkDescriptionDbObj description = new RemarkDescriptionDbObj();
		description.setDescription("This is a registrar remark");
		description.setOrder(1);
		remark.getDescriptions().add(description);

		description = new RemarkDescriptionDbObj();
		description.setDescription("Information about the registrar " + index);
		description.setOrder(2);
		remark.getDescriptions().add(description);

		description = new RemarkDescriptionDbObj();
		description.setDescription("And other stuff about this instance");
		description.setOrder(3);
		remark.getDescriptions().add(description);

		registrar.getRemarks().add(remark);

		// VCard
		VCardDbObj vcard = new VCardDbObj();
		vcard.setCompanyName("RAR" + index + "_" + tld);
		vcard.setName("RAR" + index + "_" + tld);
		vcard.setVoice(getRandomVoice());
		VCardPostalInfoDbObj postalInfo = new VCardPostalInfoDbObj();
		postalInfo.setCountry(tld);
		postalInfo.setCity(tld + " city");
		postalInfo.setStreet1("street " + index + " " + tld);
		postalInfo.setType("WORK");
		vcard.setPostalInfo(postalInfo);
		registrar.getVCardList().add(vcard);

		int contactsSize = ThreadLocalRandom.current().nextInt(1, 3 + 1);
		switch (contactsSize) {
		case 1:
			EntityDbObj rarContactEntity = getRarContactEntity(index, tld, Role.ADMINISTRATIVE, Role.TECHNICAL,
					Role.ABUSE);
			registrar.getEntities().add(rarContactEntity);
			break;
		case 2:
			EntityDbObj rarContactEntity2 = getRarContactEntity(index, tld, Role.ADMINISTRATIVE, Role.ABUSE);
			EntityDbObj rarContactEntity3 = getRarContactEntity(index, tld, Role.TECHNICAL);
			registrar.getEntities().add(rarContactEntity2);
			registrar.getEntities().add(rarContactEntity3);
			break;
		case 3:
		default:
			EntityDbObj rarContactEntity4 = getRarContactEntity(index, tld, Role.ADMINISTRATIVE);
			EntityDbObj rarContactEntity5 = getRarContactEntity(index, tld, Role.ABUSE);
			EntityDbObj rarContactEntity6 = getRarContactEntity(index, tld, Role.TECHNICAL);

			registrar.getEntities().add(rarContactEntity4);
			registrar.getEntities().add(rarContactEntity5);
			registrar.getEntities().add(rarContactEntity6);
			break;
		}

		EntityStoreModel.storeToDatabase(registrar, connection);
		return registrar;
	}

	private EntityDbObj getRarContactEntity(Integer index, String tld, Role... roles) {
		EntityDbObj entity = new EntityDbObj();

		// HANDLE
		entity.setHandle(roles[0].toString().substring(0, 3) + "_" + index);

		for (Role role : roles) {
			entity.getRoles().add(role);
		}

		// STATUS
		entity.getStatus().add(Status.ACTIVE);

		// Remark Description
		RemarkDbObj remark = new RemarkDbObj();
		remark.setLanguage("en");
		remark.setTitle("RAR TITLE " + index);

		RemarkDescriptionDbObj description = new RemarkDescriptionDbObj();
		description.setDescription("This is a  contact for RAR_" + index);
		description.setOrder(1);
		remark.getDescriptions().add(description);

		description = new RemarkDescriptionDbObj();
		description.setDescription("And other stuff about this instance");
		description.setOrder(2);
		remark.getDescriptions().add(description);

		entity.getRemarks().add(remark);

		// VCard
		VCardDbObj vcard = new VCardDbObj();
		vcard.setName(roles[0].toString() + " " + index + " " + tld);
		vcard.setCompanyName("RAR" + index + "_" + tld);
		vcard.setVoice(getRandomVoice());
		VCardPostalInfoDbObj postalInfo = new VCardPostalInfoDbObj();
		postalInfo.setCountry(tld);
		postalInfo.setCity(tld + " city");
		postalInfo.setStreet1("street " + index + " " + tld);
		postalInfo.setType("WORK");
		vcard.setPostalInfo(postalInfo);
		entity.getVCardList().add(vcard);

		return entity;
	}

	private String getRandomVoice() {
		return ThreadLocalRandom.current().nextInt(100_0000, 999_9999) + "";
	}

	private boolean getRandomBoolean() {
		return ThreadLocalRandom.current().nextBoolean();
	}

	private int getPositiveRandom() {
		return ThreadLocalRandom.current().nextInt(2_147_483_647);
	}

	private Date getRandomDate() {
		long nextLong = ThreadLocalRandom.current().nextLong(946706400, 1483250400);
		return Date.from(Instant.ofEpochSecond(nextLong));
	}

	private String getRandomIp() {
		int first = ThreadLocalRandom.current().nextInt(256);
		int second = ThreadLocalRandom.current().nextInt(256);
		int third = ThreadLocalRandom.current().nextInt(256);
		int fourth = ThreadLocalRandom.current().nextInt(256);
		return first + "." + second + "." + third + "." + fourth;
	}

	private String getRandomCountry() {
		return countryNames[ThreadLocalRandom.current().nextInt(0, countryNames.length)];
	}

}
