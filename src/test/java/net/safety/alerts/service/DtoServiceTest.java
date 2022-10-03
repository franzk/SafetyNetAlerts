package net.safety.alerts.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import net.safety.alerts.dto.PersonDto;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.model.Person;
import net.safety.alerts.utils.DtoConstants.PersonField;
import net.safety.alerts.utils.PersonTestData;

@ExtendWith(MockitoExtension.class)
public class DtoServiceTest {
	
	@InjectMocks
	private DtoService serviceUnderTest;

	private PersonField[] testFields = new PersonField[] { PersonField.FIRST_NAME, PersonField.LAST_NAME,
			PersonField.ADDRESS, PersonField.CITY, PersonField.ZIP, PersonField.PHONE, PersonField.EMAIL };

	private PersonField[] testPartialFields = new PersonField[] { PersonField.FIRST_NAME, PersonField.LAST_NAME,
			PersonField.ADDRESS, PersonField.CITY };

	private PersonField[] testFieldsWithMedicalRecord = new PersonField[] { PersonField.FIRST_NAME,
			PersonField.LAST_NAME, PersonField.AGE, PersonField.MEDICATIONS, PersonField.ALLERGIES };

	@Test
	public void buildPersonDtoTest() {
		// Arrange
		Person testPerson = PersonTestData.buildPerson();

		// Act
		PersonDto personDto = serviceUnderTest.buildPersonDto(testPerson, testFields);

		// Assert
		assertThat(personDto.getFirstName()).isEqualTo(testPerson.getFirstName());
		assertThat(personDto.getLastName()).isEqualTo(testPerson.getLastName());
		assertThat(personDto.getAddress()).isEqualTo(testPerson.getAddress());
		assertThat(personDto.getCity()).isEqualTo(testPerson.getCity());
		assertThat(personDto.getZip()).isEqualTo(testPerson.getZip());
		assertThat(personDto.getPhone()).isEqualTo(testPerson.getPhone());
		assertThat(personDto.getEmail()).isEqualTo(testPerson.getEmail());

	}

	@Test
	public void buildPartialPersonDtoTest() {
		// Arrange
		Person testPerson = PersonTestData.buildPerson();

		// Act
		PersonDto personDto = serviceUnderTest.buildPersonDto(testPerson, testPartialFields);

		// Assert
		assertThat(personDto.getFirstName()).isEqualTo(testPerson.getFirstName());
		assertThat(personDto.getLastName()).isEqualTo(testPerson.getLastName());
		assertThat(personDto.getAddress()).isEqualTo(testPerson.getAddress());
		assertThat(personDto.getCity()).isEqualTo(testPerson.getCity());
		assertThat(personDto.getZip()).isNull();
		assertThat(personDto.getPhone()).isNull();
		assertThat(personDto.getEmail()).isNull();

	}

	@Test
	public void buildPersonDtoWithMedicalRecordTest() {
		// Arrange
		Person testPerson = PersonTestData.buildPerson();

		LocalDate birthdate = LocalDate.now().minusYears(18);
		List<String> allergies = List.of(new String[] { "Pollen", "Arachides" });
		List<String> medications = List.of(new String[] { "Doliprane", "Vogalen" });

		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setFirstName(testPerson.getFirstName());
		medicalRecord.setLastName(testPerson.getLastName());
		medicalRecord.setBirthdate(birthdate);
		medicalRecord.setAllergies(allergies);
		medicalRecord.setMedications(medications);

		// Act
		PersonDto personDto = serviceUnderTest.buildPersonDto(testPerson, medicalRecord, testFieldsWithMedicalRecord);

		// Assert
		assertThat(personDto.getFirstName()).isEqualTo(testPerson.getFirstName());
		assertThat(personDto.getLastName()).isEqualTo(testPerson.getLastName());
		assertThat(personDto.getAge()).isEqualTo(18);
		assertThat(personDto.getAllergies()).isEqualTo(allergies);
		assertThat(personDto.getMedications()).isEqualTo(medications);

	}

}
