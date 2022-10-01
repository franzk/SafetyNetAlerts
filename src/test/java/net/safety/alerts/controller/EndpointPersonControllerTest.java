package net.safety.alerts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.model.Person;
import net.safety.alerts.service.PersonService;
import net.safety.alerts.utils.PersonTestData;

@ExtendWith(MockitoExtension.class)
public class EndpointPersonControllerTest {

	@Mock
	private PersonService personService;

	@InjectMocks
	private EndpointPersonController controllerUnderTest;

	@Test
	public void addPersonTest() {
		// Arrange
		Person testPerson = PersonTestData.buildPerson();
		when(personService.add(any())).thenReturn(testPerson);

		// Act
		ResponseEntity<Person> result = controllerUnderTest.addPerson(testPerson);

		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(testPerson);
	}

	@Test
	public void getPersonByNameTest() throws PersonNotFoundException {
		// Arrange
		Person testPerson = PersonTestData.buildPerson();

		when(personService.getPersonByName(anyString(), anyString())).thenReturn(testPerson);

		// Act
		ResponseEntity<Person> result = controllerUnderTest.getPersonByName(testPerson.getFirstName(),
				testPerson.getLastName());

		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(testPerson);
	}

	@Test
	public void updatePersonTest() throws PersonNotFoundException {
		// Arrange
		Person testPerson = PersonTestData.buildPerson();
		when(personService.update(any())).thenReturn(testPerson);

		// Act
		ResponseEntity<Person> result = controllerUnderTest.updatePerson(testPerson);

		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(testPerson);
	}

	@Test
	public void deletePersonTest() throws PersonNotFoundException {
		// Arrange
		Person testPerson = PersonTestData.buildPerson();

		// Act
		controllerUnderTest.deletePerson(testPerson.getFirstName(), testPerson.getLastName());

		// Assert
		verify(personService).delete(testPerson.getFirstName(), testPerson.getLastName());

	}

}
