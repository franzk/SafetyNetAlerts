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

	// create / update
	public MedicalRecord save(MedicalRecord m) {

		try {
			medicalRecordRepository.updateMedicalRecord(m);
		} catch (MedicalRecordNotFoundException e) {
			medicalRecordRepository.addMedicalRecord(m);
		}

		return m;

	}
	
	// delete
	public void deleteByName(String firstName, String lastName) throws MedicalRecordNotFoundException  {
		medicalRecordRepository.deleteMedicalRecordByName(firstName, lastName);
	}	

}
