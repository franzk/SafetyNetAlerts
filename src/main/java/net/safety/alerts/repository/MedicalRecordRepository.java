package net.safety.alerts.repository;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import net.safety.alerts.exceptions.MedicalRecordNotFoundException;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.model.Person;

@Repository
public class MedicalRecordRepository {

	private List<MedicalRecord> listMedicalRecords = new ArrayList<>();

	// import
	public void setListMedicalRecords(List<MedicalRecord> listMedicalRecords) {
		this.listMedicalRecords = listMedicalRecords;
	}

	// create
	public MedicalRecord addMedicalRecord(MedicalRecord m) {
		listMedicalRecords.add(m);
		return m;
	}

	// read
	public Optional<MedicalRecord> getMedicalRecordByName(String firstName, String lastName) {
		return listMedicalRecords.stream().filter(medicalRecord -> medicalRecord.getFirstName().equals(firstName))
				.filter(medicalRecord -> medicalRecord.getLastName().equals(lastName)).findFirst();
	}

	// update
	public MedicalRecord updateMedicalRecord(MedicalRecord m) throws MedicalRecordNotFoundException {

		Optional<MedicalRecord> medicalRecordToUpdate = getMedicalRecordByName(m.getFirstName(), m.getLastName());
		if (medicalRecordToUpdate.isPresent()) {
			int medicalRecordToUpdateIndex = listMedicalRecords.indexOf(medicalRecordToUpdate.get());
			if (medicalRecordToUpdateIndex >= 0) {
				listMedicalRecords.set(medicalRecordToUpdateIndex, m);
				return m;
			}
		}
		throw new MedicalRecordNotFoundException();
	}

	// delete
	public void deleteMedicalRecord(MedicalRecord m) throws MedicalRecordNotFoundException {
		if (!listMedicalRecords.remove(m)) {
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
			
			Period duration = Period.between(
					personMedicalRecord.get().getBirthdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
					LocalDate.now());
			return duration.getYears();
		
		} else {
			
			throw new MedicalRecordNotFoundException();
		
		}

	}

}
