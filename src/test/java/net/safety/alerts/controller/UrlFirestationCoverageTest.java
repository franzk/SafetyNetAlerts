package net.safety.alerts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyInt;
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
import net.safety.alerts.dto.UrlFirestationCoverageDto;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.service.UrlService;

public class UrlFirestationCoverageTest {

	
	@Mock
	private UrlService urlService;

	@InjectMocks
	private UrlFirestationCoverageController controllerUnderTest;

	@BeforeEach
	public void reset() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void firestationCoverageTest() {
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
		
		try {
			when(urlService.urlFirestationCoverage(anyInt())).thenReturn(dto);
		} catch (FirestationNotFoundException e1) {
			fail("firestationCoverageTest (arrange) threw an exception");
		}
		
		// Act
		ResponseEntity<UrlFirestationCoverageDto> result = null;
		try {
			result = controllerUnderTest.firestationCoverage(3);
		} catch (FirestationNotFoundException e) {
			fail("firestationCoverageTest (act) threw an exception");
		}
		
		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(dto);
		
	}
}
