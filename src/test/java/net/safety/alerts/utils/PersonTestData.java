package net.safety.alerts.utils;

import java.util.ArrayList;
import java.util.List;

import net.safety.alerts.model.Person;

public class PersonTestData {

	public static Person buildPerson() {
		return buildPerson(TestConstants.firstName, TestConstants.lastName, TestConstants.address, TestConstants.city);
	}

	public static Person buildPerson(String firstName, String lastName, String address, String city) {
		Person person = new Person();
		person.setFirstName(firstName);
		person.setLastName(lastName);
		person.setAddress(address);
		person.setCity(city);
		return person;
	}

	public static List<Person> buildPersonList() {
		List<Person> personList = new ArrayList<>();
		personList.add(buildPerson(TestConstants.firstName, TestConstants.lastName, TestConstants.address,
				TestConstants.city));
		for (int i = 1; i < 20; i++) {
			personList.add(buildPerson(TestConstants.firstName + Integer.toString(i),
					TestConstants.lastName + Integer.toString(i), TestConstants.address + Integer.toString(i),
					TestConstants.city + Integer.toString(i)));
		}
		return personList;
	}

}
