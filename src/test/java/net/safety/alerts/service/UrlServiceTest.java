package net.safety.alerts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import net.safety.alerts.dto.UrlChildAlertDto;
import net.safety.alerts.dto.UrlCommunityEmailDto;
import net.safety.alerts.dto.UrlFireDto;
import net.safety.alerts.dto.UrlFirestationCoverageDto;
import net.safety.alerts.dto.UrlFloodStationsDto;
import net.safety.alerts.dto.UrlPersonInfoDto;
import net.safety.alerts.dto.UrlPhoneAlertDto;
import net.safety.alerts.exceptions.AddressNotFoundException;
import net.safety.alerts.exceptions.CityNotFoundException;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.model.Firestation;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.model.Person;
import net.safety.alerts.repository.FirestationRepository;
import net.safety.alerts.repository.MedicalRecordRepository;
import net.safety.alerts.repository.PersonRepository;
import net.safety.alerts.utils.BuildFirestationTestData;
import net.safety.alerts.utils.BuildMedicalRecordTestData;
import net.safety.alerts.utils.BuildPersonTestData;

public class UrlServiceTest {

	private UrlService urlServiceUnderTest = new UrlService();

	private PersonRepository personRepository;

	private FirestationRepository firestationRepository;

	private MedicalRecordRepository medicalRecordRepository;

	private DtoService dtoService;

	private BuildPersonTestData personTestData = new BuildPersonTestData();
	private BuildFirestationTestData firestationTestData = new BuildFirestationTestData();
	private BuildMedicalRecordTestData medicalRecordTestData = new BuildMedicalRecordTestData();

	@BeforeEach
	public void reset() {

		personRepository = new PersonRepository();
		ReflectionTestUtils.setField(urlServiceUnderTest, "personRepository", personRepository);

		firestationRepository = new FirestationRepository();
		ReflectionTestUtils.setField(urlServiceUnderTest, "firestationRepository", firestationRepository);

		medicalRecordRepository = new MedicalRecordRepository();
		ReflectionTestUtils.setField(urlServiceUnderTest, "medicalRecordRepository", medicalRecordRepository);

		dtoService = new DtoService();
		ReflectionTestUtils.setField(urlServiceUnderTest, "dtoService", dtoService);
	}

	@Test
	public void urlFirestationCoverageTest() {
		// Arrange
		Firestation testFirestation = firestationTestData.getFirestation();
		urlServiceUnderTest.firestationRepository.addFirestation(testFirestation);

		Person testPerson = personTestData.getPerson();
		testPerson.setAddress(testFirestation.getAddress());
		urlServiceUnderTest.personRepository.addPerson(testPerson);

		MedicalRecord testMedicalRecord = medicalRecordTestData.getMedicalRecord();
		testMedicalRecord.setFirstName(testPerson.getFirstName());
		testMedicalRecord.setLastName(testPerson.getLastName());
		testMedicalRecord.setBirthdate(LocalDate.now().minusYears(20));
		urlServiceUnderTest.medicalRecordRepository.addMedicalRecord(testMedicalRecord);

		Integer testStationNumber = testFirestation.getStation();

		// Act
		UrlFirestationCoverageDto result = new UrlFirestationCoverageDto();
		try {
			result = urlServiceUnderTest.urlFirestationCoverage(testStationNumber);
		} catch (FirestationNotFoundException e) {
			fail("urlFirestationCoverageTest threw an exception");
		}

		// Arrange
		assertThat(result.getAdultsCount()).isEqualTo(1);
		assertThat(result.getChildrenCount()).isEqualTo(0);
		assertThat(result.getPersons().size()).isEqualTo(1);
		assertThat(result.getPersons().get(0).getFirstName()).isEqualTo(testPerson.getFirstName());
		assertThat(result.getPersons().get(0).getLastName()).isEqualTo(testPerson.getLastName());
	}

