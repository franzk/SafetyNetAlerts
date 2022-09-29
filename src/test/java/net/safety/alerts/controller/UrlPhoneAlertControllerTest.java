package net.safety.alerts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.safety.alerts.dto.UrlPhoneAlertDto;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.service.UrlService;

public class UrlPhoneAlertControllerTest {

	@Mock
	private UrlService urlService;

	@InjectMocks
	private UrlPhoneAlertController controllerUnderTest;

	@BeforeEach
	public void reset() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void phoneAlertTest() {
		// Arrange
		UrlPhoneAlertDto dto = new UrlPhoneAlertDto();
		Set<String> phoneNumbers = new HashSet<>();
		phoneNumbers.add("phone number 1");
		phoneNumbers.add("phone number 2");

		dto.setPhoneNumbers(phoneNumbers);

		try {
			when(urlService.urlPhoneAlert(anyInt())).thenReturn(dto);
		} catch (FirestationNotFoundException e) {
			fail("phoneAlertTest (arrange)  threw an exception");
		}

		// Act
		ResponseEntity<UrlPhoneAlertDto> result = null;

		try {
			result = controllerUnderTest.phoneAlert(42);
		} catch (FirestationNotFoundException e) {
			fail("phoneAlertTest (arrange)  threw an exception");
		}

		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(dto);
	}

}
