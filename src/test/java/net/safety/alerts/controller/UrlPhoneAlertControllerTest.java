package net.safety.alerts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.safety.alerts.dto.UrlPhoneAlertDto;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.service.UrlService;

@ExtendWith(MockitoExtension.class)
public class UrlPhoneAlertControllerTest {

	@Mock
	private UrlService urlService;

	@InjectMocks
	private UrlPhoneAlertController controllerUnderTest;

	@Test
	public void phoneAlertTest() throws FirestationNotFoundException {
		// Arrange
		UrlPhoneAlertDto dto = new UrlPhoneAlertDto();
		Set<String> phoneNumbers = new HashSet<>();
		phoneNumbers.add("phone number 1");
		phoneNumbers.add("phone number 2");

		dto.setPhoneNumbers(phoneNumbers);

		when(urlService.urlPhoneAlert(anyInt())).thenReturn(dto);

		// Act
		ResponseEntity<UrlPhoneAlertDto> result = controllerUnderTest.phoneAlert(42);

		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(dto);
	}

}
