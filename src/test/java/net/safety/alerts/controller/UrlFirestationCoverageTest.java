package net.safety.alerts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
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
import net.safety.alerts.dto.UrlFirestationCoverageDto;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.service.UrlService;

@ExtendWith(MockitoExtension.class)
public class UrlFirestationCoverageTest {

	@Mock
	private UrlService urlService;

	@InjectMocks
	private UrlFirestationCoverageController controllerUnderTest;

	@Test
	public void firestationCoverageTest() throws FirestationNotFoundException {
		// Arrange
		UrlFirestationCoverageDto dto = new UrlFirestationCoverageDto();
		List<PersonDto> persons = new ArrayList<>();
		PersonDto person = new PersonDto();
		person.setFirstName("person");
		person.setLastName("one");
		persons.add(person);
		dto.setPersons(persons);
		dto.setAdultsCount(42);
		dto.setChildrenCount(24);

		when(urlService.urlFirestationCoverage(anyInt())).thenReturn(dto);

		// Act
		ResponseEntity<UrlFirestationCoverageDto> result = controllerUnderTest.firestationCoverage(3);

		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(dto);

	}
}
