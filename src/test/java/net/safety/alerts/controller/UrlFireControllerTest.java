package net.safety.alerts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.safety.alerts.dto.PersonDto;
import net.safety.alerts.dto.UrlFireDto;
import net.safety.alerts.exceptions.AddressNotFoundException;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.service.UrlService;

@ExtendWith(MockitoExtension.class)
public class UrlFireControllerTest {

	@Mock
	private UrlService urlService;

	@InjectMocks
	private UrlFireController controllerUnderTest;

	@Test
	public void fireTest() throws FirestationNotFoundException, AddressNotFoundException {
		// Arrange
		UrlFireDto dto = new UrlFireDto();
		List<PersonDto> persons = new ArrayList<>();
		PersonDto person = new PersonDto();
		person.setFirstName("person");
		person.setLastName("one");
		persons.add(person);
		dto.setPersons(persons);
		dto.setFirestationNumber(42);

		when(urlService.urlFire(anyString())).thenReturn(dto);

		// Act
		ResponseEntity<UrlFireDto> result = controllerUnderTest.fire("address");

		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(dto);
	}
}
