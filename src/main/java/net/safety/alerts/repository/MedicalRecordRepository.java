package net.safety.alerts.repository;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import net.safety.alerts.exceptions.MedicalRecordNotFoundException;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.model.Person;

/**
 * Contains MedicalRecord Data, CRUD and advanced filter methods
 * 
 * @author FranzKa
 *
 */
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

	public MedicalRecord getMedicalRecordByName(String firstName, String lastName)
			throws MedicalRecordNotFoundException {

		Optional<MedicalRecord> medicalRecord = listMedicalRecords.stream()
				.filter(m -> m.getFirstName().equals(firstName)).filter(m -> m.getLastName().equals(lastName))
				.findFirst();

		if (medicalRecord.isPresent()) {
			return medicalRecord.get();
		} else {
			throw new MedicalRecordNotFoundException();
		}
	}

	public MedicalRecord getMedicalRecord(Person person) throws MedicalRecordNotFoundException {
		return this.getMedicalRecordByName(person.getFirstName(), person.getLastName());
	}

	// update
	public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) throws MedicalRecordNotFoundException {

		MedicalRecord medicalRecordToUpdate = getMedicalRecordByName(medicalRecord.getFirstName(),
				medicalRecord.getLastName());

		int medicalRecordToUpdateIndex = listMedicalRecords.indexOf(medicalRecordToUpdate);

		listMedicalRecords.set(medicalRecordToUpdateIndex, medicalRecord);
		return medicalRecord;

	}

	// delete
	public void deleteMedicalRecord(MedicalRecord medicalRecord) throws MedicalRecordNotFoundException {
		if (!listMedicalRecords.remove(medicalRecord)) {
			throw new MedicalRecordNotFoundException();
		}
	}

	public void deleteMedicalRecordByName(String firstName, String lastName) throws MedicalRecordNotFoundException {

		MedicalRecord medicalRecordToDelete = getMedicalRecordByName(firstName, lastName);

		deleteMedicalRecord(medicalRecordToDelete);

	}

	// utils
	private Integer getPersonAge(Person person) throws MedicalRecordNotFoundException {

		Optional<MedicalRecord> personMedicalRecord = listMedicalRecords.stream()
				.filter(m -> m.getFirstName().equals(person.getFirstName()))
				.filter(m -> m.getLastName().equals(person.getLastName())).findFirst();

		if (personMedicalRecord.isPresent()) {
			return calculateAge(personMedicalRecord.get().getBirthdate());
		} else {
			throw new MedicalRecordNotFoundException();
		}
	}

	public boolean isAdult(Person person) {
		try {
			return (this.getPersonAge(person) > 18);
		} catch (MedicalRecordNotFoundException e) {
			return false;
		}
	}

	public boolean isChild(Person person) {
		try {
			return (this.getPersonAge(person) <= 18);
		} catch (MedicalRecordNotFoundException e) {
			return false;
		}
	}

	public int calculateAge(LocalDate birthdate) {
		Period duration = Period.between(birthdate, LocalDate.now());
		return duration.getYears();
	}

}
