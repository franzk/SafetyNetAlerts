package net.safety.alerts.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.exceptions.MedicalRecordNotFoundException;
import net.safety.alerts.model.Person;
import net.safety.alerts.model.PersonDto;
import net.safety.alerts.model.StationNumberDto;

@Repository
public class JoinedDataRepository {

	@Autowired
	PersonRepository personRepository;

	@Autowired
	FirestationRepository firestationRepository;

	@Autowired
	MedicalRecordRepository medicalRecordRepository;

	public StationNumberDto stationNumber(@RequestParam Integer stationNumber) throws FirestationNotFoundException {

		String address = firestationRepository.getFirestationAdress(stationNumber);
		List<Person> personsCovered = personRepository.getPersonsByAddress(address);
		List<PersonDto> personsCoveredDto = personsCovered.stream()
				.map(person -> personRepository.convertPersonToDto(person)).collect(Collectors.toList());

		
		long adultsCount = personsCovered.stream().filter(p -> {
			try {
				return (medicalRecordRepository.getPersonAge(p) > 18);
			} catch (MedicalRecordNotFoundException e) {
				return false;
			}
		}).count();
		
		long childrenCount = personsCovered.stream().filter(p -> {
			try {
				return (medicalRecordRepository.getPersonAge(p) <= 18);
			} catch (MedicalRecordNotFoundException e) {
				return false;
			}
		}).count();
		
		StationNumberDto stationNumberDto = new StationNumberDto();
		stationNumberDto.setPersons(personsCoveredDto.toArray(new PersonDto[0]));
		stationNumberDto.setAdultsCount((int)(adultsCount));
		stationNumberDto.setChildrenCount((int)(childrenCount));

		return stationNumberDto;

	}

}
