package net.safety.alerts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.model.Person;
import net.safety.alerts.service.PersonService;
import net.safety.alerts.utils.PersonTestData;

public class EndpointPersonControllerTest {

	@Mock
	private PersonService personService;

	@InjectMocks
	private EndpointPersonController controllerUnderTest;

	@BeforeEach
	public void reset() {
		MockitoAnnotations.openMocks(this);
	}

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
	public void getPersonByNameTest() {
		// Arrange
		Person testPerson = PersonTestData.buildPerson();
		try {
			when(personService.getPersonByName(anyString(), anyString())).thenReturn(testPerson);
		} catch (PersonNotFoundException e) {
			fail("getPersonByNameTest (arrange) threw an exception");
		}

		// Act
		ResponseEntity<Person> result = null;
		try {
			result = controllerUnderTest.getPersonByName(testPerson.getFirstName(), testPerson.getLastName());
		} catch (PersonNotFoundException e) {
			fail("getPersonByNameTest (act) threw an exception");
		}

		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(testPerson);
	}

	@Test
	public void updatePersonTest() {
		// Arrange
		Person testPerson = PersonTestData.buildPerson();
		try {
			when(personService.update(any())).thenReturn(testPerson);
		} catch (PersonNotFoundException e1) {
			fail("updatePersonTest (arrange) threw an exception");
		}
		
		// Act
		ResponseEntity<Person> result = null;
		try {
			result = controllerUnderTest.updatePerson(testPerson);
		} catch (PersonNotFoundException e) {
			fail("updatePersonTest (act) threw an exception");
		}
		
		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(testPerson);				
	}

	@Test
	public void deletePersonTest() {
		// Arrange
		Person testPerson = PersonTestData.buildPerson();

		// Act
		try {
			controllerUnderTest.deletePerson(testPerson.getFirstName(), testPerson.getLastName());
		} catch (PersonNotFoundException e) {
			fail("deletePersonTest (act) threw an exception");
		}
		
		// Assert
		try {
			verify(personService).delete(testPerson.getFirstName(), testPerson.getLastName());
		} catch (PersonNotFoundException e) {
			fail("deletePersonTest (assert) threw an exception");
		}
		
	}
	
}
