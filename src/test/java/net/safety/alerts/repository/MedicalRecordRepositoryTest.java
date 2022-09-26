package net.safety.alerts.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.safety.alerts.exceptions.MedicalRecordNotFoundException;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.model.Person;
import net.safety.alerts.utils.Utils;

public class MedicalRecordRepositoryTest {

	private static MedicalRecordRepository medicalRecordRepositoryUnderTest;

	private final String testFirstName = "Angus";
	private final String testLastName = "Young";
	private final String testBirthdate = "09/20/1979";
	private final String testMedication = "Doliprane";
	private final String testAllergie = "Pollen";

	@BeforeAll
	private static void setUp() {
		medicalRecordRepositoryUnderTest = new MedicalRecordRepository();
	}

	@BeforeEach
	private void reset() {
		medicalRecordRepositoryUnderTest.setListMedicalRecords(new ArrayList<>());
	}

	@Test
	public void setListMedicalRecordsgetAndMedicalRecordByNameTest() {
		// Arrange
		List<MedicalRecord> listMedicalRecords = buildListMedicalRecords();
		MedicalRecord testMedicalRecord = listMedicalRecords.get(5);

		// Act
		medicalRecordRepositoryUnderTest.setListMedicalRecords(listMedicalRecords);

		// Assert
		MedicalRecord result;
		try {
			result = medicalRecordRepositoryUnderTest.getMedicalRecordByName(testMedicalRecord.getFirstName(),
					testMedicalRecord.getLastName());
			assertThat(result).isEqualTo(testMedicalRecord);
		} catch (MedicalRecordNotFoundException e) {
			fail("setListMedicalRecordsgetAndMedicalRecordByNameTest threw an exception");
		}
	}

	@Test
	public void updateMedicalRecord() {
		// Arrange
		MedicalRecord medicalRecordToUpdate = new MedicalRecord();
		MedicalRecord medicalRecordUpdated = new MedicalRecord();
		medicalRecordToUpdate = buildMedicalRecord("");
		medicalRecordUpdated = buildMedicalRecord("");
		medicalRecordUpdated.setBirthdate(Utils.StringToDate("12/14/2021"));

		medicalRecordRepositoryUnderTest.addMedicalRecord(medicalRecordToUpdate);

		// Act
		try {
			medicalRecordRepositoryUnderTest.updateMedicalRecord(medicalRecordUpdated);
		} catch (MedicalRecordNotFoundException e) {
			fail("updateMedicalRecord (act) threw an exception !");
		}

		// Assert
		MedicalRecord result;
		try {
			result = medicalRecordRepositoryUnderTest.getMedicalRecordByName(medicalRecordToUpdate.getFirstName(),
					medicalRecordToUpdate.getLastName());
			assertThat(result.getBirthdate()).isEqualTo(medicalRecordUpdated.getBirthdate());
		} catch (MedicalRecordNotFoundException e) {
			fail("updateMedicalRecord (assert) threw an exception !");
		}

	}

	@Test
	public void deleteMedicalRecordTest() {
		List<MedicalRecord> listMedicalRecords = buildListMedicalRecords();

		medicalRecordRepositoryUnderTest.setListMedicalRecords(listMedicalRecords);
		MedicalRecord testMedicalRecord = listMedicalRecords.get(5);

		// Act
		try {
			medicalRecordRepositoryUnderTest.deleteMedicalRecord(testMedicalRecord);
		} catch (MedicalRecordNotFoundException e) {
			fail("deleteMedicalRecordTest threw an exception !");
		}

		// Assert
		assertThrows(MedicalRecordNotFoundException.class, () -> medicalRecordRepositoryUnderTest
				.getMedicalRecordByName(testMedicalRecord.getFirstName(), testMedicalRecord.getLastName()));
	}

