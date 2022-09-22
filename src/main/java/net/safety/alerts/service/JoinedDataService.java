package net.safety.alerts.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import net.safety.alerts.dto.ChildAlertDto;
import net.safety.alerts.dto.ChildDto;
import net.safety.alerts.dto.FireEndpointDto;
import net.safety.alerts.dto.PersonDto;
import net.safety.alerts.dto.FireEndpointPersonDto;
import net.safety.alerts.dto.PersonNameDto;
import net.safety.alerts.dto.StationNumberDto;
import net.safety.alerts.exceptions.AddressNotFoundException;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.exceptions.MedicalRecordNotFoundException;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.model.Person;
import net.safety.alerts.repository.FirestationRepository;
import net.safety.alerts.repository.MedicalRecordRepository;
import net.safety.alerts.repository.PersonRepository;
import net.safety.alerts.utils.Utils;

@Service
public class JoinedDataService { 

	@Autowired
	PersonRepository personRepository;

	@Autowired
	FirestationRepository firestationRepository;

	@Autowired
	MedicalRecordRepository medicalRecordRepository;

	@Autowired
	DtoService dtoService;

	//@Autowired
	//PersonService personService;

	public StationNumberDto stationNumber(@RequestParam Integer stationNumber) throws FirestationNotFoundException {

		String address = firestationRepository.getFirestationAddress(stationNumber);
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
		stationNumberDto.setPersons(personsCoveredDto);
		stationNumberDto.setAdultsCount((int) (adultsCount));
		stationNumberDto.setChildrenCount((int) (childrenCount));

		return stationNumberDto;

	}

	public ChildAlertDto childAlert(String address, PersonService personService) {

		List<Person> personsAtThisAddress = personRepository.getPersonsByAddress(address);
		List<Person> childrenAtThisAddress = personsAtThisAddress.stream().filter(p -> {
			try {
				return (medicalRecordRepository.getPersonAge(p) <= 18);
			} catch (MedicalRecordNotFoundException e) {
				return false;
			}
		}).toList();

		List<Person> otherHouseholdMembers = personsAtThisAddress.stream()
				.filter(p -> personService.isLastNamePresentInPersonList(p.getLastName(), childrenAtThisAddress))
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

		childAlertDto.setChildren(childrenDto);
		childAlertDto.setOtherHouseHoldMembers(otherHouseholdMembersDto);

		return childAlertDto;

	}

	public FireEndpointDto fire(String address) throws AddressNotFoundException {
		List<Person> persons = personRepository.getPersonsByAddress(address);
		
		if (persons.size() == 0) {
			throw new AddressNotFoundException();
		}
		
		List<FireEndpointPersonDto> personsFireDto = persons.stream()
				.map(p -> { 
					Optional<MedicalRecord> medicalRecord = medicalRecordRepository.getMedicalRecordByName(p.getFirstName(), p.getLastName());
					if (medicalRecord.isPresent()) {
						return dtoService.convertPersonToFireDto(p, Utils.calculateAge(medicalRecord.get().getBirthdate()), medicalRecord.get().getMedications(), medicalRecord.get().getAllergies());
					}
					else {
						return dtoService.convertPersonToFireDto(p, null, null, null);						
					}
				})
				.collect(Collectors.toList());
	
		FireEndpointDto fireDto = new FireEndpointDto();
		fireDto.setPersons(personsFireDto);
		try {
			fireDto.setFirestationNumber(firestationRepository.getFirestationNumber(address));
		} catch (FirestationNotFoundException e) {
			fireDto.setFirestationNumber(null);
		}
		
		return fireDto;
		
	}
	
}
