package net.safety.alerts.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.model.Firestation;
import net.safety.alerts.utils.TestConstants;

public class FirestationRepositoryTest {

	private static FirestationRepository firestationRepositoryUnderTest = new FirestationRepository();

	@BeforeEach
	public void reset() {
		firestationRepositoryUnderTest.setListFirestations(new ArrayList<Firestation>());
	}

	private Firestation buildFirestation(String address, Integer number) {
		Firestation firestation = new Firestation();
		firestation.setAddress(address);
		firestation.setStation(number);
		return firestation;
	}

	@Test
	public void addFirestationAndGetFirestationAddressTest() {
		// Arrange
		Firestation firestation = buildFirestation(TestConstants.address, TestConstants.stationNumber);

		// Act
		firestationRepositoryUnderTest.addFirestation(firestation);

		// Assert
		try {
			assertThat(firestationRepositoryUnderTest.getFirestationAddresses(TestConstants.stationNumber).get(0))
					.isEqualTo(TestConstants.address);
		} catch (FirestationNotFoundException e) {
			fail("addFirestation Test failed : getFirestationAdress threw an exception !");
		}
	}

	@Test
	public void setListFirestationsAndGetFirestationNumberTest() {
		// Arrange
		List<Firestation> firestations = new ArrayList<>();
		firestations.add(buildFirestation(TestConstants.address, TestConstants.stationNumber));
		firestations.add(buildFirestation(TestConstants.address + "1", TestConstants.stationNumber + 1));
		firestations.add(buildFirestation(TestConstants.address + "2", TestConstants.stationNumber + 2));

		// Act
		firestationRepositoryUnderTest.setListFirestations(firestations);

		// Assert
		try {
			assertThat(firestationRepositoryUnderTest.getFirestationNumber(TestConstants.address)).isEqualTo(TestConstants.stationNumber);
		} catch (FirestationNotFoundException e) {
			fail("setListFirestations Test failed : getFirestationNumber threw an exception !");
		}
	}

	@Test
	public void updateFirestationTest() {
		// Arrange
		Firestation firestation = buildFirestation(TestConstants.address, TestConstants.stationNumber);
		firestationRepositoryUnderTest.addFirestation(firestation);
		Firestation updatedFirestation = buildFirestation(TestConstants.address, TestConstants.stationNumber + 1);

		// Act
		try {
			firestationRepositoryUnderTest.updateFirestation(updatedFirestation);
		} catch (FirestationNotFoundException e) {
			fail("updateFirestation Test failed : updateFirestation threw an exception !");
		}

		// Arrange
		try {
			assertThat(firestationRepositoryUnderTest.getFirestationNumber(TestConstants.address)).isEqualTo(TestConstants.stationNumber + 1);
		} catch (FirestationNotFoundException e) {
			fail("updateFirestation Test failed : getFirestationNumber threw an exception !");
		}
	}

	@Test
	public void getFirestationAddressExceptionTest() {
		// Arrange
		Firestation firestation = buildFirestation(TestConstants.address, TestConstants.stationNumber );
		firestationRepositoryUnderTest.addFirestation(firestation);

		// Act + Assert
		assertThrows(FirestationNotFoundException.class,
				() -> firestationRepositoryUnderTest.getFirestationAddresses(TestConstants.stationNumber  + 1));
	}

	@Test
	public void getFirestationNumberExceptionTest() {
		// Arrange
		Firestation firestation = buildFirestation(TestConstants.address, TestConstants.stationNumber );
		firestationRepositoryUnderTest.addFirestation(firestation);

		// Act + Assert
		assertThrows(FirestationNotFoundException.class,
				() -> firestationRepositoryUnderTest.getFirestationNumber(TestConstants.address + "1"));
	}

	@Test
	public void updateFirestationExceptionTest() {
		// Arrange
		Firestation firestation = buildFirestation(TestConstants.address, TestConstants.stationNumber );
		firestationRepositoryUnderTest.addFirestation(firestation);
		Firestation updatedFirestation = buildFirestation(TestConstants.address + "1", TestConstants.stationNumber  + 1);

		// Act + Assert
		assertThrows(FirestationNotFoundException.class,
				() -> firestationRepositoryUnderTest.updateFirestation(updatedFirestation));

	}

	@Test
	public void deleteFirestationTest() {
		// Arrange
		Firestation firestation = buildFirestation(TestConstants.address, TestConstants.stationNumber );
		firestationRepositoryUnderTest.addFirestation(firestation);

		// Act
		try {
			firestationRepositoryUnderTest.deleteFirestation(firestation);
		} catch (FirestationNotFoundException e) {
			fail("deleteFirestationTest threw an exception");
		}

		// Assert
		assertThrows(FirestationNotFoundException.class,
				() -> firestationRepositoryUnderTest.getFirestationByAddress(TestConstants.address));

	}

	@Test
	public void deleteFirestationExceptionTest() {
		// Arrange
		Firestation firestation = buildFirestation(TestConstants.address, TestConstants.stationNumber );
		firestationRepositoryUnderTest.addFirestation(firestation);
		Firestation wrongFirestation = buildFirestation("wrong address", TestConstants.stationNumber );

		// Act + Assert
		assertThrows(FirestationNotFoundException.class,
				() -> firestationRepositoryUnderTest.deleteFirestation(wrongFirestation));

	}

	@Test
	public void deleteByAdressTest() {
		// Arrange
		Firestation firestation = buildFirestation(TestConstants.address, TestConstants.stationNumber );
		firestationRepositoryUnderTest.addFirestation(firestation);

		// Act
		try {
			firestationRepositoryUnderTest.deleteByAddress(TestConstants.address);
		} catch (FirestationNotFoundException e) {
			fail("deleteFirestationByAdressTest threw an exception");
		}

		// Assert
		assertThrows(FirestationNotFoundException.class,
				() -> firestationRepositoryUnderTest.getFirestationByAddress(TestConstants.address));

	}

	@Test
	public void deleteByStationNumberTest() {
		// Arrange
		Firestation firestation = buildFirestation(TestConstants.address, TestConstants.stationNumber );
		firestationRepositoryUnderTest.addFirestation(firestation);

		// Act
		try {
			firestationRepositoryUnderTest.deleteByStationNumber(TestConstants.stationNumber );
		} catch (FirestationNotFoundException e) {
			fail("deleteByStationNumberTest threw an exception");
		}

		// Assert
		assertThrows(FirestationNotFoundException.class,
				() -> firestationRepositoryUnderTest.getFirestationByAddress(TestConstants.address));

	}

}
