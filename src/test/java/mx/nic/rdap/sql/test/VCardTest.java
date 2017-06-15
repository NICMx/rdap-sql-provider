package mx.nic.rdap.sql.test;

import mx.nic.rdap.core.db.VCardPostalInfo;
import mx.nic.rdap.sql.model.VCardModel;
import mx.nic.rdap.sql.objects.VCardDbObj;
import mx.nic.rdap.sql.objects.VCardPostalInfoDbObj;

/**
 * Tests for the {@link VCardModel}
 * 
 */
public class VCardTest extends DatabaseTest {

	// TODO I removed two tests from this module.
	// Both were getById() tests, which was troublesome because the model no
	// longer exports that function. Now there's getByEntityId(), but I don't
	// have time to create a new test.

	/**
	 * Creates a new instance of {@link VCardDbObj} with the incoming attributes.
	 */
	public static VCardDbObj createVCardDao(Long id, String name, String companyName, String companyURL, String email,
			String voice, String cellphone, String fax, String jobTitle) {
		VCardDbObj vCard = new VCardDbObj();
		vCard.setId(id);
		vCard.setName(name);
		vCard.setCompanyName(companyName);
		vCard.setCompanyURL(companyURL);
		vCard.setEmail(email);
		vCard.setVoice(voice);
		vCard.setCellphone(cellphone);
		vCard.setFax(fax);
		vCard.setJobTitle(jobTitle);
		return vCard;
	}

	/**
	 * Creates a new instance of {@link VCardPostalInfoDbObj} with the incoming
	 * attributes.
	 */
	public static VCardPostalInfo createVCardPostalInfo(Long id, Long vCardId, String type, String country, String city,
			String street1, String street2, String street3, String state, String postalCode) {
		VCardPostalInfoDbObj postalInfo = new VCardPostalInfoDbObj();
		postalInfo.setId(id);
		postalInfo.setVCardId(vCardId);
		postalInfo.setType(type);
		postalInfo.setCountry(country);
		postalInfo.setCity(city);
		postalInfo.setStreet1(street1);
		postalInfo.setStreet2(street2);
		postalInfo.setStreet3(street3);
		postalInfo.setState(state);
		postalInfo.setPostalCode(postalCode);

		return postalInfo;
	}
}