	@Test
	public void urlChildAlertTest() {
		// Arrange
		Person child = personTestData.getPerson();
		personRepository.addPerson(child);
		MedicalRecord childMedicalRecord = medicalRecordTestData.getMedicalRecord();
		childMedicalRecord.setFirstName(child.getFirstName());
		childMedicalRecord.setLastName(child.getLastName());
		childMedicalRecord.setBirthdate(LocalDate.now().minusYears(3));
		medicalRecordRepository.addMedicalRecord(childMedicalRecord);

		String testAddress = child.getAddress();

		Person adult = personTestData.getPerson("firstName", child.getLastName(), testAddress, "city");
		personRepository.addPerson(adult);
		MedicalRecord adultMedicalRecord = medicalRecordTestData.getMedicalRecord();
		adultMedicalRecord.setFirstName(adult.getFirstName());
		adultMedicalRecord.setLastName(adult.getLastName());
		adultMedicalRecord.setBirthdate(LocalDate.now().minusYears(40));
		medicalRecordRepository.addMedicalRecord(adultMedicalRecord);

		Person personWithoutMedicalRecord = personTestData.getPerson("person witout medical record",
				child.getLastName(), testAddress, "");
		personRepository.addPerson(personWithoutMedicalRecord);

		// Act
		UrlChildAlertDto result = new UrlChildAlertDto();
		try {
			result = urlServiceUnderTest.urlChildAlert(testAddress);
		} catch (AddressNotFoundException e) {
			fail("urlChildAlertTest threw an exception");
		}

		// Arrange
		assertThat(result.getChildren().size()).isEqualTo(1);
		assertThat(result.getChildren().get(0).getFirstName()).isEqualTo(child.getFirstName());
		assertThat(result.getChildren().get(0).getLastName()).isEqualTo(child.getLastName());
		assertThat(result.getOtherHouseHoldMembers().size()).isEqualTo(2);
		assertThat(result.getOtherHouseHoldMembers().get(0).getFirstName()).isEqualTo(adult.getFirstName());
		assertThat(result.getOtherHouseHoldMembers().get(0).getLastName()).isEqualTo(adult.getLastName());

	}

	@Test
	public void urlFireTest() {
		// Arrange
		Firestation testFirestation = firestationTestData.getFirestation();
		firestationRepository.addFirestation(testFirestation);

		String testAddress = testFirestation.getAddress();

		Person person1 = personTestData.getPerson();
		person1.setAddress(testAddress);
		personRepository.addPerson(person1);

		Person person2 = personTestData.getPerson("firstName", "lastName", testAddress, "city");
		personRepository.addPerson(person2);

		// Act
		UrlFireDto result = new UrlFireDto();
		try {
			result = urlServiceUnderTest.urlFire(testAddress);
		} catch (Exception e) {
			fail("urlFireTest threw an exception");
		}

		// Assert
		assertThat(result.getFirestationNumber()).isEqualTo(testFirestation.getStation());
		assertThat(result.getPersons().size()).isEqualTo(2);
		assertThat(result.getPersons().get(0).getFirstName()).isEqualTo(person1.getFirstName());
		assertThat(result.getPersons().get(0).getLastName()).isEqualTo(person1.getLastName());
		assertThat(result.getPersons().get(1).getFirstName()).isEqualTo(person2.getFirstName());
		assertThat(result.getPersons().get(1).getLastName()).isEqualTo(person2.getLastName());
	}

	@Test
	public void urlPhoneAlertTest() {
		// Arrange
		Firestation testFirestation = firestationTestData.getFirestation();
		firestationRepository.addFirestation(testFirestation);

		Integer testStationNumber = testFirestation.getStation();
		String testAddress = testFirestation.getAddress();

		Person person1 = personTestData.getPerson();
		person1.setAddress(testAddress);
		person1.setPhone("phone1");
		personRepository.addPerson(person1);

		Person person2 = personTestData.getPerson("firstName", "lastName", testAddress, "city");
		person2.setPhone("phone2");
		personRepository.addPerson(person2);

		// Act
		UrlPhoneAlertDto result = new UrlPhoneAlertDto();
		try {
			result = urlServiceUnderTest.urlPhoneAlert(testStationNumber);
		} catch (FirestationNotFoundException e) {
			fail("urlPhoneAlertTest threw an exception");
		}

		// Assert
		assertThat(result.getPhoneNumbers().size()).isEqualTo(2);
		assertThat(result.getPhoneNumbers().contains(person1.getPhone())).isTrue();
		assertThat(result.getPhoneNumbers().contains(person2.getPhone())).isTrue();
	}

	@Test
	public void urlPersonInfoTest() {
		// Arrange
		Person testPerson = personTestData.getPerson();
		personRepository.addPerson(testPerson);

		Integer testAge = 20;

		MedicalRecord testMedicalRecord = medicalRecordTestData.getMedicalRecord();
		testMedicalRecord.setFirstName(testPerson.getFirstName());
		testMedicalRecord.setLastName(testPerson.getLastName());
		testMedicalRecord.setBirthdate(LocalDate.now().minusYears(testAge));
		medicalRecordRepository.addMedicalRecord(testMedicalRecord);

		// Act
		UrlPersonInfoDto result = new UrlPersonInfoDto();
		try {
			result = urlServiceUnderTest.urlPersonInfo(testPerson.getFirstName(), testPerson.getLastName());
		} catch (PersonNotFoundException e) {
			fail("urlPersonInfoTest threw an exception");
		}

		// Assert
		assertThat(result.getPersons().get(0).getFirstName()).isEqualTo(testPerson.getFirstName());
		assertThat(result.getPersons().get(0).getLastName()).isEqualTo(testPerson.getLastName());
		assertThat(result.getPersons().get(0).getAddress()).isEqualTo(testPerson.getAddress());
		assertThat(result.getPersons().get(0).getEmail()).isEqualTo(testPerson.getEmail());
		assertThat(result.getPersons().get(0).getAge()).isEqualTo(testAge);
		assertThat(result.getPersons().get(0).getAllergies()).isEqualTo(testMedicalRecord.getAllergies());
		assertThat(result.getPersons().get(0).getMedications()).isEqualTo(testMedicalRecord.getMedications());
	}

