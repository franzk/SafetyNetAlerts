package net.safety.alerts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.safety.alerts.exceptions.MedicalRecordNotFoundException;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.repository.MedicalRecordRepository;

@Service
public class MedicalRecordService {

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	// create
	public MedicalRecord add(MedicalRecord m) {
		return medicalRecordRepository.addMedicalRecord(m);
	}
	
	// update
	public MedicalRecord update(MedicalRecord medicalRecord) throws MedicalRecordNotFoundException {
		return medicalRecordRepository.updateMedicalRecord(medicalRecord);
	}
	
	// delete
	public void deleteByName(String firstName, String lastName) throws MedicalRecordNotFoundException {
		medicalRecordRepository.deleteMedicalRecordByName(firstName, lastName);
	}

	public MedicalRecord getMedicalRecordByName(String firstName, String lastName) {
		// TODO Auto-generated method stub
		return null;
	}

}
