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
import net.safety.alerts.dto.UrlFloodStationsAddressDto;
import net.safety.alerts.dto.UrlFloodStationsDto;
import net.safety.alerts.dto.UrlPersonInfoDto;
import net.safety.alerts.dto.UrlPhoneAlertDto;
import net.safety.alerts.exceptions.AddressNotFoundException;
import net.safety.alerts.exceptions.CityNotFoundException;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.exceptions.MedicalRecordNotFoundException;
import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.model.Person;
import net.safety.alerts.repository.FirestationRepository;
import net.safety.alerts.repository.MedicalRecordRepository;
import net.safety.alerts.repository.PersonRepository;
import net.safety.alerts.utils.DtoConstants;

/**
 * description
 * 
 * @author FranzKa
 *
 */
@Service
public class UrlService {

	@Autowired
	PersonRepository personRepository;

	@Autowired
	FirestationRepository firestationRepository;

	@Autowired
	MedicalRecordRepository medicalRecordRepository;

	/**
	 * description
	 * 
	 * @param stationNumber
	 * @return
	 * @throws FirestationNotFoundException
	 */
	public UrlFirestationCoverageDto urlFirestationCoverage(@RequestParam Integer stationNumber)
			throws FirestationNotFoundException {

		List<String> addresses = firestationRepository.getFirestationAddresses(stationNumber);
		List<Person> personsCovered = personRepository.getPersonsByAddresses(addresses);
		List<PersonDto> personsCoveredDto = personsCovered.stream()
				.map(person -> DtoService.buildPersonDto(person, DtoConstants.UrlFirestationCoveragePerson))
				.collect(Collectors.toList());

		long adultsCount = personsCovered.stream().filter(p -> 
		medicalRecordRepository.isAdult(p)
		).count();

		long childrenCount = personsCovered.stream().filter(p -> medicalRecordRepository.isChild(p)).count();

		UrlFirestationCoverageDto firestationCoverageDto = new UrlFirestationCoverageDto();
		firestationCoverageDto.setPersons(personsCoveredDto);
		firestationCoverageDto.setAdultsCount((int) (adultsCount));
		firestationCoverageDto.setChildrenCount((int) (childrenCount));

		return firestationCoverageDto;

	}

	public UrlChildAlertDto urlChildAlert(String address) throws AddressNotFoundException {

		List<Person> personsAtThisAddress = personRepository.getPersonsByAddress(address);
		List<Person> childrenAtThisAddress = personsAtThisAddress.stream()
				.filter(p -> medicalRecordRepository.isChild(p)).collect(Collectors.toList());

		List<Person> otherHouseholdMembers = personsAtThisAddress.stream()
				.filter(p -> childrenAtThisAddress.stream().anyMatch(c -> c.getLastName().equals(p.getLastName())))
				.filter(p -> !medicalRecordRepository.isChild(p)).collect(Collectors.toList());

		UrlChildAlertDto childAlertDto = new UrlChildAlertDto();

		List<PersonDto> childrenDto = childrenAtThisAddress.stream().map(p -> {
			try {
				MedicalRecord m = medicalRecordRepository.getMedicalRecord(p);
				return DtoService.buildPersonDto(p, m, DtoConstants.UrlChildAlertChild);
			} catch (MedicalRecordNotFoundException e) {
				return DtoService.buildPersonDto(p, DtoConstants.UrlChildAlertChild); // ce cas ne peut pas arriver car les children ont forcément un medical recod
			}
		}).collect(Collectors.toList());

		List<PersonDto> otherHouseholdMembersDto = otherHouseholdMembers.stream()
				.map(p -> DtoService.buildPersonDto(p, DtoConstants.UrlChildAlertAdult)).collect(Collectors.toList());

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
		Set<String> phoneNumbers = persons.stream().map(p -> p.getPhone()).collect(Collectors.toSet());

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

		List<UrlFloodStationsAddressDto> addressesDto = addresses.stream().map(a -> {
			UrlFloodStationsAddressDto addressDto = new UrlFloodStationsAddressDto();
			addressDto.setAddress(a);
			List<Person> inhabitants = new ArrayList<>();
			try {
				inhabitants = personRepository.getPersonsByAddress(a);
			} catch (AddressNotFoundException e) {
				// nobody is living at this address.
			}
			List<PersonDto> personsDto = inhabitants.stream().map(p -> this.convertPersonToUrlFireDto(p))
					.collect(Collectors.toList());
			addressDto.setInhabitants(personsDto);
			return addressDto;

		}).collect(Collectors.toList());

		floodDto.setAddresses(addressesDto);

		return floodDto;

	}

	public UrlPersonInfoDto urlPersonInfo(String firstName, String lastName) throws PersonNotFoundException {

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
			return DtoService.buildPersonDto(person, medicalRecordRepository.getMedicalRecord(person),
					DtoConstants.UrlPersonInfo);
		} catch (MedicalRecordNotFoundException e) {
			return DtoService.buildPersonDto(person, DtoConstants.UrlPersonInfo);
		}
	}

	private PersonDto convertPersonToUrlFireDto(Person person) {
		try {
			return DtoService.buildPersonDto(person, medicalRecordRepository.getMedicalRecord(person),
					DtoConstants.UrlFirePerson);
		} catch (MedicalRecordNotFoundException e) {
			return DtoService.buildPersonDto(person, DtoConstants.UrlFirePerson);
		}
	}

}
