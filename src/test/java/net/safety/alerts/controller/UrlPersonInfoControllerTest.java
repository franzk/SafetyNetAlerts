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
import net.safety.alerts.dto.UrlPersonInfoDto;
import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.service.UrlService;

@ExtendWith(MockitoExtension.class)
public class UrlPersonInfoControllerTest {

	@Mock
	private UrlService urlService;

	@InjectMocks
	private UrlPersonInfoController controllerUnderTest;

	@Test
	public void urlPersonInfoTest() throws PersonNotFoundException {
		// Arrange
		UrlPersonInfoDto dto = new UrlPersonInfoDto();
		List<PersonDto> persons = new ArrayList<>();
		PersonDto person = new PersonDto();
		person.setFirstName("firstName");
		person.setLastName("LastName");
		persons.add(person);

		dto.setPersons(persons);

		when(urlService.urlPersonInfo(anyString(), anyString())).thenReturn(dto);

		// Act
		ResponseEntity<UrlPersonInfoDto> result = controllerUnderTest.personInfo(person.getFirstName(),
				person.getLastName());

		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(dto);
	}
}
