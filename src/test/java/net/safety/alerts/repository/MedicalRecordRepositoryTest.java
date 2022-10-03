package net.safety.alerts.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import net.safety.alerts.exceptions.MedicalRecordNotFoundException;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.model.Person;
import net.safety.alerts.utils.TestConstants;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordRepositoryTest {

	@InjectMocks
	private MedicalRecordRepository medicalRecordRepositoryUnderTest;

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
		medicalRecordUpdated.setBirthdate(TestConstants.birthdate);

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
	public void getMedicalRecordTest() {
		// Arrange
		MedicalRecord testMedicalRecord = buildMedicalRecord("");
		medicalRecordRepositoryUnderTest.addMedicalRecord(testMedicalRecord);

		Person person = new Person();
		person.setFirstName(testMedicalRecord.getFirstName());
		person.setLastName(testMedicalRecord.getLastName());

		// Act
		try {
			MedicalRecord result = medicalRecordRepositoryUnderTest.getMedicalRecord(person);
			assertThat(result.equals(testMedicalRecord));
		} catch (MedicalRecordNotFoundException e) {
			fail("getMedicalRecordTest threw an exception");
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
	public void isAdultTest() {
		// Arrange
		MedicalRecord testMedicalRecord = buildMedicalRecord("");
		testMedicalRecord.setBirthdate(LocalDate.now().minusYears(40));
		medicalRecordRepositoryUnderTest.addMedicalRecord(testMedicalRecord);

		Person testPerson = new Person();
		testPerson.setFirstName(testMedicalRecord.getFirstName());
		testPerson.setLastName(testMedicalRecord.getLastName());

		// Act
		boolean isAdult = medicalRecordRepositoryUnderTest.isAdult(testPerson);

		// Assert
		assertThat(isAdult).isTrue();
	}

	@Test
	public void isAdultFailTest() {
		// Arrange
		MedicalRecord testMedicalRecord = buildMedicalRecord("");
		testMedicalRecord.setBirthdate(LocalDate.now().minusYears(1));
		medicalRecordRepositoryUnderTest.addMedicalRecord(testMedicalRecord);

		Person testPerson = new Person();
		testPerson.setFirstName(testMedicalRecord.getFirstName());
		testPerson.setLastName(testMedicalRecord.getLastName());

		// Act
		boolean isAdult = medicalRecordRepositoryUnderTest.isAdult(testPerson);

		// Assert
		assertThat(isAdult).isFalse();
	}

	@Test
	public void isAdultExceptionTest() {
		Person testPerson = new Person();
		assertThat(medicalRecordRepositoryUnderTest.isAdult(testPerson)).isFalse();
	}

	@Test
	public void isChildTest() {
		// Arrange
		MedicalRecord testMedicalRecord = buildMedicalRecord("");
		testMedicalRecord.setBirthdate(LocalDate.now().minusYears(1));
		medicalRecordRepositoryUnderTest.addMedicalRecord(testMedicalRecord);

		Person testPerson = new Person();
		testPerson.setFirstName(testMedicalRecord.getFirstName());
		testPerson.setLastName(testMedicalRecord.getLastName());

		// Act
		boolean isChild = medicalRecordRepositoryUnderTest.isChild(testPerson);

		// Act + Assert
		assertThat(isChild).isTrue();
	}

	@Test
	public void isChildFailTest() {
		// Arrange
		MedicalRecord testMedicalRecord = buildMedicalRecord("");
		testMedicalRecord.setBirthdate(LocalDate.now().minusYears(40));
		medicalRecordRepositoryUnderTest.addMedicalRecord(testMedicalRecord);

		Person testPerson = new Person();
		testPerson.setFirstName(testMedicalRecord.getFirstName());
		testPerson.setLastName(testMedicalRecord.getLastName());

		// Act
		boolean isChild = medicalRecordRepositoryUnderTest.isChild(testPerson);

		// Act + Assert
		assertThat(isChild).isFalse();
	}

	@Test
	public void isChildExceptionTest() {
		Person testPerson = new Person();
		assertThat(medicalRecordRepositoryUnderTest.isChild(testPerson)).isFalse();
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
		assertThrows(MedicalRecordNotFoundException.class, () -> medicalRecordRepositoryUnderTest
				.deleteMedicalRecordByName(TestConstants.firstName, TestConstants.lastName));
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
		medicalRecord.setFirstName(TestConstants.firstName + modifier);
		medicalRecord.setLastName(TestConstants.lastName + modifier);
		medicalRecord.setBirthdate(TestConstants.birthdate);
		medicalRecord.setMedications(TestConstants.medications);
		medicalRecord.setAllergies(TestConstants.allergies);
		return medicalRecord;
	}
}
