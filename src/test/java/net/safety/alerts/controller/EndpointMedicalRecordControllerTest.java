package net.safety.alerts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.safety.alerts.exceptions.MedicalRecordNotFoundException;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.service.MedicalRecordService;
import net.safety.alerts.utils.MedicalRecordTestData;

@ExtendWith(MockitoExtension.class)
public class EndpointMedicalRecordControllerTest {

	@Mock
	private MedicalRecordService medicalRecordService;

	@InjectMocks
	private EndpointMedicalRecordController controllerUnderTest;
	
	@Autowired
	private MedicalRecordTestData medicalRecordTestData;

	@Test
	public void addMedicalRecordTest() {
		// Arrange
		MedicalRecord testMedicalRecord = medicalRecordTestData.buildMedicalRecord();
		when(medicalRecordService.add(any())).thenReturn(testMedicalRecord);

		// Act
		ResponseEntity<MedicalRecord> result = controllerUnderTest.addMedicalRecord(null);

		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(testMedicalRecord);
	}

	@Test
	public void getMedicalRecordTest() throws MedicalRecordNotFoundException {
		// Arrange
		MedicalRecord testMedicalRecord = medicalRecordTestData.buildMedicalRecord();

		when(medicalRecordService.getMedicalRecordByName(any(), any())).thenReturn(testMedicalRecord);

		// Act
		ResponseEntity<MedicalRecord> result = controllerUnderTest.getMedicalRecord(testMedicalRecord.getFirstName(),
				testMedicalRecord.getLastName());

		// Arrange
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(testMedicalRecord);
	}

	@Test
	public void updateMedicalRecordTest() throws MedicalRecordNotFoundException {
		// Arrange
		MedicalRecord testMedicalRecord = medicalRecordTestData.buildMedicalRecord();
		when(medicalRecordService.update(any())).thenReturn(testMedicalRecord);

		// Act
		ResponseEntity<MedicalRecord> result = null;
		result = controllerUnderTest.updateMedicalRecord(testMedicalRecord);

		// Arrange
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(testMedicalRecord);
	}

	@Test
	public void deleteMedicalRecordTest() throws MedicalRecordNotFoundException {
		// Arrange
		MedicalRecord medicalRecord = medicalRecordTestData.buildMedicalRecord();

		// Act
		controllerUnderTest.deleteMedicalRecord(medicalRecord.getFirstName(), medicalRecord.getLastName());

		// Assert
		verify(medicalRecordService).deleteByName(medicalRecord.getFirstName(), medicalRecord.getLastName());
	}

}