	@Test
	public void deleteMedicalRecordByNameTest() {
		// Arrange
		List<MedicalRecord> listMedicalRecords = buildListMedicalRecords();

		medicalRecordRepositoryUnderTest.setListMedicalRecords(listMedicalRecords);
		MedicalRecord testMedicalRecord = listMedicalRecords.get(5);

		// Act
		try {
			medicalRecordRepositoryUnderTest.deleteMedicalRecordByName(testMedicalRecord.getFirstName(),
					testMedicalRecord.getLastName());
		} catch (MedicalRecordNotFoundException e) {
			fail("deleteMedicalRecordByNameTest threw an exception !");
		}

		// Assert
		assertThrows(MedicalRecordNotFoundException.class, () -> medicalRecordRepositoryUnderTest
				.getMedicalRecordByName(testMedicalRecord.getFirstName(), testMedicalRecord.getLastName()));
	}

	@Test
	public void getPersonAgeTest() {
		// Arrange
		MedicalRecord testMedicalRecord = buildMedicalRecord("");
		medicalRecordRepositoryUnderTest.addMedicalRecord(testMedicalRecord);

		Person testPerson = new Person();
		testPerson.setFirstName(testMedicalRecord.getFirstName());
		testPerson.setLastName(testMedicalRecord.getLastName());

		// Act
		int age = 0;
		try {
			age = medicalRecordRepositoryUnderTest.getPersonAge(testPerson);
		} catch (MedicalRecordNotFoundException e) {
			fail("getPersonAgeTest threw an exception !");
		}

		// Assert
		assertThat(age).isEqualTo(Utils.calculateAge(testMedicalRecord.getBirthdate()));
	}

	@Test
	public void updateMedicalRecordExceptionTest() {
		// Arrange
		MedicalRecord medicalRecordToUpdate = buildMedicalRecord("");
		MedicalRecord medicalRecordUpdated = buildMedicalRecord("");

		medicalRecordRepositoryUnderTest.addMedicalRecord(medicalRecordToUpdate);
		medicalRecordUpdated.setFirstName("wrong first name");
		final MedicalRecord medicalRecordUpdatedTest = medicalRecordUpdated;

		// Act + Assert
		assertThrows(MedicalRecordNotFoundException.class,
				() -> medicalRecordRepositoryUnderTest.updateMedicalRecord(medicalRecordUpdatedTest));
	}

	@Test
	public void deleteMedicalRecordExceptionTest() {
		// Arrange
		MedicalRecord medicalRecord = buildMedicalRecord("");
		MedicalRecord testMedicalRecord = medicalRecord;

		// Act + Assert
		assertThrows(MedicalRecordNotFoundException.class,
				() -> medicalRecordRepositoryUnderTest.deleteMedicalRecord(testMedicalRecord));

	}

	@Test
	public void deleteMedicalRecordByNameExceptionTest() {
		assertThrows(MedicalRecordNotFoundException.class,
				() -> medicalRecordRepositoryUnderTest.deleteMedicalRecordByName(testFirstName, testLastName));
	}

	@Test
	public void getPersonAgeExceptionTest() {
		Person person = new Person();
		person.setFirstName(testFirstName);
		person.setLastName(testLastName);
		assertThrows(MedicalRecordNotFoundException.class, () -> medicalRecordRepositoryUnderTest.getPersonAge(person));
	}

	// Utils
	private List<MedicalRecord> buildListMedicalRecords() {
		List<MedicalRecord> listMedicalRecords = new ArrayList<>();
		listMedicalRecords.add(buildMedicalRecord(""));
		for (int i = 0; i < 20; i++) {
			listMedicalRecords.add(buildMedicalRecord(Integer.toString(i)));
		}
		return listMedicalRecords;
	}

	private MedicalRecord buildMedicalRecord(String modifier) {
		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setFirstName(testFirstName + modifier);
		medicalRecord.setLastName(testLastName + modifier);
		medicalRecord.setBirthdate(Utils.StringToDate(testBirthdate));
		medicalRecord.setMedications(buildStringList(testMedication + modifier));
		medicalRecord.setAllergies(buildStringList(testAllergie + modifier));
		return medicalRecord;
	}

	private List<String> buildStringList(String baseString) {
		List<String> listString = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			listString.add(baseString + Integer.toString(i));
		}
		return listString;
	}
}
