package net.safety.alerts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
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
import net.safety.alerts.dto.UrlFloodStationsAddressDto;
import net.safety.alerts.dto.UrlFloodStationsDto;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.service.UrlService;

public class UrlFloodStationsControllerTest {
	@Mock
	private UrlService urlService;

	@InjectMocks
	private UrlFloodStationsController controllerUnderTest;

	@BeforeEach
	public void reset() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void floodStationsTest() {
		// Arrange
		UrlFloodStationsDto dto = new UrlFloodStationsDto();
		UrlFloodStationsAddressDto address1 = new UrlFloodStationsAddressDto();
		address1.setAddress("address");

		List<PersonDto> inhabitants = new ArrayList<>();
		PersonDto inhabitant = new PersonDto();
		inhabitant.setFirstName("inhab");
		inhabitant.setLastName("itant");
		inhabitants.add(inhabitant);

		address1.setInhabitants(inhabitants);

		List<Integer> stations = List.of(1, 2);

		try {
			when(urlService.urlFloodStations(stations)).thenReturn(dto);
		} catch (FirestationNotFoundException e) {
			fail("floodStationsTest (arrange) threw an exception");
		}

		// Act
		ResponseEntity<UrlFloodStationsDto> result = null;
		try {
			result = controllerUnderTest.floodStations(stations);
		} catch (FirestationNotFoundException e) {
			fail("floodStationsTest (arrange) threw an exception");
		}

		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(dto);

	}

}
