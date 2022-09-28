package net.safety.alerts.utils;

import java.util.ArrayList;
import java.util.List;

import net.safety.alerts.model.Person;

public class BuildPersonTestData {

	public Person getPerson() {
		return getPerson(TestConstants.firstName, TestConstants.lastName, TestConstants.address, TestConstants.city);
	}

	public Person getPerson(String firstName, String lastName, String address, String city) {
		Person person = new Person();
		person.setFirstName(firstName);
		person.setLastName(lastName);
		person.setAddress(address);
		person.setCity(city);
		return person;
	}

	public List<Person> getPersonList() {
		List<Person> personList = new ArrayList<>();
		personList.add(getPerson(TestConstants.firstName, TestConstants.lastName, TestConstants.address,
				TestConstants.city));
		for (int i = 1; i < 20; i++) {
			personList.add(getPerson(TestConstants.firstName + Integer.toString(i),
					TestConstants.lastName + Integer.toString(i), TestConstants.address + Integer.toString(i),
					TestConstants.city + Integer.toString(i)));
		}
		return personList;
	}

}
