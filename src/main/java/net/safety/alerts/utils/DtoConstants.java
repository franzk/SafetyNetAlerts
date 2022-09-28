package net.safety.alerts.utils;

public class DtoConstants {

	public static enum PersonField {
		FIRST_NAME, LAST_NAME, ADDRESS, CITY, ZIP, PHONE, EMAIL, AGE, MEDICATIONS, ALLERGIES
	}

	public static PersonField[] UrlChildAlertChild = new PersonField[] { PersonField.FIRST_NAME, PersonField.LAST_NAME,
			PersonField.AGE };

	public static PersonField[] UrlChildAlertAdult = new PersonField[] { PersonField.FIRST_NAME,
			PersonField.LAST_NAME };

	public static PersonField[] UrlFirePerson = new PersonField[] { PersonField.FIRST_NAME, PersonField.LAST_NAME,
			PersonField.PHONE, PersonField.AGE, PersonField.MEDICATIONS, PersonField.ALLERGIES };

	public static PersonField[] UrlFirestationCoveragePerson = new PersonField[] { PersonField.FIRST_NAME,
			PersonField.LAST_NAME, PersonField.ADDRESS, PersonField.PHONE };

	public static PersonField[] UrlPersonInfo = new PersonField[] { PersonField.FIRST_NAME, PersonField.LAST_NAME,
			PersonField.ADDRESS, PersonField.AGE, PersonField.EMAIL, PersonField.MEDICATIONS, PersonField.ALLERGIES };
}
