package net.safety.alerts.utils;

/**
 * Constants used to choose which fields are exported to Json results file.
 * 
 * @author FranzKa
 *
 */
public class DtoConstants {

	public static enum PersonField {
		FIRST_NAME, LAST_NAME, ADDRESS, CITY, ZIP, PHONE, EMAIL, AGE, MEDICATIONS, ALLERGIES
	}

	// preset of fields for "/childAlert" URL (Child DTO)
	public static PersonField[] UrlChildAlertChild = new PersonField[] { PersonField.FIRST_NAME, PersonField.LAST_NAME,
			PersonField.AGE };

	// preset of fields for "/childAlert" URL (Adult DTO)
	public static PersonField[] UrlChildAlertAdult = new PersonField[] { PersonField.FIRST_NAME,
			PersonField.LAST_NAME };

	// preset of fields for "/fire" URL
	public static PersonField[] UrlFirePerson = new PersonField[] { PersonField.FIRST_NAME, PersonField.LAST_NAME,
			PersonField.PHONE, PersonField.AGE, PersonField.MEDICATIONS, PersonField.ALLERGIES };

	// preset of fields for "/firestationCoverage" URL
	public static PersonField[] UrlFirestationCoveragePerson = new PersonField[] { PersonField.FIRST_NAME,
			PersonField.LAST_NAME, PersonField.ADDRESS, PersonField.PHONE };

	// preset of fields for "/personInfo" URL
	public static PersonField[] UrlPersonInfo = new PersonField[] { PersonField.FIRST_NAME, PersonField.LAST_NAME,
			PersonField.ADDRESS, PersonField.AGE, PersonField.EMAIL, PersonField.MEDICATIONS, PersonField.ALLERGIES };
}
