package net.safety.alerts.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import net.safety.alerts.dto.PersonDto;
import net.safety.alerts.dto.UrlChildAlertDto;
import net.safety.alerts.dto.UrlCommunityEmailDto;
import net.safety.alerts.dto.UrlFireDto;
import net.safety.alerts.dto.UrlFirestationCoverageDto;
import net.safety.alerts.dto.UrlFloodStationsAddress;
import net.safety.alerts.dto.UrlFloodStationsDto;
import net.safety.alerts.dto.UrlPersonInfoDto;
import net.safety.alerts.dto.UrlPhoneAlertDto;
import net.safety.alerts.exceptions.AddressNotFoundException;
import net.safety.alerts.exceptions.CityNotFoundException;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.exceptions.MedicalRecordNotFoundException;
import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.model.Person;
import net.safety.alerts.repository.FirestationRepository;
import net.safety.alerts.repository.MedicalRecordRepository;
import net.safety.alerts.repository.PersonRepository;
import net.safety.alerts.utils.DtoConstants;

@Service
public class UrlService {

	@Autowired
	PersonRepository personRepository;

	@Autowired
	FirestationRepository firestationRepository;

	@Autowired
	MedicalRecordRepository medicalRecordRepository;

	@Autowired
	DtoService dtoService;

	public UrlFirestationCoverageDto urlFirestationCoverage(@RequestParam Integer stationNumber)
			throws FirestationNotFoundException {

		DtoService dtoService = new DtoService();

		List<String> addresses = firestationRepository.getFirestationAddresses(stationNumber);
		List<Person> personsCovered = personRepository.getPersonsByAddresses(addresses);
		List<PersonDto> personsCoveredDto = personsCovered.stream()
				.map(person -> dtoService.buildPersonDto(person, DtoConstants.UrlFirestationCoveragePerson))
				.collect(Collectors.toList());

		long adultsCount = personsCovered.stream().filter(p -> medicalRecordRepository
				.isAdult(p)/*
							 * { try { return (medicalRecordRepository.getPersonAge(p) > 18); } catch
							 * (MedicalRecordNotFoundException e) { return false; } }
							 */).count();

		long childrenCount = personsCovered.stream().filter(p -> medicalRecordRepository
				.isChild(p) /*
							 * { try { return (medicalRecordRepository.getPersonAge(p) <= 18); } catch
							 * (MedicalRecordNotFoundException e) { return false; } }
							 */).count();

		UrlFirestationCoverageDto firestationCoverageDto = new UrlFirestationCoverageDto();
		firestationCoverageDto.setPersons(personsCoveredDto);
		firestationCoverageDto.setAdultsCount((int) (adultsCount));
		firestationCoverageDto.setChildrenCount((int) (childrenCount));

		return firestationCoverageDto;

	}

	public UrlChildAlertDto urlChildAlert(String address) throws AddressNotFoundException {

		List<Person> personsAtThisAddress = personRepository.getPersonsByAddress(address);
		List<Person> childrenAtThisAddress = personsAtThisAddress.stream()
				.filter(p -> medicalRecordRepository.isChild(p)).toList();

		List<Person> otherHouseholdMembers = personsAtThisAddress.stream()
				.filter(p -> childrenAtThisAddress.stream().anyMatch(c -> c.getLastName().equals(p.getLastName())))
				.filter(p -> medicalRecordRepository.isAdult(p)).collect(Collectors.toList());

		UrlChildAlertDto childAlertDto = new UrlChildAlertDto();

		List<PersonDto> childrenDto = childrenAtThisAddress.stream().map(p -> {
			try {
				return dtoService.buildPersonDto(p, medicalRecordRepository.getMedicalRecord(p),
						DtoConstants.UrlChildAlertChild);
			} catch (MedicalRecordNotFoundException e) {
				return dtoService.buildPersonDto(p, DtoConstants.UrlChildAlertChild);
			}
		}).collect(Collectors.toList());

		List<PersonDto> otherHouseholdMembersDto = otherHouseholdMembers.stream()
				.map(p -> dtoService.buildPersonDto(p, DtoConstants.UrlChildAlertAdult)).collect(Collectors.toList());

		childAlertDto.setChildren(childrenDto);
		childAlertDto.setOtherHouseHoldMembers(otherHouseholdMembersDto);

		return childAlertDto;

	}

	public UrlFireDto urlFire(String address) throws FirestationNotFoundException, AddressNotFoundException {

		Integer stationNumber = firestationRepository.getFirestationNumber(address);

		List<Person> persons = personRepository.getPersonsByAddress(address);

		List<PersonDto> personsFireDto = persons.stream().map(p -> this.convertPersonToUrlFireDto(p))
				.collect(Collectors.toList());

		UrlFireDto fireDto = new UrlFireDto();
		fireDto.setFirestationNumber(stationNumber);
		fireDto.setPersons(personsFireDto);

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

	public UrlFloodStationsDto urlFloodStations(List<Integer> stations) throws FirestationNotFoundException {

		List<String> addresses = new ArrayList<>();

		for (Integer station : stations) {
			addresses.addAll(firestationRepository.getFirestationAddresses(station));
		}

		UrlFloodStationsDto floodDto = new UrlFloodStationsDto();

		List<UrlFloodStationsAddress> addressesDto = addresses.stream().map(a -> {
			UrlFloodStationsAddress addressDto = new UrlFloodStationsAddress();
			addressDto.setAddress(a);
			List<Person> inhabitants = new ArrayList<>();
			try {
				inhabitants = personRepository.getPersonsByAddress(a);
			} catch (AddressNotFoundException e) {
				e.printStackTrace();
			}
			List<PersonDto> personsDto = inhabitants.stream().map(p -> this.convertPersonToUrlFireDto(p))
					.collect(Collectors.toList());
			addressDto.setInhabitants(personsDto);
			return addressDto;

		}).collect(Collectors.toList());

		floodDto.setAddresses(addressesDto);

		return floodDto;

	}

	public UrlPersonInfoDto getPersonInfoByName(String firstName, String lastName) throws PersonNotFoundException {

		List<Person> persons = personRepository.getPersonsByName(firstName, lastName);

		List<PersonDto> personsDto = persons.stream().map(p -> convertPersonToUrlPersonInfoItemDTO(p))
				.collect(Collectors.toList());

		UrlPersonInfoDto urlPersonInfoDto = new UrlPersonInfoDto();
		urlPersonInfoDto.setPersons(personsDto);

		return urlPersonInfoDto;
	}

	public UrlCommunityEmailDto urlCommunityEmail(String city) throws CityNotFoundException {
		UrlCommunityEmailDto communityDto = new UrlCommunityEmailDto();
		communityDto.setEmails(personRepository.getEmailsByCity(city));
		return communityDto;
	}

	// utils
	private PersonDto convertPersonToUrlPersonInfoItemDTO(Person person) {
		try {
			return dtoService.buildPersonDto(person, medicalRecordRepository.getMedicalRecord(person),
					DtoConstants.UrlPersonInfo);
		} catch (MedicalRecordNotFoundException e) {
			return dtoService.buildPersonDto(person, DtoConstants.UrlPersonInfo);
		}
	}

	private PersonDto convertPersonToUrlFireDto(Person person) {
		try {
			return dtoService.buildPersonDto(person, medicalRecordRepository.getMedicalRecord(person),
					DtoConstants.UrlFirePerson);
		} catch (MedicalRecordNotFoundException e) {
			return dtoService.buildPersonDto(person, DtoConstants.UrlFirePerson);
		}
	}

}
