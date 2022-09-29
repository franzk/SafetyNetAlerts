package net.safety.alerts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.safety.alerts.dto.PersonDto;
import net.safety.alerts.dto.UrlFireDto;
import net.safety.alerts.exceptions.AddressNotFoundException;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.service.UrlService;

public class UrlFireControllerTest {

	@Mock
	private UrlService urlService;

	@InjectMocks
	private UrlFireController controllerUnderTest;

	@BeforeEach
	public void reset() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void fireTest() {
		// Arrange
		UrlFireDto dto = new UrlFireDto();
		List<PersonDto> persons = new ArrayList<>();
		PersonDto person = new PersonDto();
		person.setFirstName("person");
		person.setLastName("one");
		persons.add(person);
		dto.setPersons(persons);
		dto.setFirestationNumber(42);
		
		try {
			when(urlService.urlFire(anyString())).thenReturn(dto);
		} catch (FirestationNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AddressNotFoundException e) {
			fail("fireTest (arrange) threw an exception");
		}
		
		// Act
		ResponseEntity<UrlFireDto> result = null;
		try {
			result = controllerUnderTest.fire("address");
		} catch (Exception e) {
			fail("fireTest (act) threw an exception");
		}
		
		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(dto);
	}
}
