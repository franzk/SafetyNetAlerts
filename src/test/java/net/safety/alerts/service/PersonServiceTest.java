package net.safety.alerts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.model.Person;
import net.safety.alerts.repository.PersonRepository;
import net.safety.alerts.utils.PersonTestData;

public class PersonServiceTest {

	private PersonService personServiceUnderTest = new PersonService();

	private PersonRepository personRepository;

	@BeforeEach
	public void reset() {
		personRepository = new PersonRepository();
		ReflectionTestUtils.setField(personServiceUnderTest, "personRepository", personRepository);
	}

	@Test
	public void addAndGetByNameTest() {
		// Arrange
		Person testPerson = PersonTestData.buildPerson();

		// Act
		personServiceUnderTest.add(testPerson);

		// Assert
		Person result = new Person();
		try {
			result = personServiceUnderTest.getPersonByName(testPerson.getFirstName(), testPerson.getLastName());
		} catch (PersonNotFoundException e) {
			fail("addAndGetByNameTest threw an exception");
		}
		assertThat(result).isEqualTo(testPerson);

	}

	@Test
	public void getPersonsWithSameNameTest() {
		// Arrange
		Person testPerson = populateRepositoryAndReturnTestPerson();
		personServiceUnderTest.add(testPerson);

		// Act
		List<Person> result = new ArrayList<>();
		try {
			result = personServiceUnderTest.getPersonsWithSameName(testPerson.getFirstName(), testPerson.getLastName());
		} catch (PersonNotFoundException e) {
			fail("getPersonsWithSameNameTest threw an exception");
		}

		// Assert
		assertThat(result.size()).isEqualTo(2);
		assertThat(result.get(0).getLastName()).isEqualTo(result.get(1).getLastName());
	}

	@Test
	public void getPersonByNameTest() {
		// Arrange
		Person testPerson = populateRepositoryAndReturnTestPerson();

		// Act
		Person result = new Person();
		try {
			result = personServiceUnderTest.getPersonByName(testPerson.getFirstName(), testPerson.getLastName());
		} catch (PersonNotFoundException e) {
			fail("getPersonByNameTest threw an exception");
		}

		// Assert
		assertThat(result).isEqualTo(testPerson);
	}

	@Test
	public void updateTest() {
		// Arrange
		Person testPerson = PersonTestData.buildPerson();
		personRepository.addPerson(testPerson);

		Person testPersonUpdated = PersonTestData.buildPerson();
		testPersonUpdated.setCity("updated City");
		testPersonUpdated.setZip("updated Zip");

		// Act
		try {
			personServiceUnderTest.update(testPersonUpdated);
		} catch (PersonNotFoundException e) {
			fail("updateTest threw an exception");
		}

		// Assert
		Person result = new Person();
		try {
			result = personServiceUnderTest.getPersonByName(testPerson.getFirstName(), testPerson.getLastName());
		} catch (PersonNotFoundException e) {
			fail("updateTest");
		}

		// Assert
		assertThat(result).isEqualTo(testPersonUpdated);
	}

	@Test
	public void deleteTest() {
		// Arrange
		Person testPerson = populateRepositoryAndReturnTestPerson();

		// Act
		try {
			personServiceUnderTest.delete(testPerson.getFirstName(), testPerson.getLastName());
		} catch (PersonNotFoundException e) {
			fail("deleteTest threw an exception");
		}

		// Assert
		assertThrows(PersonNotFoundException.class,
				() -> personServiceUnderTest.getPersonByName(testPerson.getFirstName(), testPerson.getLastName()));
	}

	private Person populateRepositoryAndReturnTestPerson() {
		List<Person> listPerson = PersonTestData.buildPersonList();
		for (Person p : listPerson) {
			personServiceUnderTest.add(p);
		}
		Person testPerson = listPerson.get(5);
		return testPerson;
	}

}
