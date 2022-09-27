package net.safety.alerts.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.safety.alerts.exceptions.AddressNotFoundException;
import net.safety.alerts.exceptions.CityNotFoundException;
import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.model.Person;
import net.safety.alerts.utils.TestConstants;

public class PersonRepositoryTest {

	private PersonRepository personRepositoryUnderTest = new PersonRepository();

	@BeforeEach
	public void reset() {
		personRepositoryUnderTest.setListPersons(new ArrayList<Person>());
	}

	public Person buildPerson(String firstName, String lastName, String address, String city) {
		Person person = new Person();
		person.setFirstName(firstName);
		person.setLastName(lastName);
		person.setAddress(address);
		person.setCity(city);
		return person;
	}

	public List<Person> buildPersonList(String firstName, String lastName, String address, String city) {
		List<Person> personList = new ArrayList<>();
		personList.add(buildPerson(firstName, lastName, address, city));
		for (int i = 1; i < 20; i++) {
			personList.add(buildPerson(firstName + Integer.toString(i), lastName + Integer.toString(i),
					address + Integer.toString(i), city + Integer.toString(i)));
		}
		return personList;
	}

	@Test
	public void setListPersonsAndGetPersonByNameTest() {
		// Arrange
		List<Person> personList = buildPersonList(TestConstants.firstName, TestConstants.lastName,
				TestConstants.address, TestConstants.city);

		// Act
		personRepositoryUnderTest.setListPersons(personList);

		try {
			Person p = personRepositoryUnderTest.getPersonByName(TestConstants.firstName, TestConstants.lastName);

			// Assert
			assertThat(p.getAddress()).isEqualTo(TestConstants.address);

		} catch (PersonNotFoundException e) {
			fail("setListPersonsAndGetPersonByNameTest threw an exception");
		}

	}

	@Test
	public void addPersonTest() {
		// Arrange
		Person person = buildPerson(TestConstants.firstName, TestConstants.lastName, TestConstants.address,
				TestConstants.city);

		// Act
		personRepositoryUnderTest.addPerson(person);
		try {
			Person p = personRepositoryUnderTest.getPersonByName(TestConstants.firstName, TestConstants.lastName);

			// Assert
			assertThat(p.getAddress()).isEqualTo(TestConstants.address);
		} catch (PersonNotFoundException e) {
			fail("addPersonTest threw an exception");
		}

	}

	@Test
	public void getPersonByNameTest() {
		// Arrange
		List<Person> personList = buildPersonList(TestConstants.firstName, TestConstants.lastName,
				TestConstants.address, TestConstants.city);
		personRepositoryUnderTest.setListPersons(personList);
		Person testPerson = personList.get(5);
		personList.add(testPerson);

		// Act
		List<Person> result = new ArrayList<>();
		try {
			result = personRepositoryUnderTest.getPersonsByName(testPerson.getFirstName(), testPerson.getLastName());
		} catch (PersonNotFoundException e) {
			fail("getPersonsByAddressTest threw an exception !");
		}

		// Assert
		assertThat(result.size()).isEqualTo(2);
		assertThat(result.get(0)).isEqualTo(testPerson);
		assertThat(result.get(1)).isEqualTo(testPerson);

	}

	@Test
	public void getPersonsByAddressesTest() {
		// Arrange
		List<Person> personList = buildPersonList(TestConstants.firstName, TestConstants.lastName,
				TestConstants.address, TestConstants.city);
		personRepositoryUnderTest.setListPersons(personList);

		List<String> addresses = new ArrayList<>();
		addresses.add(personList.get(2).getAddress());
		addresses.add(personList.get(3).getAddress());

		// Act
		List<Person> result = personRepositoryUnderTest.getPersonsByAddresses(addresses);

		// Assert
		assertThat(result.size()).isEqualTo(2);
		assertThat(result.get(0)).isEqualTo(personList.get(2));
		assertThat(result.get(1)).isEqualTo(personList.get(3));

	}

	@Test
	public void getEmailsByCityTest() {
		// Arrange
		List<Person> personList = buildPersonList(TestConstants.firstName, TestConstants.lastName,
				TestConstants.address, TestConstants.city);
		Person testPerson = personList.get(5);
		personList.get(6).setCity(testPerson.getCity());

		personRepositoryUnderTest.setListPersons(personList);

		List<String> emails = new ArrayList<>();

		// Act
		try {
			emails = personRepositoryUnderTest.getEmailsByCity(testPerson.getCity());
		} catch (CityNotFoundException e) {
			fail("getEmailsByCityTest threw an exception");
		}

		// Assert
		assertThat(emails.size()).isEqualTo(2);
		assertThat(emails.get(0)).isEqualTo(testPerson.getEmail());
		assertThat(emails.get(1)).isEqualTo(personList.get(6).getEmail());

	}

	@Test
	public void getPersonsByNameExceptionTest() {
		assertThrows(PersonNotFoundException.class, () -> personRepositoryUnderTest.getPersonsByName("no", "matter"));
	}

