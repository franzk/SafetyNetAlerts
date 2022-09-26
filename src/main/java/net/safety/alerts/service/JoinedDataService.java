package net.safety.alerts.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import net.safety.alerts.dto.UrlChildAlertDto;
import net.safety.alerts.dto.ChildDto;
import net.safety.alerts.dto.UrlFireDto;
import net.safety.alerts.dto.UrlFirePersonDto;
import net.safety.alerts.dto.PersonDto;
import net.safety.alerts.dto.PersonNameDto;
import net.safety.alerts.dto.UrlFirestationCoverageDto;
import net.safety.alerts.dto.UrlPhoneAlertDto;
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

	public UrlFirestationCoverageDto firestationCoverage(@RequestParam Integer stationNumber)
			throws FirestationNotFoundException {

		DtoService dtoService = new DtoService();

		List<String> addresses = firestationRepository.getFirestationAddresses(stationNumber);
		List<Person> personsCovered = personRepository.getPersonsByAddresses(addresses);
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

		UrlFirestationCoverageDto firestationCoverageDto = new UrlFirestationCoverageDto();
		firestationCoverageDto.setPersons(personsCoveredDto);
		firestationCoverageDto.setAdultsCount((int) (adultsCount));
		firestationCoverageDto.setChildrenCount((int) (childrenCount));

		return firestationCoverageDto;

	}

	public UrlChildAlertDto childAlert(String address, PersonService personService) {

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

		UrlChildAlertDto childAlertDto = new UrlChildAlertDto();

		DtoService dtoService = new DtoService();

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

	public UrlFireDto fire(String address) throws AddressNotFoundException {
		List<Person> persons = personRepository.getPersonsByAddress(address);

		if (persons.size() == 0) {
			throw new AddressNotFoundException();
		}

		DtoService dtoService = new DtoService();

		List<UrlFirePersonDto> personsFireDto = persons.stream().map(p -> {
			Optional<MedicalRecord> medicalRecord = medicalRecordRepository
					.getOptionalMedicalRecordByName(p.getFirstName(), p.getLastName());
			if (medicalRecord.isPresent()) {
				return dtoService.convertPersonToUrlFireDto(p, Utils.calculateAge(medicalRecord.get().getBirthdate()),
						medicalRecord.get().getMedications(), medicalRecord.get().getAllergies());
			} else {
				return dtoService.convertPersonToUrlFireDto(p, null, null, null);
			}
		}).collect(Collectors.toList());

		UrlFireDto fireDto = new UrlFireDto();
		fireDto.setPersons(personsFireDto);
		try {
			fireDto.setFirestationNumber(firestationRepository.getFirestationNumber(address));
		} catch (FirestationNotFoundException e) {
			fireDto.setFirestationNumber(null);
		}

		return fireDto;

	}

	public UrlPhoneAlertDto urlPhoneAlert(Integer firestationNumber) throws FirestationNotFoundException {
		List<String> addresses = firestationRepository.getFirestationAddresses(firestationNumber);
		List<Person> persons = personRepository.getPersonsByAddresses(addresses);

		// utiliser Set pour éviter les doublons
		Set<String> phoneNumbers = persons.stream().map(Person::getPhone).collect(Collectors.toSet());

		UrlPhoneAlertDto urlFirestationDto = new UrlPhoneAlertDto();
		urlFirestationDto.setPhoneNumbers(phoneNumbers);

		return urlFirestationDto;
	}

}
