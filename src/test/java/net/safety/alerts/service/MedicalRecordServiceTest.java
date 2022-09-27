package net.safety.alerts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import net.safety.alerts.exceptions.MedicalRecordNotFoundException;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.repository.MedicalRecordRepository;
import net.safety.alerts.utils.TestConstants;

public class MedicalRecordServiceTest {

	private MedicalRecordService medicalRecordServiceUnderTest = new MedicalRecordService();

	private MedicalRecordRepository medicalRecordRepository;

	@BeforeEach
	public void reset() {
		medicalRecordRepository = new MedicalRecordRepository();
		ReflectionTestUtils.setField(medicalRecordServiceUnderTest, "medicalRecordRepository", medicalRecordRepository);
	}

	private MedicalRecord buildMedicalRecord() {
		return buildMedicalRecord(0);
	}

	private MedicalRecord buildMedicalRecord(Integer modifer) {
		MedicalRecord m = new MedicalRecord();

		m.setFirstName(TestConstants.firstName + Integer.toString(modifer));
		m.setLastName(TestConstants.lastName + Integer.toString(modifer));
		m.setBirthdate(TestConstants.birthdate.plusYears(modifer));

		List<String> medications = new ArrayList<>();
		medications.addAll(TestConstants.medications);
		medications.add(TestConstants.medications.get(0) + Integer.toString(modifer));
		m.setMedications(medications);

		List<String> allergies = new ArrayList<>();
		allergies.addAll(TestConstants.allergies);
		allergies.add(TestConstants.allergies.get(0) + Integer.toString(modifer));
		m.setAllergies(allergies);

		return m;
	}

	private MedicalRecord populateMedicalRecordRepository(Integer testMedicalRecordIndex) {
		MedicalRecord testMedicalRecord = new MedicalRecord();
		for (int i = 0; i < 24; i++) {
			MedicalRecord m = buildMedicalRecord(i);
			medicalRecordServiceUnderTest.add(m);
			if (i == testMedicalRecordIndex) {
				testMedicalRecord = m;
			}
		}
		return testMedicalRecord;
	}

	@Test
	public void addAndGetMedicalRecordByNameTest() {
		// Arrange
		MedicalRecord testMedicalRecord = buildMedicalRecord();

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
		MedicalRecord testMedicalRecord = buildMedicalRecord();
		medicalRecordServiceUnderTest.add(testMedicalRecord);

		MedicalRecord updatedMedicalRecord = buildMedicalRecord();
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
		MedicalRecord testMedicalRecord = populateMedicalRecordRepository(4);

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
