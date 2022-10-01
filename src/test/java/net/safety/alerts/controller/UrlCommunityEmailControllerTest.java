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

import net.safety.alerts.dto.UrlCommunityEmailDto;
import net.safety.alerts.exceptions.CityNotFoundException;
import net.safety.alerts.service.UrlService;

@ExtendWith(MockitoExtension.class)
public class UrlCommunityEmailControllerTest {

	@Mock
	private UrlService urlService;

	@InjectMocks
	private UrlCommunityEmailController controllerUnderTest;

	@Test
	public void communityEmailTest() throws CityNotFoundException {
		// Arrange
		UrlCommunityEmailDto dto = new UrlCommunityEmailDto();
		List<String> emails = new ArrayList<>();
		emails.add("email 1");
		emails.add("email 2");
		dto.setEmails(emails);

		when(urlService.urlCommunityEmail(anyString())).thenReturn(dto);

		// Act
		ResponseEntity<UrlCommunityEmailDto> result = controllerUnderTest.communityEmail("city");

		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(dto);

	}

}
