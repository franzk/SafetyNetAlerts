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

import net.safety.alerts.dto.UrlCommunityEmailDto;
import net.safety.alerts.exceptions.CityNotFoundException;
import net.safety.alerts.service.UrlService;

public class UrlCommunityEmailControllerTest {

	@Mock
	private UrlService urlService;

	@InjectMocks
	private UrlCommunityEmailController controllerUnderTest;

	@BeforeEach
	public void reset() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void communityEmailTest() {
		// Arrange
		UrlCommunityEmailDto dto = new UrlCommunityEmailDto();
		List<String> emails = new ArrayList<>();
		emails.add("email 1");
		emails.add("email 2");
		dto.setEmails(emails);

		try {
			when(urlService.urlCommunityEmail(anyString())).thenReturn(dto);
		} catch (CityNotFoundException e) {
			fail("communityEmailTest (arrange) threw an exception");
		}

		// Act
		ResponseEntity<UrlCommunityEmailDto> result = null;
		try {
			result = controllerUnderTest.communityEmail("city");
		} catch (CityNotFoundException e) {
			fail("communityEmailTest (act) threw an exception");
		}

		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(dto);

	}

}
