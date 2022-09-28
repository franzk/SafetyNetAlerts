package net.safety.alerts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.safety.alerts.exceptions.MedicalRecordNotFoundException;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.service.MedicalRecordService;
import net.safety.alerts.utils.MedicalRecordTestData;

public class EndpointMedicalRecordControllerTest {

	@Mock
	private MedicalRecordService medicalRecordService;
	
	@InjectMocks
	private EndpointMedicalRecordController controllerUnderTest;
	
	@BeforeEach
	public void reset() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void addMedicalRecordTest() {
		// Arrange
		MedicalRecord testMedicalRecord = MedicalRecordTestData.buildMedicalRecord();
		when(medicalRecordService.add(any())).thenReturn(testMedicalRecord);
		
		// Act
		ResponseEntity<MedicalRecord> result = controllerUnderTest.addMedicalRecord(testMedicalRecord);
	
		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(testMedicalRecord);
	}
	
	@Test
	public void getMedicalRecordTest() {
		// Arrange
		MedicalRecord testMedicalRecord = MedicalRecordTestData.buildMedicalRecord();
		try {
			when(medicalRecordService.getMedicalRecordByName(any(), any())).thenReturn(testMedicalRecord);
		} catch (MedicalRecordNotFoundException e) {
			fail("getMedicalRecordTest (arrange) threw an exception");
		}
		
		// Act
		ResponseEntity<MedicalRecord> result = null;
		try {
			result = controllerUnderTest.getMedicalRecord(testMedicalRecord.getFirstName(), testMedicalRecord.getLastName());
		} catch (MedicalRecordNotFoundException e) {
			fail("getMedicalRecordTest (act) threw an exception");
		}
		
		// Arrange
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(testMedicalRecord);
	}
	
	@Test
	public void updateMedicalRecordTest() {
		// Arrange
		MedicalRecord testMedicalRecord = MedicalRecordTestData.buildMedicalRecord();
		try {
			when(medicalRecordService.update(any())).thenReturn(testMedicalRecord);
		} catch (MedicalRecordNotFoundException e) {
			fail("updateMedicalRecordTest (arrange) threw an exception");
		}
		
		// Act
		ResponseEntity<MedicalRecord> result = null;
		try {
			result = controllerUnderTest.updateMedicalRecord(testMedicalRecord);
		} catch (MedicalRecordNotFoundException e) {
			fail("updateMedicalRecordTest (act) threw an exception");
		}
		
		// Arrange
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(testMedicalRecord);
	}
	
	@Test
	public void deleteMedicalRecordTest() {
		// Arrange
		MedicalRecord medicalRecord = MedicalRecordTestData.buildMedicalRecord();
		
		// Act
		try {
			controllerUnderTest.deleteMedicalRecord(medicalRecord.getFirstName(), medicalRecord.getLastName());
		} catch (MedicalRecordNotFoundException e) {
			fail("deleteMedicalRecordTest (act) threw an exception");
		}
		
		// Assert
		try {
			verify(medicalRecordService).deleteByName(medicalRecord.getFirstName(), medicalRecord.getLastName());
		} catch (MedicalRecordNotFoundException e) {
			fail("deleteMedicalRecordTest (assert) threw an exception");
		}
	}
	
}
