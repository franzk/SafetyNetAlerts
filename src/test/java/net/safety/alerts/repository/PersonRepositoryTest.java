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
import net.safety.alerts.utils.BuildPersonTestData;
//import net.safety.alerts.utils.TestConstants;

public class PersonRepositoryTest {

	private PersonRepository personRepositoryUnderTest = new PersonRepository();

	BuildPersonTestData populatePersonRepository = new BuildPersonTestData();

	@BeforeEach
	public void reset() {
		personRepositoryUnderTest.setListPersons(new ArrayList<Person>());
	}

	@Test
	public void setListPersonsAndGetPersonByNameTest() {
		// Arrange
		List<Person> personList = populatePersonRepository.getPersonList();
		Person testPerson = personList.get(0);

		// Act
		personRepositoryUnderTest.setListPersons(personList);

		try {
			Person p = personRepositoryUnderTest.getPersonByName(testPerson.getFirstName(), testPerson.getLastName());

			// Assert
			assertThat(p.getAddress()).isEqualTo(testPerson.getAddress());

		} catch (PersonNotFoundException e) {
			fail("setListPersonsAndGetPersonByNameTest threw an exception");
		}

	}

	@Test
	public void addPersonTest() {
		// Arrange
		Person testPerson = populatePersonRepository.getPerson();

		// Act
		personRepositoryUnderTest.addPerson(testPerson);

		Person result = new Person();
		try {
			result = personRepositoryUnderTest.getPersonByName(testPerson.getFirstName(), testPerson.getLastName());
		} catch (PersonNotFoundException e) {
			fail("addPersonTest threw an exception");
		}

		// Assert
		assertThat(result.getAddress()).isEqualTo(testPerson.getAddress());
	}

	@Test
	public void getPersonByNameTest() {
		// Arrange
		List<Person> personList = populatePersonRepository.getPersonList();
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
		List<Person> personList = populatePersonRepository.getPersonList();
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
		List<Person> personList = populatePersonRepository.getPersonList();
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
		Person testPerson = populatePersonRepository.getPerson();
		personRepositoryUnderTest.addPerson(testPerson);

		Person testPersonUpdated = populatePersonRepository.getPerson();
		testPersonUpdated.setAddress("updated address");

		// Act
		try {
			personRepositoryUnderTest.updatePerson(testPersonUpdated);
		} catch (PersonNotFoundException e) {
			fail("updatePersonTest (act) threw an exception !");
		}

		// Assert
		Person result = new Person();
		try {
			result = personRepositoryUnderTest.getPersonByName(testPerson.getFirstName(), testPerson.getLastName());
		} catch (PersonNotFoundException e) {
			fail("updatePersonTest (assert) threw an exception !");
		}

		// Assert
		assertThat(result.getAddress()).isEqualTo(testPersonUpdated.getAddress());

	}

	@Test
	public void getPersonsByAddressTest() {
		// Arrange
		List<Person> personList = populatePersonRepository.getPersonList();
		String testAdress = personList.get(0).getAddress();
		personList.get(1).setAddress(testAdress);

		personRepositoryUnderTest.setListPersons(personList);

		// Act
		List<Person> result = new ArrayList<>();
		try {
			result = personRepositoryUnderTest.getPersonsByAddress(testAdress);
		} catch (AddressNotFoundException e) {
			fail("getPersonsByAddressTest threw an exception !");
		}

		// Assert
		assertThat(result.size()).isEqualTo(2);
		assertThat(result.get(0)).isEqualTo(personList.get(0));

	}

	@Test
	public void deletePersonTest() {
		// Arrange
		List<Person> personList = populatePersonRepository.getPersonList();
		personRepositoryUnderTest.setListPersons(personList);
		
		Person personToDelete = personList.get(5);
		

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
		List<Person> personList = populatePersonRepository.getPersonList();
		personRepositoryUnderTest.setListPersons(personList);
		
		Person personToDelete = personList.get(5);
		

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
		Person testPerson = populatePersonRepository.getPerson();
		testPerson.setFirstName("wrong FirstName");
		personRepositoryUnderTest.addPerson(testPerson);

		Person testPersonUpdated = populatePersonRepository.getPerson();
		testPersonUpdated.setAddress("updated address");

		// Act + Assert
		assertThrows(PersonNotFoundException.class, () -> personRepositoryUnderTest.updatePerson(testPersonUpdated));

	}

	@Test
	public void deletePersonExceptionTest() {
		// Arrange
		List<Person> personList = populatePersonRepository.getPersonList();
		personRepositoryUnderTest.setListPersons(personList);

		Person personToDelete = populatePersonRepository.getPerson("wrong first name", "wrong last name",
				"wrong address", "wrong city");

		// Act + Assert
		assertThrows(PersonNotFoundException.class, () -> personRepositoryUnderTest.deletePerson(personToDelete));
	}

	@Test
	public void deleteByNameExceptionTest() {
		// Arrange
		List<Person> personList = populatePersonRepository.getPersonList();
		personRepositoryUnderTest.setListPersons(personList);

		// Act + Assert
		assertThrows(PersonNotFoundException.class,
				() -> personRepositoryUnderTest.deletePersonByName("wrong first name", "wrong last name"));

	}

}
