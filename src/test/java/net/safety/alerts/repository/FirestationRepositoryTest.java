package net.safety.alerts.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.OutputCaptureExtension;

import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.model.Firestation;
import net.safety.alerts.repository.FirestationRepository;

@ExtendWith(OutputCaptureExtension.class)
public class FirestationRepositoryTest {

	private static FirestationRepository firestationRepositoryUnderTest;

	private final String testAddress = "test Address";
	private final Integer testNumber = 10;

	@BeforeAll
	public static void setUp() {
		firestationRepositoryUnderTest = new FirestationRepository();
	}

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
		Firestation firestation = buildFirestation(testAddress, testNumber);

		// Act
		firestationRepositoryUnderTest.addFirestation(firestation);

		// Assert
		try {
			assertThat(firestationRepositoryUnderTest.getFirestationAddress(testNumber)).isEqualTo(testAddress);
		} catch (FirestationNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("addFirestation Test failed : getFirestationAdress threw an exception !");
		}
	}

	@Test
	public void setListFirestationsAndGetFirestationNumberTest() {
		// Arrange
		List<Firestation> firestations = new ArrayList<>();
		firestations.add(buildFirestation(testAddress, testNumber));
		firestations.add(buildFirestation(testAddress + "1", testNumber + 1));
		firestations.add(buildFirestation(testAddress + "2", testNumber + 2));

		// Act
		firestationRepositoryUnderTest.setListFirestations(firestations);

		// Assert
		try {
			assertThat(firestationRepositoryUnderTest.getFirestationNumber(testAddress)).isEqualTo(testNumber);
		} catch (FirestationNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("setListFirestations Test failed : getFirestationNumber threw an exception !");
		}
	}

	@Test
	public void updateFirestationTest() {
		// Arrange
		Firestation firestation = buildFirestation(testAddress, testNumber);
		firestationRepositoryUnderTest.addFirestation(firestation);
		Firestation updatedFirestation = buildFirestation(testAddress, testNumber + 1);

		// Act
		try {
			firestationRepositoryUnderTest.updateFirestation(updatedFirestation);
		} catch (FirestationNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("updateFirestation Test failed : updateFirestation threw an exception !");
		}

		// Arrange
		try {
			assertThat(firestationRepositoryUnderTest.getFirestationNumber(testAddress)).isEqualTo(testNumber + 1);
		} catch (FirestationNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("updateFirestation Test failed : getFirestationNumber threw an exception !");
		}
	}

	@Test
	public void getFirestationAddressExceptionTest() {
		// Arrange
		Firestation firestation = buildFirestation(testAddress, testNumber);
		firestationRepositoryUnderTest.addFirestation(firestation);

		// Act + Assert
		assertThrows(FirestationNotFoundException.class,
				() -> firestationRepositoryUnderTest.getFirestationAddress(testNumber + 1));
	}

	@Test
	public void getFirestationNumberExceptionTest() {
		// Arrange
		Firestation firestation = buildFirestation(testAddress, testNumber);
		firestationRepositoryUnderTest.addFirestation(firestation);

		// Act + Assert
		assertThrows(FirestationNotFoundException.class,
				() -> firestationRepositoryUnderTest.getFirestationNumber(testAddress + "1"));
	}

	@Test
	public void updateFirestationExceptionTest() {
		// Arrange
		Firestation firestation = buildFirestation(testAddress, testNumber);
		firestationRepositoryUnderTest.addFirestation(firestation);		
		Firestation updatedFirestation = buildFirestation(testAddress + "1", testNumber + 1);

		// Act + Assert
		assertThrows(FirestationNotFoundException.class,
				() -> firestationRepositoryUnderTest.updateFirestation(updatedFirestation));

	}

}
