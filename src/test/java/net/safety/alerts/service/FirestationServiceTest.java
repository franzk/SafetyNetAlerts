package net.safety.alerts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.model.Firestation;
import net.safety.alerts.repository.FirestationRepository;
import net.safety.alerts.utils.TestConstants;

public class FirestationServiceTest {

	private FirestationService firestationServiceUnderTest = new FirestationService();

	private FirestationRepository firestationRepository;

	@BeforeEach
	private void reset() {
		firestationRepository = new FirestationRepository();
		ReflectionTestUtils.setField(firestationServiceUnderTest, "firestationRepository", firestationRepository);
	}

	private Firestation buildFirestation(String addressModifier, Integer stationNumberModifier) {
		Firestation f = new Firestation();
		f.setAddress(TestConstants.address + addressModifier);
		f.setStation(TestConstants.stationNumber + stationNumberModifier);
		return f;
	}

	private Firestation buildFirestation() {
		return buildFirestation("", 0);
	}

	private Firestation populatefirestationRepository(Integer testFirestationIndex) {
		Firestation testFirestation = new Firestation();
		for (int i = 0; i < 24; i++) {
			Firestation f = buildFirestation(Integer.toString(i), i);
			firestationServiceUnderTest.add(f);
			if (i == testFirestationIndex) {
				testFirestation = f;
			}
		}
		return testFirestation;
	}

	@Test
	public void addAndGetByAddressTest() {
		// Arrange
		Firestation testFirestation = buildFirestation();

		// Act
		firestationServiceUnderTest.add(testFirestation);
		List<Firestation> result = new ArrayList<>();
		try {
			result = firestationServiceUnderTest.getByAddress(testFirestation.getAddress());
		} catch (FirestationNotFoundException e) {
			fail("addAndGetByAddressTest threw an exception");
		}

		// Assert
		assertThat(result.get(0)).isEqualTo(testFirestation);
	}

	@Test
	public void getByAddressTest() {
		// Arrange
		Firestation testFirestation = populatefirestationRepository(5);

		// Act
		List<Firestation> result = new ArrayList<>();
		try {
			result = firestationServiceUnderTest.getByAddress(testFirestation.getAddress());
		} catch (FirestationNotFoundException e) {
			fail("getByAddressTest threw an exception");
		}

		// Assert
		assertThat(result.get(0)).isEqualTo(testFirestation);
	}

	@Test
	public void getByStationNumberTest() {
		// Arrange
		Firestation testFirestation = populatefirestationRepository(5);

		// Act
		List<Firestation> result = new ArrayList<>();
		try {
			result = firestationServiceUnderTest.getByStationNumber(testFirestation.getStation());
		} catch (FirestationNotFoundException e) {
			fail("getByAddressTest threw an exception");
		}

		// Assert
		assertThat(result.get(0)).isEqualTo(testFirestation);
	}

	@Test
	public void updateTest() {
		// Arrange
		Firestation testFirestation = populatefirestationRepository(5);
		Integer oldStationNumber = testFirestation.getStation();
		testFirestation.setStation(666);
		
		// Act
		try {
			firestationServiceUnderTest.update(testFirestation);
		} catch (FirestationNotFoundException e) {
			fail("updateTest (act) threw an exception");
		}
		
		// Assert
		Firestation result = new Firestation();
		try {
			result = firestationServiceUnderTest.getByAddress(testFirestation.getAddress()).get(0);
		} catch (FirestationNotFoundException e) {
			fail("updateTest (assert) threw an exception");
		}
		assertThat(result.getStation()).isNotEqualTo(oldStationNumber);
	}
	
	@Test
	public void deleteTest() {
		// Arrange
		Firestation testFirestation = populatefirestationRepository(5);
		
		// Act
		try {
			firestationServiceUnderTest.delete(testFirestation);
		} catch (FirestationNotFoundException e) {
			fail("deleteTest threw an exception");
		}
		
		assertThrows(FirestationNotFoundException.class, () -> firestationServiceUnderTest.getByAddress(testFirestation.getAddress()));

	}

	@Test
	public void deleteByAdddessTest() {
		// Arrange
		Firestation testFirestation = populatefirestationRepository(5);
		
		// Act
		try {
			firestationServiceUnderTest.deleteByAddress(testFirestation.getAddress());
		} catch (FirestationNotFoundException e) {
			fail("deleteByAdddessTest threw an exception");
		}
		
		assertThrows(FirestationNotFoundException.class, () -> firestationServiceUnderTest.getByAddress(testFirestation.getAddress()));

	}
	
	@Test
	public void deleteByStationNumberTest() {
		// Arrange
		Firestation testFirestation = populatefirestationRepository(5);
		
		// Act
		try {
			firestationServiceUnderTest.deleteByStationNumber(testFirestation.getStation());
		} catch (FirestationNotFoundException e) {
			fail("deleteByAdddessTest threw an exception");
		}
		
		// Assert
		assertThrows(FirestationNotFoundException.class, () -> firestationServiceUnderTest.getByAddress(testFirestation.getAddress()));

	}
	
}
