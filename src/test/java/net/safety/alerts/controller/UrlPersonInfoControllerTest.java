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
import net.safety.alerts.dto.UrlPersonInfoDto;
import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.service.UrlService;

public class UrlPersonInfoControllerTest {

	@Mock
	private UrlService urlService;

	@InjectMocks
	private UrlPersonInfoController controllerUnderTest;

	@BeforeEach
	public void reset() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void urlPersonInfoTest() {
		// Arrange
		UrlPersonInfoDto dto = new UrlPersonInfoDto();
		List<PersonDto> persons = new ArrayList<>();
		PersonDto person = new PersonDto();
		person.setFirstName("firstName");
		person.setLastName("LastName");
		persons.add(person);

		dto.setPersons(persons);

		try {
			when(urlService.urlPersonInfo(anyString(), anyString())).thenReturn(dto);
		} catch (PersonNotFoundException e) {
			fail("urlPersonInfoTest (arrange) threw an exception");
		}

		// Act
		ResponseEntity<UrlPersonInfoDto> result = null;
		try {
			result = controllerUnderTest.personInfo(person.getFirstName(), person.getLastName());
		} catch (PersonNotFoundException e) {
			fail("urlPersonInfoTest (act) threw an exception");
		}

		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(dto);
	}
}
