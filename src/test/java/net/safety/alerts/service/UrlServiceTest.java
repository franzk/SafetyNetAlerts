package net.safety.alerts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
import net.safety.alerts.exceptions.MedicalRecordNotFoundException;
import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.model.Person;
import net.safety.alerts.repository.FirestationRepository;
import net.safety.alerts.repository.MedicalRecordRepository;
import net.safety.alerts.repository.PersonRepository;
import net.safety.alerts.utils.MedicalRecordTestData;
import net.safety.alerts.utils.PersonTestData;

@ExtendWith(MockitoExtension.class)
public class UrlServiceTest {

	@InjectMocks
	private UrlService urlServiceUnderTest = new UrlService();

	@Mock
	private PersonRepository personRepository;

	@Mock
	private FirestationRepository firestationRepository;

	@Mock
	private MedicalRecordRepository medicalRecordRepository;

	@Test
	public void urlFirestationCoverageTest() throws FirestationNotFoundException {

		// Arrange
		Person p1 = PersonTestData.buildPerson("person", "one", "child", "");
		Person p2 = PersonTestData.buildPerson("person", "two", "adult", "");
		Person p3 = PersonTestData.buildPerson("person", "three", "unknown", "");
		List<Person> persons = List.of(p1, p2, p3);

		when(firestationRepository.getFirestationAddresses(any())).thenReturn(new ArrayList<>());
		when(personRepository.getPersonsByAddresses(any())).thenReturn(persons);

		// child
		when(medicalRecordRepository.isAdult(persons.get(0))).thenReturn(true);
		when(medicalRecordRepository.isChild(persons.get(0))).thenReturn(false);

		// adult
		when(medicalRecordRepository.isAdult(persons.get(1))).thenReturn(false);
		when(medicalRecordRepository.isChild(persons.get(1))).thenReturn(true);

		// unknown
		when(medicalRecordRepository.isAdult(persons.get(2))).thenReturn(false);
		when(medicalRecordRepository.isChild(persons.get(2))).thenReturn(false);

		// Act
		UrlFirestationCoverageDto result = urlServiceUnderTest.urlFirestationCoverage(0);

		// Assert
		assertThat(result.getAdultsCount()).isEqualTo(1);
		assertThat(result.getChildrenCount()).isEqualTo(1);
		assertThat(result.getPersons().size()).isEqualTo(3);
		assertThat(result.getPersons().get(0).getFirstName()).isEqualTo(p1.getFirstName());
		assertThat(result.getPersons().get(0).getLastName()).isEqualTo(p1.getLastName());
	}

	@Test
	public void urlChildAlertTest() throws AddressNotFoundException, MedicalRecordNotFoundException {
		// Arrange
		Person child = PersonTestData.buildPerson("child", "name", "address", "");
		Person adult1 = PersonTestData.buildPerson("adult1", "name", "address", "");
		Person adult2 = PersonTestData.buildPerson("adult2", "name", "address", "");
		Person adult3 = PersonTestData.buildPerson("adult3", "othername", "other address", "");
		List<Person> persons = List.of(child, adult1, adult2, adult3);
		MedicalRecord childMedicalRecord = MedicalRecordTestData.buildChildMedicalRecord(child.getFirstName(),
				child.getLastName());

		when(personRepository.getPersonsByAddress(any())).thenReturn(persons);

		when(medicalRecordRepository.isChild(child)).thenReturn(true);
		when(medicalRecordRepository.isChild(adult1)).thenReturn(false);
		when(medicalRecordRepository.isChild(adult2)).thenReturn(false);
		when(medicalRecordRepository.isChild(adult3)).thenReturn(false);

		when(medicalRecordRepository.getMedicalRecord(child)).thenReturn(childMedicalRecord);

		// Act
		UrlChildAlertDto result = urlServiceUnderTest.urlChildAlert("");

		// Assert
		assertThat(result.getChildren().size()).isEqualTo(1);
		assertThat(result.getChildren().get(0).getFirstName()).isEqualTo(child.getFirstName());
		assertThat(result.getChildren().get(0).getLastName()).isEqualTo(child.getLastName());
		assertThat(result.getOtherHouseHoldMembers().size()).isEqualTo(2);
		assertThat(result.getOtherHouseHoldMembers().get(0).getFirstName()).isEqualTo(adult1.getFirstName());
		assertThat(result.getOtherHouseHoldMembers().get(0).getLastName()).isEqualTo(adult1.getLastName());

	}

