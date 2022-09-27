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
import net.safety.alerts.utils.TestConstants;

public class PersonServiceTest {

	private PersonService personServiceUnderTest = new PersonService();

	private PersonRepository personRepository;

	@BeforeEach
	public void reset() {
		personRepository = new PersonRepository();
		ReflectionTestUtils.setField(personServiceUnderTest, "personRepository", personRepository);
	}

	private Person buildPerson() {
		return buildPerson(0);
	}

	private Person buildPerson(Integer modifier) {
		Person p = new Person();
		p.setFirstName(TestConstants.firstName + Integer.toString(modifier));
		p.setLastName(TestConstants.lastName + Integer.toString(modifier));
		p.setAddress(TestConstants.address + Integer.toString(modifier));
		p.setCity(TestConstants.city + Integer.toString(modifier));
		p.setZip(TestConstants.zip + Integer.toString(modifier));
		p.setPhone(TestConstants.phone + Integer.toString(modifier));
		return p;
	}

	private Person populatePersonRepository(Integer testPersonIndex) {
		Person testPerson = new Person();
		for (int i = 0; i < 24; i++) {
			Person p = buildPerson(i);
			personRepository.addPerson(p);
			if (i == testPersonIndex) {
				testPerson = p;
			}
		}
		return testPerson;
	}

	@Test
	public void addAndGetByNameTest() {
		// Arrange
		Person testPerson = buildPerson();
		personRepository.addPerson(testPerson);

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
		Person testPerson = populatePersonRepository(5);
		personRepository.addPerson(testPerson);

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
		Person testPerson = populatePersonRepository(5);

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
		Person testPerson = buildPerson();
		personRepository.addPerson(testPerson);

		Person testPersonUpdated = buildPerson();
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
		Person testPerson = populatePersonRepository(5);

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
}
