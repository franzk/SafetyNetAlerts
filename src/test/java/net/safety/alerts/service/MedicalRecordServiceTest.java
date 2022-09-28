package net.safety.alerts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;
import org.springframework.test.util.ReflectionTestUtils;

import net.safety.alerts.exceptions.MedicalRecordNotFoundException;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.repository.MedicalRecordRepository;
import net.safety.alerts.utils.BuildMedicalRecordTestData;
import net.safety.alerts.utils.TestConstants;

@Component
public class MedicalRecordServiceTest {

	private MedicalRecordService medicalRecordServiceUnderTest = new MedicalRecordService();

	private MedicalRecordRepository medicalRecordRepository;

	private BuildMedicalRecordTestData medicalRecordTestData = new BuildMedicalRecordTestData();

	@BeforeEach
	public void reset() {
		medicalRecordRepository = new MedicalRecordRepository();
		ReflectionTestUtils.setField(medicalRecordServiceUnderTest, "medicalRecordRepository", medicalRecordRepository);
	}

	@Test
	public void addAndGetMedicalRecordByNameTest() {
		// Arrange
		MedicalRecord testMedicalRecord = medicalRecordTestData.getMedicalRecord();

		// Act
		medicalRecordServiceUnderTest.add(testMedicalRecord);

		MedicalRecord result = new MedicalRecord();
		try {
			result = medicalRecordServiceUnderTest.getMedicalRecordByName(testMedicalRecord.getFirstName(),
					testMedicalRecord.getLastName());
		} catch (MedicalRecordNotFoundException e) {
			fail("addAndGetMedicalRecordByNameTest threw an exception");
		}

		// Assert
		assertThat(result).isEqualTo(testMedicalRecord);
	}

	@Test
	public void updateTest() {
		// Arrange
		MedicalRecord testMedicalRecord = medicalRecordTestData.getMedicalRecord();
		medicalRecordServiceUnderTest.add(testMedicalRecord);

		MedicalRecord updatedMedicalRecord = medicalRecordTestData.getMedicalRecord();
		updatedMedicalRecord.setBirthdate(TestConstants.birthdate.plusYears(42));

		// Act
		MedicalRecord result = new MedicalRecord();
		try {
			medicalRecordServiceUnderTest.update(updatedMedicalRecord);
		} catch (MedicalRecordNotFoundException e) {
			fail("updateTest (act) threw an exception");
		}

		// Assert
		try {
			result = medicalRecordServiceUnderTest.getMedicalRecordByName(testMedicalRecord.getFirstName(),
					testMedicalRecord.getLastName());
		} catch (MedicalRecordNotFoundException e) {
			fail("updateTest (assert) threw an exception");
		}
		assertThat(result).isEqualTo(updatedMedicalRecord);
	}

	@Test
	public void deleteByNameTest() {
		// Arrange
		List<MedicalRecord> listMedicalRecords = medicalRecordTestData.getMedicalRecordList();
		for (MedicalRecord m : listMedicalRecords) {
			medicalRecordServiceUnderTest.add(m);
		}
		MedicalRecord testMedicalRecord = listMedicalRecords.get(5);
		
		// Act
		try {
			medicalRecordServiceUnderTest.deleteByName(testMedicalRecord.getFirstName(),
					testMedicalRecord.getLastName());
		} catch (MedicalRecordNotFoundException e) {
			fail("deleteByNameTest threw an exception");
		}

		// Assert
		assertThrows(MedicalRecordNotFoundException.class, () -> medicalRecordServiceUnderTest
				.getMedicalRecordByName(testMedicalRecord.getFirstName(), testMedicalRecord.getLastName()));
	}
}