	@Test
	public void urlFireTest()
			throws FirestationNotFoundException, AddressNotFoundException, MedicalRecordNotFoundException {
		// Arrange
		Integer testStationNumber = 24;

		Person p1 = PersonTestData.buildPerson();
		Person p2 = PersonTestData.buildPerson();
		List<Person> persons = List.of(p1, p2);

		MedicalRecord m1 = MedicalRecordTestData.buildChildMedicalRecord(p1.getFirstName(), p2.getLastName());
		MedicalRecord m2 = MedicalRecordTestData.buildAdultMedicalRecord(p1.getFirstName(), p2.getLastName());

		when(firestationRepository.getFirestationNumber(anyString())).thenReturn(testStationNumber);
		when(personRepository.getPersonsByAddress(any())).thenReturn(persons);

		when(medicalRecordRepository.getMedicalRecord(persons.get(0))).thenReturn(m1);
		when(medicalRecordRepository.getMedicalRecord(persons.get(1))).thenReturn(m2);

		// Act
		UrlFireDto result = urlServiceUnderTest.urlFire("");

		// Assert
		assertThat(result.getFirestationNumber()).isEqualTo(testStationNumber);
		assertThat(result.getPersons().size()).isEqualTo(2);
		assertThat(result.getPersons().get(0).getFirstName()).isEqualTo(p1.getFirstName());
		assertThat(result.getPersons().get(0).getLastName()).isEqualTo(p1.getLastName());
		assertThat(result.getPersons().get(1).getFirstName()).isEqualTo(p2.getFirstName());
		assertThat(result.getPersons().get(1).getLastName()).isEqualTo(p2.getLastName());
	}

	@Test
	public void urlPhoneAlertTest() throws FirestationNotFoundException {

		// Arrange
		Person p1 = PersonTestData.buildPerson();
		p1.setPhone("phone1");
		Person p2 = PersonTestData.buildPerson();
		p2.setPhone("phone2");
		Person p3 = PersonTestData.buildPerson();
		p3.setPhone("phone3");
		List<Person> persons = List.of(p1, p2, p3);

		when(firestationRepository.getFirestationAddresses(any())).thenReturn(new ArrayList<>());
		when(personRepository.getPersonsByAddresses(any())).thenReturn(persons);

		// Act
		UrlPhoneAlertDto result = urlServiceUnderTest.urlPhoneAlert(0);

		// Assert
		assertThat(result.getPhoneNumbers().size()).isEqualTo(3);
		assertThat(result.getPhoneNumbers()).contains(p1.getPhone());
		assertThat(result.getPhoneNumbers()).contains(p2.getPhone());
	}

	@Test
	public void urlPersonInfoTest() throws PersonNotFoundException, MedicalRecordNotFoundException {
		// Arrange
		Person p1 = PersonTestData.buildPerson();
		Person p2 = PersonTestData.buildPerson();
		p2.setFirstName("p");
		p2.setLastName("2");

		Integer testAge = 20;

		MedicalRecord testMedicalRecord = MedicalRecordTestData.buildMedicalRecord();
		testMedicalRecord.setFirstName(p1.getFirstName());
		testMedicalRecord.setLastName(p2.getLastName());
		testMedicalRecord.setBirthdate(LocalDate.now().minusYears(testAge));

		when(personRepository.getPersonsByName(any(), any())).thenReturn(List.of(p1, p2));
		when(medicalRecordRepository.getMedicalRecord(any())).thenReturn(testMedicalRecord);

		// Act
		UrlPersonInfoDto result = urlServiceUnderTest.urlPersonInfo("", "");

		// Assert
		assertThat(result.getPersons().get(0).getFirstName()).isEqualTo(p1.getFirstName());
		assertThat(result.getPersons().get(0).getLastName()).isEqualTo(p1.getLastName());
		assertThat(result.getPersons().get(0).getAddress()).isEqualTo(p1.getAddress());
		assertThat(result.getPersons().get(0).getEmail()).isEqualTo(p1.getEmail());
		assertThat(result.getPersons().get(0).getAge()).isEqualTo(testAge);
		assertThat(result.getPersons().get(0).getAllergies()).isEqualTo(testMedicalRecord.getAllergies());
		assertThat(result.getPersons().get(0).getMedications()).isEqualTo(testMedicalRecord.getMedications());
	}

	@Test
	public void urlCommunityEmailTest() throws CityNotFoundException {
		// Arrange

		when(personRepository.getEmailsByCity(any())).thenReturn(List.of("email1", "email2", "email3"));

		// Act
		UrlCommunityEmailDto result = urlServiceUnderTest.urlCommunityEmail("testCity");

		// Assert
		assertThat(result.getEmails().size()).isEqualTo(3);
		assertThat(result.getEmails().get(0)).isEqualTo("email1");
		assertThat(result.getEmails().get(1)).isEqualTo("email2");
		assertThat(result.getEmails().get(2)).isEqualTo("email3");
	}