	@Test
	public void urlCommunityEmailTest() {
		// Arrange
		Person person1 = personTestData.getPerson();
		person1.setCity("testCity");
		person1.setEmail("1@aa.com");
		personRepository.addPerson(person1);

		Person person2 = personTestData.getPerson();
		person2.setCity("testCity");
		person2.setEmail("2@aa.com");
		personRepository.addPerson(person2);

		Person person3 = personTestData.getPerson();
		person3.setCity("testCity");
		person3.setEmail("3@aa.com");
		personRepository.addPerson(person3);

		// Act
		UrlCommunityEmailDto result = new UrlCommunityEmailDto();
		try {
			result = urlServiceUnderTest.urlCommunityEmail("testCity");
		} catch (CityNotFoundException e) {
			fail("urlCommunityEmailTest threw an exception");
		}

		// Assert
		assertThat(result.getEmails().size()).isEqualTo(3);
		assertThat(result.getEmails().get(0)).isEqualTo(person1.getEmail());
		assertThat(result.getEmails().get(1)).isEqualTo(person2.getEmail());
		assertThat(result.getEmails().get(2)).isEqualTo(person3.getEmail());
	}

	@Test
	public void urlFloodStationsTest() {
		// Arrange
		Firestation firestation1 = new Firestation();
		firestation1.setStation(1);
		firestation1.setAddress("address 1");
		firestationRepository.addFirestation(firestation1);

		Firestation firestation2 = firestationTestData.getFirestation();
		firestation2.setStation(1);
		firestation2.setAddress("address 2");
		firestationRepository.addFirestation(firestation2);

		Firestation firestation3 = firestationTestData.getFirestation();
		firestation3.setStation(2);
		firestation3.setAddress("address 3");
		firestationRepository.addFirestation(firestation3);

		Person inhabitant1 = personTestData.getPerson();
		inhabitant1.setAddress(firestation2.getAddress());
		personRepository.addPerson(inhabitant1);
		MedicalRecord medicalRecord1 = medicalRecordTestData.getMedicalRecord();
		medicalRecord1.setFirstName(inhabitant1.getFirstName());
		medicalRecord1.setLastName(inhabitant1.getLastName());
		medicalRecordRepository.addMedicalRecord(medicalRecord1);
		

		Person inhabitant2 = personTestData.getPerson();
		inhabitant2.setAddress(firestation2.getAddress());
		personRepository.addPerson(inhabitant2);

		// Act
		List<Integer> listStationNumber = List.of(new Integer[] { 1, 2 });
		UrlFloodStationsDto result = new UrlFloodStationsDto();
		try {
			result = urlServiceUnderTest.urlFloodStations(listStationNumber);
		} catch (FirestationNotFoundException e) {
			fail("urlFloodStationsTest");
		}

		// Assert
		assertThat(result.getAddresses().size()).isEqualTo(3);
		assertThat(result.getAddresses().get(0).getAddress()).isEqualTo(firestation1.getAddress());
		assertThat(result.getAddresses().get(2).getInhabitants().size()).isEqualTo(0);
		assertThat(result.getAddresses().get(1).getInhabitants().size()).isEqualTo(2);
		assertThat(result.getAddresses().get(1).getInhabitants().get(0).getFirstName())
				.isEqualTo(inhabitant1.getFirstName());
		assertThat(result.getAddresses().get(1).getInhabitants().get(0).getLastName())
				.isEqualTo(inhabitant1.getLastName());
	}

	@Test
	public void convertPersonToUrlPersonInfoItemDTOExcetpion() {
		// Arrange
		Person testPerson = personTestData.getPerson();
		personRepository.addPerson(testPerson);

		// Act
		UrlPersonInfoDto result = new UrlPersonInfoDto();
		try {
			result = urlServiceUnderTest.urlPersonInfo(testPerson.getFirstName(), testPerson.getLastName());
		} catch (PersonNotFoundException e) {
			fail("convertPersonToUrlPersonInfoItemDTOExcetpion");
		}
		
		// Assert
		assertThat(result.getPersons().size()).isEqualTo(1);
		assertThat(result.getPersons().get(0).getAge()).isNull();
	
	}
	
	@Test
	public void convertPersonToUrlFireDtoTest() {
		
	}
	
	
}