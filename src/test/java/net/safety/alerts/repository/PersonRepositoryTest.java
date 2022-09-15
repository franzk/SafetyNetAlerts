package net.safety.alerts.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.model.Person;

public class PersonRepositoryTest {

	private static PersonRepository personRepositoryUnderTest;

	private final String testFirstName = "Angus";
	private final String testLastName = "Young";
	private final String testAdress = "666, Highway to hell";

	@BeforeAll
	public static void setUp() {
		personRepositoryUnderTest = new PersonRepository();
	}

	@BeforeEach
	public void reset() {
		personRepositoryUnderTest.setListPersons(new ArrayList<Person>());
	}

	public Person buildPerson(String firstName, String lastName, String address) {
		Person person = new Person();
		person.setFirstName(firstName);
		person.setLastName(lastName);
		person.setAddress(address);
		return person;
	}

	public List<Person> buildPersonList(String firstName, String lastName, String address) {
		List<Person> personList = new ArrayList<>();
		personList.add(buildPerson(firstName, lastName, address));
		for (int i = 1; i < 20; i++) {
			personList.add(buildPerson(firstName + Integer.toString(i), lastName + Integer.toString(i),
					address + Integer.toString(i)));
		}
		return personList;
	}

	@Test
	public void setListPersonsAndGetPersonByNameTest() {
		// Arrange
		List<Person> personList = buildPersonList(testFirstName, testLastName, testAdress);

		// Act
		personRepositoryUnderTest.setListPersons(personList);

		List<Person> result = personRepositoryUnderTest.getPersonsByName(testFirstName, testLastName);

		// Assert
		assertThat(result.get(0).getAddress()).isEqualTo(testAdress);

	}

	@Test
	public void addPersonTest() {
		// Arrange
		Person person = buildPerson(testFirstName, testLastName, testAdress);

		// Act
		personRepositoryUnderTest.addPerson(person);
		List<Person> result = personRepositoryUnderTest.getPersonsByName(testFirstName, testLastName);

		// Assert
		assertThat(result.get(0).getAddress()).isEqualTo(testAdress);
	}

	@Test
	public void updatePersonTest() {
		// Arrange
		Person person1 = buildPerson(testFirstName, testLastName, testAdress + " 1");
		Person person2 = buildPerson(testFirstName, testLastName, testAdress);
		personRepositoryUnderTest.addPerson(person1);

		// Act
		try {
			personRepositoryUnderTest.updatePerson(person2);
		} catch (PersonNotFoundException e) {
			fail("updatePersonTest threw an exception !");
		}

		// Assert
		assertThat(personRepositoryUnderTest.getPersonsByName(testFirstName, testLastName).get(0).getAddress())
				.isEqualTo(testAdress);
	}

	@Test
	public void getPersonsByAddressTest() {
		// Arrange
		List<Person> personList = buildPersonList(testFirstName, testLastName, testAdress);
		personRepositoryUnderTest.setListPersons(personList);

		// Act
		List<Person> result = personRepositoryUnderTest.getPersonsByAddress(testAdress);

		// Assert
		assertThat(result.get(0)).isEqualTo(personList.get(0));

	}

	@Test
	public void deletePersonTest() {
		// Arrange
		List<Person> personList = buildPersonList(testFirstName, testLastName, testAdress);
		Person personToDelete = personList.get(5);
		personRepositoryUnderTest.setListPersons(personList);

		// Act
		try {
			personRepositoryUnderTest.deletePerson(personToDelete);
		} catch (PersonNotFoundException e) {
			fail("deletePersonTest threw an exception !");
		}

		// Assert
		assertThat(personRepositoryUnderTest
				.getPersonsByName(personToDelete.getFirstName(), personToDelete.getLastName()).size()).isEqualTo(0);
	}

	@Test
	public void deleteByNameTest() { // Arrange List<Person> personList =
		List<Person> personList = buildPersonList(testFirstName, testLastName, testAdress);
		Person personToDelete = personList.get(5);
		personRepositoryUnderTest.setListPersons(personList);

		// Act
		try {
			personRepositoryUnderTest.deletePersonByName(personToDelete.getFirstName(), personToDelete.getLastName());
		} catch (PersonNotFoundException e) {
			fail("deletePersonTest threw an exception !");
		}

		// Assert
		assertThat(personRepositoryUnderTest
				.getPersonsByName(personToDelete.getFirstName(), personToDelete.getLastName()).size()).isEqualTo(0);

	}

	@Test
	public void updatePersonExceptionTest() {
		// Arrange
		Person person1 = buildPerson(testFirstName, testLastName, testAdress + " 1");
		Person person2 = buildPerson("wrong first name", testLastName, testAdress);
		personRepositoryUnderTest.addPerson(person1);

		// Act + Assert
		assertThrows(PersonNotFoundException.class, () -> personRepositoryUnderTest.updatePerson(person2));

	}

	@Test
	public void deletePersonExceptionTest() {
		// Arrange
		List<Person> personList = buildPersonList(testFirstName, testLastName, testAdress);
		Person personToDelete = buildPerson("wrong first name", "wrong last name", "wrong address");
		personRepositoryUnderTest.setListPersons(personList);

		// Act + Assert
		assertThrows(PersonNotFoundException.class, () -> personRepositoryUnderTest.deletePerson(personToDelete));
	}

	@Test
	public void deleteByNameExceptionTest() { // Arrange List<Person> personList =
		List<Person> personList = buildPersonList(testFirstName, testLastName, testAdress);
		personRepositoryUnderTest.setListPersons(personList);

		// Act + Assert
		assertThrows(PersonNotFoundException.class, () -> personRepositoryUnderTest.deletePersonByName("wrong first name", "wrong last name"));	

	}

}
