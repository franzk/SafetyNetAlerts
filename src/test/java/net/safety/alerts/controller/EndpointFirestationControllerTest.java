package net.safety.alerts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.model.Firestation;
import net.safety.alerts.service.FirestationService;
import net.safety.alerts.utils.FirestationTestData;

@ExtendWith(MockitoExtension.class)
public class EndpointFirestationControllerTest {

	@Mock
	private FirestationService firestationService;

	@InjectMocks
	private EndpointFirestationController controllerUnderTest;

	@Test
	public void addFirestationTest() {
		// Arrange
		Firestation testFirestation = FirestationTestData.buildFirestation();
		when(firestationService.add(any())).thenReturn(testFirestation);

		// Act
		ResponseEntity<Firestation> result = controllerUnderTest.addFirestation(testFirestation);

		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(testFirestation);
	}

	@Test
	public void getFirestationByAddressTest() throws FirestationNotFoundException {
		// Arrange
		List<Firestation> listFirestation = FirestationTestData.buildFirestationList();

		when(firestationService.getByAddress(any())).thenReturn(listFirestation);

		// Act
		ResponseEntity<List<Firestation>> result = controllerUnderTest.getFirestation(Optional.of("address"),
				Optional.empty());

		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(listFirestation);
	}

	@Test
	public void getFirestationByAStationNumberTest() throws FirestationNotFoundException {
		// Arrange
		List<Firestation> listFirestation = FirestationTestData.buildFirestationList();

		when(firestationService.getByStationNumber(any())).thenReturn(listFirestation);

		// Act
		ResponseEntity<List<Firestation>> result = controllerUnderTest.getFirestation(Optional.empty(), Optional.of(1));

		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(listFirestation);
	}

	@Test
	public void getFirestationWithNoArguments() {
		assertThrows(IllegalArgumentException.class,
				() -> controllerUnderTest.getFirestation(Optional.empty(), Optional.empty()));
	}

	@Test
	public void updateFirestationTest() throws FirestationNotFoundException {
		// Arrange
		Firestation testFirestation = FirestationTestData.buildFirestation();

		when(firestationService.update(any())).thenReturn(testFirestation);

		// Act
		ResponseEntity<Firestation> result = controllerUnderTest.updateFirestation(testFirestation);

		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(testFirestation);
	}

	@Test
	public void deleteFirestationByAddressTest() throws FirestationNotFoundException {
		// Arrange
		Firestation testFirestation = FirestationTestData.buildFirestation();

		// Act

		controllerUnderTest.deleteFirestations(Optional.of(testFirestation.getAddress()), Optional.empty());

		// Assert
		verify(firestationService).deleteByAddress(testFirestation.getAddress());

	}

	@Test
	public void deleteFirestationByStationNumberTest() throws FirestationNotFoundException {
		// Arrange
		Firestation testFirestation = FirestationTestData.buildFirestation();

		// Act
		controllerUnderTest.deleteFirestations(Optional.empty(), Optional.of(testFirestation.getStation()));

		// Assert
		verify(firestationService).deleteByStationNumber(testFirestation.getStation());

	}

	@Test
	public void deleteFirestationWithoutArgumentsTest() {
		assertThrows(IllegalArgumentException.class,
				() -> controllerUnderTest.deleteFirestations(Optional.empty(), Optional.empty()));
	}
}
