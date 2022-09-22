package net.safety.alerts.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import net.safety.alerts.exceptions.MedicalRecordNotFoundException;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.model.Person;
import net.safety.alerts.utils.Utils;

@Repository
public class MedicalRecordRepository {

	private List<MedicalRecord> listMedicalRecords = new ArrayList<>();

	// import
	public void setListMedicalRecords(List<MedicalRecord> listMedicalRecords) {
		this.listMedicalRecords = listMedicalRecords;
	}

	// create
	public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
		listMedicalRecords.add(medicalRecord);
		return medicalRecord;
	}

	// read
	public Optional<MedicalRecord> getMedicalRecordByName(String firstName, String lastName) {
		return listMedicalRecords.stream()
				.filter(m -> m.getFirstName().equals(firstName))
				.filter(m -> m.getLastName().equals(lastName))
				.findFirst();
	}

	// update
	public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) throws MedicalRecordNotFoundException {

		Optional<MedicalRecord> medicalRecordToUpdate = getMedicalRecordByName(medicalRecord.getFirstName(), medicalRecord.getLastName());
		if (medicalRecordToUpdate.isPresent()) {
			int medicalRecordToUpdateIndex = listMedicalRecords.indexOf(medicalRecordToUpdate.get());
			if (medicalRecordToUpdateIndex >= 0) { // si c'est intestable, on enlève le if
				listMedicalRecords.set(medicalRecordToUpdateIndex, medicalRecord);
				return medicalRecord;
			}
		}
		throw new MedicalRecordNotFoundException();
	}

	// delete
	public void deleteMedicalRecord(MedicalRecord medicalRecord) throws MedicalRecordNotFoundException {
		if (!listMedicalRecords.remove(medicalRecord)) {
			throw new MedicalRecordNotFoundException();
		}
	}

	public void deleteMedicalRecordByName(String firstName, String lastName) throws MedicalRecordNotFoundException {
		Optional<MedicalRecord> medicalRecordToDelete = getMedicalRecordByName(firstName, lastName);
		if (medicalRecordToDelete.isPresent()) {
			deleteMedicalRecord(medicalRecordToDelete.get());
		} else {
			throw new MedicalRecordNotFoundException();
		}
	}

	// utils
	public Integer getPersonAge(Person person) throws MedicalRecordNotFoundException {
		Optional<MedicalRecord> personMedicalRecord = listMedicalRecords.stream()
				.filter(m -> m.getFirstName().equals(person.getFirstName()))
				.filter(m -> m.getLastName().equals(person.getLastName())).findFirst();

		if (personMedicalRecord.isPresent()) {
			return Utils.calculateAge(personMedicalRecord.get().getBirthdate());
		} else {
			throw new MedicalRecordNotFoundException();
		}
	}
	
}
