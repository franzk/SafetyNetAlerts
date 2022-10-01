package net.safety.alerts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.model.Person;
import net.safety.alerts.repository.PersonRepository;
import net.safety.alerts.utils.PersonTestData;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

	@InjectMocks
	private PersonService personServiceUnderTest;

	@Mock
	private PersonRepository personRepository;

	@Test
	public void addTest() {
		// Arrange
		Person p = PersonTestData.buildPerson();

		when(personRepository.addPerson(any())).thenReturn(p);

		// Act
		Person result = personServiceUnderTest.add(p);

		// Assert
		assertThat(result).isEqualTo(p);
		verify(personRepository).addPerson(p);
	}

	@Test
	public void getByNameTest() throws PersonNotFoundException {
		// Arrange
		Person testPerson = PersonTestData.buildPerson();
		String testFirstName = testPerson.getFirstName();
		String testLastName = testPerson.getLastName();

		when(personRepository.getPersonByName(any(), any())).thenReturn(testPerson);

		// Act
		Person result = personServiceUnderTest.getPersonByName(testFirstName, testLastName);

		// Assert
		assertThat(result).isEqualTo(testPerson);
		verify(personRepository).getPersonByName(testFirstName, testLastName);

	}

	@Test
	public void getPersonsWithSameNameTest() throws PersonNotFoundException {
		// Arrange
		String testFirstName = "testFireName";
		String testLastName = "testLastName";

		List<Person> persons = PersonTestData.buildPersonList();

		when(personRepository.getPersonsByName(any(), any())).thenReturn(persons);

		// Act
		List<Person> result = personServiceUnderTest.getPersonsWithSameName(testFirstName, testLastName);

		// Assert
		assertThat(result).isEqualTo(persons);
		verify(personRepository).getPersonsByName(testFirstName, testLastName);
	}

	@Test
	public void updateTest() throws PersonNotFoundException {
		// Arrange
		Person testPersonUpdated = PersonTestData.buildPerson();
		testPersonUpdated.setCity("updated City");
		testPersonUpdated.setZip("updated Zip");

		when(personRepository.updatePerson(any())).thenReturn(testPersonUpdated);

		// Act
		Person result = personServiceUnderTest.update(testPersonUpdated);

		// Assert
		assertThat(result).isEqualTo(testPersonUpdated);
		verify(personRepository).updatePerson(testPersonUpdated);
	}

	@Test
	public void deleteTest() throws PersonNotFoundException {
		// Arrange
		String testFirstName = "testFireName";
		String testLastName = "testLastName";

		// Act
		personServiceUnderTest.delete(testFirstName, testLastName);

		// Assert
		verify(personRepository).deletePersonByName(testFirstName, testLastName);
	}
}
