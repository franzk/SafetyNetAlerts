package net.safety.alerts.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import net.safety.alerts.dto.ChildAlertDto;
import net.safety.alerts.dto.ChildDto;
import net.safety.alerts.dto.FireDto;
import net.safety.alerts.dto.PersonDto;
import net.safety.alerts.dto.PersonFireDto;
import net.safety.alerts.dto.PersonNameDto;
import net.safety.alerts.dto.StationNumberDto;
import net.safety.alerts.exceptions.AddressNotFoundException;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.exceptions.MedicalRecordNotFoundException;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.model.Person;
import net.safety.alerts.service.DtoService;
import net.safety.alerts.service.PersonService;

@Repository
public class JoinedDataRepository {

	@Autowired
	PersonRepository personRepository;

	@Autowired
	FirestationRepository firestationRepository;

	@Autowired
	MedicalRecordRepository medicalRecordRepository;

	@Autowired
	DtoService dtoService;

	@Autowired
	PersonService personService;

	public StationNumberDto stationNumber(@RequestParam Integer stationNumber) throws FirestationNotFoundException {

		String address = firestationRepository.getFirestationAdress(stationNumber);
		List<Person> personsCovered = personRepository.getPersonsByAddress(address);
		List<PersonDto> personsCoveredDto = personsCovered.stream()
				.map(person -> dtoService.convertPersonToPersonDto(person)).collect(Collectors.toList());

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
		stationNumberDto.setAdultsCount((int) (adultsCount));
		stationNumberDto.setChildrenCount((int) (childrenCount));

		return stationNumberDto;

	}

	public ChildAlertDto childAlert(String address) {

		List<Person> personsAtThisAddress = personRepository.getPersonsByAddress(address);
		List<Person> childrenAtThisAddress = personsAtThisAddress.stream().filter(p -> {
			try {
				return (medicalRecordRepository.getPersonAge(p) <= 18);
			} catch (MedicalRecordNotFoundException e) {
				return false;
			}
		}).toList();

		List<Person> otherHouseholdMembers = personsAtThisAddress.stream()
				.filter(p -> personService.isPhonePresentInPersonList(p.getPhone(), childrenAtThisAddress))
				.filter(p -> {
					try {
						return medicalRecordRepository.getPersonAge(p) > 18;
					} catch (MedicalRecordNotFoundException e) {
						return true;
					}
				}).collect(Collectors.toList());

		ChildAlertDto childAlertDto = new ChildAlertDto();

		List<ChildDto> childrenDto = childrenAtThisAddress.stream().map(p -> {
			try {
				return dtoService.convertPersonToChildDto(p, medicalRecordRepository.getPersonAge(p));
			} catch (MedicalRecordNotFoundException e) {
				return dtoService.convertPersonToChildDto(p, null);
			}
		}).collect(Collectors.toList());

		List<PersonNameDto> otherHouseholdMembersDto = otherHouseholdMembers.stream()
				.map(p -> dtoService.convertPersonToPersonNameDto(p)).collect(Collectors.toList());

		childAlertDto.setChildren(childrenDto.toArray(new ChildDto[0]));
		childAlertDto.setOtherHouseHoldMembers(otherHouseholdMembersDto.toArray(new PersonNameDto[0]));

		return childAlertDto;

	}

	public FireDto fire(String address) throws AddressNotFoundException {
		List<Person> persons = personRepository.getPersonsByAddress(address);
		
		if (persons.size() == 0) {
			throw new AddressNotFoundException();
		}
		
		List<PersonFireDto> personsFireDto = persons.stream()
				.map(p -> { 
					Optional<MedicalRecord> medicalRecord = medicalRecordRepository.getMedicalRecordByName(p.getFirstName(), p.getLastName());
					if (medicalRecord.isPresent()) {
						return dtoService.convertPersonToFireDto(p, medicalRecordRepository.calculateAge(medicalRecord.get().getBirthdate()), medicalRecord.get().getMedications(), medicalRecord.get().getAllergies());
					}
					else {
						return dtoService.convertPersonToFireDto(p, null, null, null);						
					}
				})
				.collect(Collectors.toList());
	
		FireDto fireDto = new FireDto();
		fireDto.setPersons(personsFireDto.toArray(new PersonFireDto[0]));
		try {
			fireDto.setFirestationNumber(firestationRepository.getFirestationNumber(address));
		} catch (FirestationNotFoundException e) {
			fireDto.setFirestationNumber(null);
		}
		
		return fireDto;
		
	}
	
}
