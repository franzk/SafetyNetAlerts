package net.safety.alerts.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import net.safety.alerts.dto.ChildAlertDto;
import net.safety.alerts.dto.ChildDto;
import net.safety.alerts.dto.PersonDto;
import net.safety.alerts.dto.PersonNameDto;
import net.safety.alerts.dto.StationNumberDto;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.exceptions.MedicalRecordNotFoundException;
import net.safety.alerts.model.Person;

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
				.map(person -> personRepository.convertPersonToPersonDto(person)).collect(Collectors.toList());

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
				.filter(p -> isPhonePresentInPersonList(p.getPhone(), childrenAtThisAddress))
				.filter(p -> {
					try {
						return medicalRecordRepository.getPersonAge(p) > 18;
					} catch (MedicalRecordNotFoundException e) {
						return true;
					}
				})
				.collect(Collectors.toList());


		ChildAlertDto childAlertDto = new ChildAlertDto();
		
		List<ChildDto> childrenDto = childrenAtThisAddress.stream()
				.map(p -> convertPersonToChildDto(p))
				.collect(Collectors.toList());
		
		List<PersonNameDto> otherHouseholdMembersDto = otherHouseholdMembers.stream()
				.map(p -> personRepository.convertPersonToPersonNameDto(p))
				.collect(Collectors.toList());
		
		childAlertDto.setChildren(childrenDto.toArray(new ChildDto[0]));
		childAlertDto.setOtherHouseHoldMembers(otherHouseholdMembersDto.toArray(new PersonNameDto[0]));
	
		return childAlertDto;
	
	}

	
	private boolean isPhonePresentInPersonList(String phone, List<Person> listPerson) {
		Optional<Person> person = listPerson.stream().filter(p -> p.getPhone().equals(phone)).findFirst();
		return person.isPresent();
	}
	
	public ChildDto convertPersonToChildDto(Person person) {
		ChildDto childDto = new ChildDto();
		childDto.setFirstName(person.getFirstName());
		childDto.setLastName(person.getLastName());
		try {
			childDto.setAge(medicalRecordRepository.getPersonAge(person));
		} catch (MedicalRecordNotFoundException e) {
			childDto.setAge(null);
		}
		return childDto;
	}

}
