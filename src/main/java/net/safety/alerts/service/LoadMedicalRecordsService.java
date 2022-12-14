package net.safety.alerts.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.repository.MedicalRecordRepository;
import net.safety.alerts.utils.JsonFileConstants;

/**
 * Import Json data into MedicalRecord Repository
 * 
 * @author FranzKa
 *
 */
@Service
public class LoadMedicalRecordsService {

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;
	
	/**
	 * Import Json data into MedicalRecord Repository
	 * @param medicalrecordsNode 
	 */
	public void loadMedicalRecords(JsonNode medicalrecordsNode) {

		for (JsonNode medicalRecordNode : medicalrecordsNode) {
			MedicalRecord medicalRecord = new MedicalRecord();
			medicalRecord.setFirstName(medicalRecordNode.path(JsonFileConstants.medicalrecord_firstName).asText());
			medicalRecord.setLastName(medicalRecordNode.path(JsonFileConstants.medicalrecord_lastName).asText());

			medicalRecord.setBirthdate(
					this.stringToDate(medicalRecordNode.path(JsonFileConstants.medicalrecord_birthdate).asText()));

			// medications
			List<String> medications = new ArrayList<>();
			JsonNode medicationData = medicalRecordNode.path(JsonFileConstants.medicalrecord_medications);
			for (JsonNode medicalrecordNode : medicationData) {
				medications.add(medicalrecordNode.asText());
			}
			medicalRecord.setMedications(medications);

			// allergies
			List<String> allergies = new ArrayList<>();
			JsonNode allergiesData = medicalRecordNode.path(JsonFileConstants.medicalrecord_allergies);
			for (JsonNode allergieNode : allergiesData) {
				allergies.add(allergieNode.asText());
			}
			medicalRecord.setAllergies(allergies);

			medicalRecordRepository.addMedicalRecord(medicalRecord);
		}

	}
	
	public LocalDate stringToDate(String strDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		return LocalDate.parse(strDate, formatter);
	}
	
}
