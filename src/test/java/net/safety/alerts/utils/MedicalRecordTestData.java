package net.safety.alerts.utils;

import java.util.ArrayList;
import java.util.List;

import net.safety.alerts.model.MedicalRecord;

public class MedicalRecordTestData {

	public static MedicalRecord buildMedicalRecord() {
		return buildMedicalRecord(0);
	}

	public static MedicalRecord buildMedicalRecord(Integer modifer) {
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
	
	public static List<MedicalRecord> buildMedicalRecordList() {
		List<MedicalRecord> listMedicalRecords = new ArrayList<>();
		for (int i = 0; i < 24; i++) {
			listMedicalRecords.add(buildMedicalRecord(i));
		}
		return listMedicalRecords;
	}
	
}
