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
 * Contains the logic that generate URL results
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
	
	@Autowired
	DtoService dtoService;

	/**
	 * logic of "/firestationCoverage" URL
	 * 
	 * @param stationNumber
	 * @return {@link UrlFirestationCoverageDto}
	 * @throws FirestationNotFoundException
	 */
	public UrlFirestationCoverageDto urlFirestationCoverage(@RequestParam Integer stationNumber)
			throws FirestationNotFoundException {

		// get persons covered by the firestation
		List<String> addresses = firestationRepository.getFirestationAddresses(stationNumber);
		List<Person> personsCovered = personRepository.getPersonsByAddresses(addresses);

		// convert them to DTO objects
		List<PersonDto> personsCoveredDto = personsCovered.stream()
				.map(person -> dtoService.buildPersonDto(person, DtoConstants.UrlFirestationCoveragePerson))
				.collect(Collectors.toList());

		// calculate counts
		long adultsCount = personsCovered.stream().filter(p -> medicalRecordRepository.isAdult(p)).count();

		long childrenCount = personsCovered.stream().filter(p -> medicalRecordRepository.isChild(p)).count();

		// create the final DTO object that will be returned by URL Controller
		UrlFirestationCoverageDto firestationCoverageDto = new UrlFirestationCoverageDto();
		firestationCoverageDto.setPersons(personsCoveredDto);
		firestationCoverageDto.setAdultsCount((int) (adultsCount));
		firestationCoverageDto.setChildrenCount((int) (childrenCount));

		return firestationCoverageDto;

	}

	/**
	 * logic of "/childAlert" URL
	 * 
	 * @param address
	 * @return {@link UrlChildAlertDto}
	 * @throws AddressNotFoundException
	 */
	public UrlChildAlertDto urlChildAlert(String address) throws AddressNotFoundException {

		// get Persons at the adress
		List<Person> personsAtThisAddress = personRepository.getPersonsByAddress(address);

		// filter only children
		List<Person> childrenAtThisAddress = personsAtThisAddress.stream()
				.filter(p -> medicalRecordRepository.isChild(p)).collect(Collectors.toList());

		// find other people with same last names as the children
		// (otherHouseholdMembers)
		List<Person> otherHouseholdMembers = personsAtThisAddress.stream()
				.filter(p -> childrenAtThisAddress.stream().anyMatch(c -> c.getLastName().equals(p.getLastName())))
				.filter(p -> !medicalRecordRepository.isChild(p)).collect(Collectors.toList());

		// convert children to DTO
		List<PersonDto> childrenDto = childrenAtThisAddress.stream().map(p -> {
			try {
				MedicalRecord m = medicalRecordRepository.getMedicalRecord(p);
				return dtoService.buildPersonDto(p, m, DtoConstants.UrlChildAlertChild);
			} catch (MedicalRecordNotFoundException e) {
				return dtoService.buildPersonDto(p, DtoConstants.UrlChildAlertChild); // ce cas ne peut pas arriver car
																						// les children ont forcément un
																						// medical recod
			}
		}).collect(Collectors.toList());

		// convert otherHouseholdMembers to DTO
		List<PersonDto> otherHouseholdMembersDto = otherHouseholdMembers.stream()
				.map(p -> dtoService.buildPersonDto(p, DtoConstants.UrlChildAlertAdult)).collect(Collectors.toList());

		// create the final DTO object that will be returned by URL Controller
		UrlChildAlertDto childAlertDto = new UrlChildAlertDto();
		childAlertDto.setChildren(childrenDto);
		childAlertDto.setOtherHouseHoldMembers(otherHouseholdMembersDto);

		return childAlertDto;

	}

	/**
	 * logic of "/fire" URL
	 * 
	 * @param address
	 * @return {@link UrlFireDto}
	 * @throws FirestationNotFoundException
	 * @throws AddressNotFoundException
	 */
	public UrlFireDto urlFire(String address) throws FirestationNotFoundException, AddressNotFoundException {

		// get the firestation concerned by this address
		Integer stationNumber = firestationRepository.getFirestationNumber(address);

		// get perrsons living at this addess
		List<Person> persons = personRepository.getPersonsByAddress(address);

		// convert persons to List of PersonDto objects
		List<PersonDto> personsFireDto = persons.stream().map(p -> this.convertPersonToUrlFireDto(p))
				.collect(Collectors.toList());

		// create the final DTO object that will be returned by URL Controller
		UrlFireDto fireDto = new UrlFireDto();
		fireDto.setFirestationNumber(stationNumber);
		fireDto.setPersons(personsFireDto);

		return fireDto;

	}

	/**
	 * logic of "/phoneAlert" URL
	 * 
	 * @param firestationNumber
	 * @return {@link UrlPhoneAlertDto}
	 * @throws FirestationNotFoundException
	 */
	public UrlPhoneAlertDto urlPhoneAlert(Integer firestationNumber) throws FirestationNotFoundException {

		// get Persons covered by the Firestation
		List<String> addresses = firestationRepository.getFirestationAddresses(firestationNumber);
		List<Person> persons = personRepository.getPersonsByAddresses(addresses);

		// extract phone numbers (use Set to avoid duplicates)
		Set<String> phoneNumbers = persons.stream().map(p -> p.getPhone()).collect(Collectors.toSet());

		// create the final DTO object that will be returned by URL Controller
		UrlPhoneAlertDto urlFirestationDto = new UrlPhoneAlertDto();
		urlFirestationDto.setPhoneNumbers(phoneNumbers);

		return urlFirestationDto;
	}

	/**
	 * logic of "/flood/stations" URL
	 * 
	 * @param stations
	 * @return {@link UrlFloodStationsDto}
	 * @throws FirestationNotFoundException
	 */
	public UrlFloodStationsDto urlFloodStations(List<Integer> stations) throws FirestationNotFoundException {

		// get the addresses covered by the Firestations
		List<String> addresses = new ArrayList<>();

		for (Integer station : stations) {
			addresses.addAll(firestationRepository.getFirestationAddresses(station));
		}

		// create a list of addresses with their inhabitants
		// Each address is converted to a UrlFloodStationsAddressDto that contains the
		// address and a list of PersonDto (the inhabitants)
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

		// create the final DTO object that will be returned by URL Controller
		UrlFloodStationsDto floodDto = new UrlFloodStationsDto();
		floodDto.setAddresses(addressesDto);

		return floodDto;

	}

	/**
	 * logic of "/personInfo" URL
	 * 
	 * @param firstName
	 * @param lastName
	 * @return {@link UrlPersonInfoDto}
	 * @throws PersonNotFoundException
	 */
	public UrlPersonInfoDto urlPersonInfo(String firstName, String lastName) throws PersonNotFoundException {

		// get Persons Data ("Si plusieurs personnes portent le même nom, elles doivent
		// toutes apparaître")
		List<Person> persons = personRepository.getPersonsByName(firstName, lastName);

		// convert them to list of DTO
		List<PersonDto> personsDto = persons.stream().map(p -> convertPersonToUrlPersonInfoItemDTO(p))
				.collect(Collectors.toList());

		// create the final DTO object that will be returned by URL Controller
		UrlPersonInfoDto urlPersonInfoDto = new UrlPersonInfoDto();
		urlPersonInfoDto.setPersons(personsDto);

		return urlPersonInfoDto;
	}

	/**
	 * logic of "/communityEmail" URL
	 * 
	 * @param city
	 * @return {@link UrlCommunityEmailDto}
	 * @throws CityNotFoundException
	 */
	public UrlCommunityEmailDto urlCommunityEmail(String city) throws CityNotFoundException {
		// get emails by City, convert them into DTO and return it to Controller
		UrlCommunityEmailDto communityDto = new UrlCommunityEmailDto();
		communityDto.setEmails(personRepository.getEmailsByCity(city));
		return communityDto;
	}

	// utils

	/**
	 * convert a {@link Person} to {@link PersonDto} for "/personInfo" URL
	 * 
	 * @param person
	 * @return {@link PersonDto}
	 */
	private PersonDto convertPersonToUrlPersonInfoItemDTO(Person person) {
		try {
			return dtoService.buildPersonDto(person, medicalRecordRepository.getMedicalRecord(person),
					DtoConstants.UrlPersonInfo);
		} catch (MedicalRecordNotFoundException e) {
			return dtoService.buildPersonDto(person, DtoConstants.UrlPersonInfo);
		}
	}

	/**
	 * convert a {@link Person} to {@link PersonDto} for "/fire" URL
	 * 
	 * @param person
	 * @return {@link PersonDto}
	 */
	private PersonDto convertPersonToUrlFireDto(Person person) {
		try {
			return dtoService.buildPersonDto(person, medicalRecordRepository.getMedicalRecord(person),
					DtoConstants.UrlFirePerson);
		} catch (MedicalRecordNotFoundException e) {
			return dtoService.buildPersonDto(person, DtoConstants.UrlFirePerson);
		}
	}

}
