package net.safety.alerts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.model.Firestation;
import net.safety.alerts.repository.FirestationRepository;
import net.safety.alerts.utils.FirestationTestData;

@ExtendWith(MockitoExtension.class)
public class FirestationServiceTest {

	@InjectMocks
	private FirestationService firestationServiceUnderTest; 

	@Mock
	private FirestationRepository firestationRepository;

	@Test
	public void addTest() {
		// Arrange
		Firestation f = FirestationTestData.buildFirestation();
		when(firestationRepository.addFirestation(any())).thenReturn(f);

		// Act
		Firestation result = firestationServiceUnderTest.add(f);

		// Assert
		assertThat(result).isEqualTo(f);
		verify(firestationRepository).addFirestation(f);
	}

	@Test
	public void getByAddressTest() throws FirestationNotFoundException {
		// Arrange
		List<Firestation> firestations = FirestationTestData.buildFirestationList();

		when(firestationRepository.getFirestationsByAddress(any())).thenReturn(firestations);

		// Act
		List<Firestation> result = firestationServiceUnderTest.getByAddress("");

		// Assert
		assertThat(result).isEqualTo(firestations);
	}

	@Test
	public void getByStationNumberTest() throws FirestationNotFoundException {
		// Arrange
		List<Firestation> firestations = FirestationTestData.buildFirestationList();

		when(firestationRepository.getFirestationsByStationNumber(any())).thenReturn(firestations);

		// Act
		List<Firestation> result = firestationServiceUnderTest.getByStationNumber(42);

		// Assert
		assertThat(result).isEqualTo(firestations);
	}

	@Test
	public void updateTest() throws FirestationNotFoundException {
		// Arrange
		Firestation testFirestation = FirestationTestData.buildFirestation();
		Firestation testFirestationUpdated = FirestationTestData.buildFirestation();
		testFirestationUpdated.setAddress("address updated");
		
		when(firestationRepository.updateFirestation(any())).thenReturn(testFirestationUpdated);

		// Act
		Firestation result = firestationServiceUnderTest.update(testFirestation);

		// Assert
		assertThat(result).isEqualTo(testFirestationUpdated);
	}

	@Test
	public void deleteTest() throws FirestationNotFoundException {
		// Arrange
		Firestation testFirestation = FirestationTestData.buildFirestation();

		// Act
		firestationServiceUnderTest.delete(testFirestation);

		// Assert
		verify(firestationRepository).deleteFirestation(testFirestation);

	}

	@Test
	public void deleteByAdddessTest() throws FirestationNotFoundException {
		// Arrange
		Firestation testFirestation = FirestationTestData.buildFirestation();

		// Act
		firestationServiceUnderTest.deleteByAddress(testFirestation.getAddress());

		// Assert
		verify(firestationRepository).deleteByAddress(testFirestation.getAddress());

	}

	@Test
	public void deleteByStationNumberTest() throws FirestationNotFoundException {
		// Arrange
		Firestation testFirestation = FirestationTestData.buildFirestation();

		// Act
		firestationServiceUnderTest.deleteByStationNumber(testFirestation.getStation());

		// Assert
		verify(firestationRepository).deleteByStationNumber(testFirestation.getStation());
	}
}
