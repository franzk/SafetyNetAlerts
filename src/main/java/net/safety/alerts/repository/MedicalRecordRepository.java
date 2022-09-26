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

	public Optional<MedicalRecord> getOptionalMedicalRecordByName(String firstName, String lastName) {

		return listMedicalRecords.stream().filter(m -> m.getFirstName().equals(firstName))
				.filter(m -> m.getLastName().equals(lastName)).findFirst();

	}

	public MedicalRecord getMedicalRecordByName(String firstName, String lastName)
			throws MedicalRecordNotFoundException {

		Optional<MedicalRecord> medicalRecord = getOptionalMedicalRecordByName(firstName, lastName);

		if (medicalRecord.isPresent()) {
			return medicalRecord.get();
		} else {
			throw new MedicalRecordNotFoundException();
		}
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