	@Test
	public void urlFloodStationsTest()
			throws FirestationNotFoundException, AddressNotFoundException, MedicalRecordNotFoundException {
		// Arrange
		List<String> addresses = List.of("a1", "a2");
		Person p1 = PersonTestData.buildPerson("1", "1", "1", "1");
		Person p2 = PersonTestData.buildPerson("2", "2", "2", "2");
		Person p3 = PersonTestData.buildPerson("3", "3", "3", "3");
		List<Person> personsAtAdress1 = List.of(p1, p2);
		List<Person> personsAtAdress2 = List.of(p3);

		MedicalRecord medicalRecord = MedicalRecordTestData.buildMedicalRecord();

		when(firestationRepository.getFirestationAddresses(any())).thenReturn(addresses);

		when(personRepository.getPersonsByAddress(addresses.get(0))).thenReturn(personsAtAdress1);
		when(personRepository.getPersonsByAddress(addresses.get(1))).thenReturn(personsAtAdress2);

		when(medicalRecordRepository.getMedicalRecord(any())).thenReturn(medicalRecord);

		// Act
		UrlFloodStationsDto result = urlServiceUnderTest.urlFloodStations(List.of(1));

		// Assert
		assertThat(result.getAddresses().size()).isEqualTo(2);

		assertThat(result.getAddresses().get(0).getAddress()).isEqualTo(addresses.get(0));
		assertThat(result.getAddresses().get(0).getInhabitants().size()).isEqualTo(2);

		assertThat(result.getAddresses().get(1).getInhabitants().size()).isEqualTo(1);
		assertThat(result.getAddresses().get(1).getInhabitants().get(0).getFirstName())
				.isEqualTo(personsAtAdress2.get(0).getFirstName());
		assertThat(result.getAddresses().get(1).getInhabitants().get(0).getLastName())
				.isEqualTo(personsAtAdress2.get(0).getLastName());
	}


	@Test
	public void convertPersonToUrlPersonInfoItemDTOException()
			throws PersonNotFoundException, MedicalRecordNotFoundException {
		// Arrange
		Person p1 = PersonTestData.buildPerson("1", "1", "1", "1");
		Person p2 = PersonTestData.buildPerson("2", "2", "2", "2");
		Person p3 = PersonTestData.buildPerson("3", "3", "3", "3");
		List<Person> persons = List.of(p1, p2, p3);

		when(personRepository.getPersonsByName(any(), any())).thenReturn(persons);
		when(medicalRecordRepository.getMedicalRecord(any())).thenThrow(new MedicalRecordNotFoundException());

		// Act
		UrlPersonInfoDto result = urlServiceUnderTest.urlPersonInfo("", "");

		// Assert
		assertThat(result.getPersons().get(0).getMedications()).isNull();
	}

	@Test
	public void convertPersonToUrlFireDtoException()
			throws FirestationNotFoundException, AddressNotFoundException, MedicalRecordNotFoundException {
		// Arrange
		Person p1 = PersonTestData.buildPerson();
		List<Person> persons = List.of(p1);

		when(firestationRepository.getFirestationNumber(anyString())).thenReturn(24);
		when(personRepository.getPersonsByAddress(any())).thenReturn(persons);

		when(medicalRecordRepository.getMedicalRecord(any())).thenThrow(new MedicalRecordNotFoundException());

		// Act
		UrlFireDto result = urlServiceUnderTest.urlFire("");

		// Assert
		assertThat(result.getPersons().get(0).getAge()).isNull();

	}

	@Test
	public void urlChildAlertMedicalRecordNotFoundExceptionTest() throws AddressNotFoundException, MedicalRecordNotFoundException {
		// Arrange
		Person child = PersonTestData.buildPerson("child", "name", "address", "");
		List<Person> persons = List.of(child);

		when(personRepository.getPersonsByAddress(any())).thenReturn(persons);

		when(medicalRecordRepository.isChild(any())).thenReturn(true);

		when(medicalRecordRepository.getMedicalRecord(any())).thenThrow(new MedicalRecordNotFoundException());

		// Act
		UrlChildAlertDto result = urlServiceUnderTest.urlChildAlert("");

		// Assert
		assertThat(result.getChildren().size()).isEqualTo(1);
		assertThat(result.getChildren().get(0).getAge()).isNull();
	}

	
}