	@Test
	public void getPersonsByAddressExceptionTest() {
		assertThrows(AddressNotFoundException.class, () -> personRepositoryUnderTest.getPersonsByAddress("no matter"));
	}

	@Test
	public void getEmailsByCityExceptionTest() {
		assertThrows(CityNotFoundException.class, () -> personRepositoryUnderTest.getEmailsByCity("no matter"));
	}

	@Test
	public void updatePersonTest() {
		// Arrange
		Person person1 = buildPerson(TestConstants.firstName, TestConstants.lastName, TestConstants.address + " 1",
				TestConstants.city);
		Person person2 = buildPerson(TestConstants.firstName, TestConstants.lastName, TestConstants.address,
				TestConstants.city);
		personRepositoryUnderTest.addPerson(person1);

		// Act
		try {
			personRepositoryUnderTest.updatePerson(person2);
		} catch (PersonNotFoundException e) {
			fail("updatePersonTest (act) threw an exception !");
		}

		// Assert
		try {
			Person p = personRepositoryUnderTest.getPersonByName(TestConstants.firstName, TestConstants.lastName);

			// Assert
			assertThat(p.getAddress()).isEqualTo(TestConstants.address);

		} catch (PersonNotFoundException e) {
			fail("updatePersonTest (assert) threw an exception !");
		}
	}

	@Test
	public void getPersonsByAddressTest() {
		// Arrange
		List<Person> personList = buildPersonList(TestConstants.firstName, TestConstants.lastName,
				TestConstants.address, TestConstants.city);
		personRepositoryUnderTest.setListPersons(personList);

		// Act
		List<Person> result = new ArrayList<>();
		try {
			result = personRepositoryUnderTest.getPersonsByAddress(TestConstants.address);
		} catch (AddressNotFoundException e) {
			fail("getPersonsByAddressTest threw an exception !");
		}

		// Assert
		assertThat(result.get(0)).isEqualTo(personList.get(0));

	}

	@Test
	public void deletePersonTest() {
		// Arrange
		List<Person> personList = buildPersonList(TestConstants.firstName, TestConstants.lastName,
				TestConstants.address, TestConstants.city);
		Person personToDelete = personList.get(5);
		personRepositoryUnderTest.setListPersons(personList);

		// Act
		try {
			personRepositoryUnderTest.deletePerson(personToDelete);
		} catch (PersonNotFoundException e) {
			fail("deletePersonTest (act) threw an exception !");
		}

		// Assert
		assertThrows(PersonNotFoundException.class, () -> personRepositoryUnderTest
				.getPersonByName(personToDelete.getFirstName(), personToDelete.getLastName()));
	}

	@Test
	public void deleteByNameTest() {

		// Arrange
		List<Person> personList = buildPersonList(TestConstants.firstName, TestConstants.lastName,
				TestConstants.address, TestConstants.city);
		Person personToDelete = personList.get(5);
		personRepositoryUnderTest.setListPersons(personList);

		// Act
		try {
			personRepositoryUnderTest.deletePersonByName(personToDelete.getFirstName(), personToDelete.getLastName());
		} catch (PersonNotFoundException e) {
			fail("deleteByNameTest threw an exception !");
		}

		// Assert
		assertThrows(PersonNotFoundException.class, () -> personRepositoryUnderTest
				.getPersonByName(personToDelete.getFirstName(), personToDelete.getLastName()));
	}

	@Test
	public void updatePersonExceptionTest() {
		// Arrange
		Person person1 = buildPerson(TestConstants.firstName, TestConstants.lastName, TestConstants.address + " 1",
				TestConstants.city);
		Person person2 = buildPerson("wrong first name", TestConstants.lastName, TestConstants.address,
				TestConstants.city);
		personRepositoryUnderTest.addPerson(person1);

		// Act + Assert
		assertThrows(PersonNotFoundException.class, () -> personRepositoryUnderTest.updatePerson(person2));

	}

	@Test
	public void deletePersonExceptionTest() {
		// Arrange
		List<Person> personList = buildPersonList(TestConstants.firstName, TestConstants.lastName,
				TestConstants.address, TestConstants.city);
		Person personToDelete = buildPerson("wrong first name", "wrong last name", "wrong address", TestConstants.city);
		personRepositoryUnderTest.setListPersons(personList);

		// Act + Assert
		assertThrows(PersonNotFoundException.class, () -> personRepositoryUnderTest.deletePerson(personToDelete));
	}

	@Test
	public void deleteByNameExceptionTest() {
		// Arrange
		List<Person> personList = buildPersonList(TestConstants.firstName, TestConstants.lastName,
				TestConstants.address, TestConstants.city);
		personRepositoryUnderTest.setListPersons(personList);

		// Act + Assert
		assertThrows(PersonNotFoundException.class,
				() -> personRepositoryUnderTest.deletePersonByName("wrong first name", "wrong last name"));

	}

}
