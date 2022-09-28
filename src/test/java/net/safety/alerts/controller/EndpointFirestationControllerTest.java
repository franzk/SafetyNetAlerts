package net.safety.alerts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.model.Firestation;
import net.safety.alerts.service.FirestationService;
import net.safety.alerts.utils.FirestationTestData;

public class EndpointFirestationControllerTest {

	@Mock
	private FirestationService firestationService;

	@InjectMocks
	private EndpointFirestationController controllerUnderTest;

	@BeforeEach
	public void reset() {
		MockitoAnnotations.openMocks(this);
	}

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
	public void getFirestationByAddressTest() {
		// Arrange
		List<Firestation> listFirestation = FirestationTestData.buildFirestationList();
		try {
			when(firestationService.getByAddress(any())).thenReturn(listFirestation);
		} catch (FirestationNotFoundException e) {
			fail("getFirestationByAddressTest (arrange) threw an exception");
		}

		// Act
		ResponseEntity<List<Firestation>> result = null;
		try {
			result = controllerUnderTest.getFirestation(Optional.of("address"), Optional.empty());
		} catch (FirestationNotFoundException e) {
			fail("getFirestationByAddressTest (act) threw an exception");
		}

		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(listFirestation);
	}

	@Test
	public void getFirestationByAStationNumberTest() {
		// Arrange
		List<Firestation> listFirestation = FirestationTestData.buildFirestationList();
		try {
			when(firestationService.getByStationNumber(any())).thenReturn(listFirestation);
		} catch (FirestationNotFoundException e) {
			fail("getFirestationByAStationNumberTest (arrange) threw an exception");
		}

		// Act
		ResponseEntity<List<Firestation>> result = null;
		try {
			result = controllerUnderTest.getFirestation(Optional.empty(), Optional.of(1));
		} catch (FirestationNotFoundException e) {
			fail("getFirestationByAStationNumberTest (act) threw an exception");
		}

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
	public void updateFirestationTest() {
		// Arrange
		Firestation testFirestation = FirestationTestData.buildFirestation();
		try {
			when(firestationService.update(any())).thenReturn(testFirestation);
		} catch (FirestationNotFoundException e) {
			fail("updateFirestationTest (arrange) threw an exception");
		}

		// Act
		ResponseEntity<Firestation> result = null;
		try {
			result = controllerUnderTest.updateFirestation(testFirestation);
		} catch (FirestationNotFoundException e) {
			fail("updateFirestationTest (act) threw an exception");
		}

		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(testFirestation);
	}

	@Test
	public void deleteFirestationByAddressTest() {
		// Arrange
		Firestation testFirestation = FirestationTestData.buildFirestation();

		// Act
		try {
			controllerUnderTest.deleteFirestations(Optional.of(testFirestation.getAddress()), Optional.empty());
		} catch (FirestationNotFoundException e1) {
			fail("deleteFirestationByAddressTest (act) threw an exception");
		}

		try {
			verify(firestationService).deleteByAddress(testFirestation.getAddress());
		} catch (FirestationNotFoundException e) {
			fail("deleteFirestationByAddressTest (assert) threw an exception");
		}
	}

	@Test
	public void deleteFirestationByStationNumberTest() {
		// Arrange
		Firestation testFirestation = FirestationTestData.buildFirestation();

		// Act
		try {
			controllerUnderTest.deleteFirestations(Optional.empty(), Optional.of(testFirestation.getStation()));
		} catch (FirestationNotFoundException e1) {
			fail("deleteFirestationByStationNumberTest (act) threw an exception");
		}

		try {
			verify(firestationService).deleteByStationNumber(testFirestation.getStation());
		} catch (FirestationNotFoundException e) {
			fail("deleteFirestationByStationNumberTest (assert) threw an exception");
		}
	}

	@Test
	public void deleteFirestationWithoutArgumentsTest() {
		assertThrows(IllegalArgumentException.class,
				() -> controllerUnderTest.deleteFirestations(Optional.empty(), Optional.empty()));
	}
}
