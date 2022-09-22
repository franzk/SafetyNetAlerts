package net.safety.alerts.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
		List<MedicalRecord> listMedicalRecords = new ArrayList<>();
		try {
			listMedicalRecords = buildListMedicalRecords();
		} catch (ParseException e) {
			fail("setListMedicalRecordsTest threw an exception !");
		}
		MedicalRecord testMedicalRecord = listMedicalRecords.get(5);

		// Act
		medicalRecordRepositoryUnderTest.setListMedicalRecords(listMedicalRecords);

		// Assert
		Optional<MedicalRecord> result = medicalRecordRepositoryUnderTest
				.getMedicalRecordByName(testMedicalRecord.getFirstName(), testMedicalRecord.getLastName());
		assertThat(result.get()).isEqualTo(testMedicalRecord);
	}

	@Test
	public void updateMedicalRecord() {
		// Arrange
		MedicalRecord medicalRecordToUpdate = new MedicalRecord();
		MedicalRecord medicalRecordUpdated = new MedicalRecord();
		try {
			medicalRecordToUpdate = buildMedicalRecord("");
			medicalRecordUpdated = buildMedicalRecord("");
			medicalRecordUpdated.setBirthdate(Utils.StringToDate("12/14/2021"));
		} catch (ParseException e) {
			fail("setListMedicalRecordsTest threw an exception !");
		}

		medicalRecordRepositoryUnderTest.addMedicalRecord(medicalRecordToUpdate);

		// Act
		try {
			medicalRecordRepositoryUnderTest.updateMedicalRecord(medicalRecordUpdated);
		} catch (MedicalRecordNotFoundException e) {
			fail("setListMedicalRecordsTest threw an exception !");
		}

		// Assert
		Optional<MedicalRecord> result = medicalRecordRepositoryUnderTest
				.getMedicalRecordByName(medicalRecordToUpdate.getFirstName(), medicalRecordToUpdate.getLastName());

		assertThat(result.get().getBirthdate()).isEqualTo(medicalRecordUpdated.getBirthdate());

	}

	@Test
	public void deleteMedicalRecordTest() {
		// Arrange
		List<MedicalRecord> listMedicalRecords = new ArrayList<>();
		try {
			listMedicalRecords = buildListMedicalRecords();
		} catch (ParseException e) {
			fail("deleteMedicalRecordTest threw an exception !");
		}

		medicalRecordRepositoryUnderTest.setListMedicalRecords(listMedicalRecords);
		MedicalRecord testMedicalRecord = listMedicalRecords.get(5);

		// Act
		try {
			medicalRecordRepositoryUnderTest.deleteMedicalRecord(testMedicalRecord);
		} catch (MedicalRecordNotFoundException e) {
			fail("deleteMedicalRecordTest threw an exception !");
		}

		// Assert
		Optional<MedicalRecord> result = medicalRecordRepositoryUnderTest
				.getMedicalRecordByName(testMedicalRecord.getFirstName(), testMedicalRecord.getLastName());
		assertThat(result.isEmpty());
	}

	@Test
	public void deleteMedicalRecordByNameTest() {
		// Arrange
		List<MedicalRecord> listMedicalRecords = new ArrayList<>();
		try {
			listMedicalRecords = buildListMedicalRecords();
		} catch (ParseException e) {
			fail("deleteMedicalRecordByNameTest threw an exception !");
		}

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
		Optional<MedicalRecord> result = medicalRecordRepositoryUnderTest
				.getMedicalRecordByName(testMedicalRecord.getFirstName(), testMedicalRecord.getLastName());
		assertThat(result.isEmpty());
	}

	@Test
	public void getPersonAgeTest() {
		// Arrange
		MedicalRecord testMedicalRecord = new MedicalRecord();
		try {
			testMedicalRecord = buildMedicalRecord("");
			medicalRecordRepositoryUnderTest.addMedicalRecord(testMedicalRecord);
		} catch (ParseException e) {
			fail("getPersonAgeTest threw an exception !");
		}

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
		MedicalRecord medicalRecordToUpdate = new MedicalRecord();
		MedicalRecord medicalRecordUpdated = new MedicalRecord();
		try {
			medicalRecordToUpdate = buildMedicalRecord("");
			medicalRecordUpdated = buildMedicalRecord("");
		} catch (ParseException e) {
			fail("setListMedicalRecordsTest threw an exception !");
		}

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
		MedicalRecord medicalRecord = new MedicalRecord();
		try {
			medicalRecord = buildMedicalRecord("");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final MedicalRecord testMedicalRecord = medicalRecord;
		
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
	private List<MedicalRecord> buildListMedicalRecords() throws ParseException {
		List<MedicalRecord> listMedicalRecords = new ArrayList<>();
		listMedicalRecords.add(buildMedicalRecord(""));
		for (int i = 0; i < 20; i++) {
			listMedicalRecords.add(buildMedicalRecord(Integer.toString(i)));
		}
		return listMedicalRecords;
	}

	private MedicalRecord buildMedicalRecord(String modifier) throws ParseException {
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
