package net.safety.alerts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.safety.alerts.exceptions.MedicalRecordNotFoundException;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.repository.MedicalRecordRepository;
import net.safety.alerts.utils.MedicalRecordTestData;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {

	@InjectMocks
	private MedicalRecordService medicalRecordServiceUnderTest;

	@Mock
	private MedicalRecordRepository medicalRecordRepository;

	@Test
	public void addTest() {
		// Arrange
		MedicalRecord testMedicalRecord = MedicalRecordTestData.buildMedicalRecord();

		when(medicalRecordRepository.addMedicalRecord(any())).thenReturn(testMedicalRecord);

		// Act
		MedicalRecord result = medicalRecordServiceUnderTest.add(testMedicalRecord);

		// Assert
		assertThat(result).isEqualTo(testMedicalRecord);
		verify(medicalRecordRepository).addMedicalRecord(testMedicalRecord);
	}

	@Test
	public void getMedicalRecordByNameTest() throws MedicalRecordNotFoundException {
		// Arrange
		MedicalRecord testMedicalRecord = MedicalRecordTestData.buildMedicalRecord();

		when(medicalRecordRepository.getMedicalRecordByName(any(), any())).thenReturn(testMedicalRecord);

		// Act
		MedicalRecord result = medicalRecordServiceUnderTest.getMedicalRecordByName(testMedicalRecord.getFirstName(),
				testMedicalRecord.getLastName());

		// Assert
		assertThat(result).isEqualTo(testMedicalRecord);
	}

	@Test
	public void updateTest() throws MedicalRecordNotFoundException {
		// Arrange
		MedicalRecord testMedicalRecord = MedicalRecordTestData.buildMedicalRecord();
		MedicalRecord testMedicalRecordUpdated = MedicalRecordTestData.buildMedicalRecord();
		testMedicalRecordUpdated.setBirthdate(LocalDate.now());

		when(medicalRecordRepository.updateMedicalRecord(any())).thenReturn(testMedicalRecordUpdated);

		// Act
		MedicalRecord result = medicalRecordServiceUnderTest.update(testMedicalRecord);

		// Assert
		assertThat(result).isEqualTo(testMedicalRecordUpdated);
	}

	@Test
	public void deleteByNameTest() throws MedicalRecordNotFoundException {
		// Arrange
		String firstName = "firstName";
		String lastName = "lastName";

		// Act
		medicalRecordServiceUnderTest.deleteByName(firstName, lastName);

		// Assert
		verify(medicalRecordRepository).deleteMedicalRecordByName(firstName, lastName);
	}
}
