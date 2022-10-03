package net.safety.alerts.service;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Service;

import net.safety.alerts.dto.PersonDto;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.model.Person;
import net.safety.alerts.utils.DtoConstants.PersonField;

/**
 * Convert a Person in a DTO object. Used to create Json ouput files.
 * 
 * @author FranzKa
 *
 */
@Service
public class DtoService {

	/**
	 * Convert a {@link Person} in a DTO object
	 * 
	 * @param person : Person to convert
	 * @param fields : attributes that should appear in output Json File
	 * @return {@link PersonDto}
	 */
	public PersonDto buildPersonDto(Person person, PersonField[] fields) {
		PersonDto personDto = new PersonDto();
		for (PersonField field : fields) {
			switch (field) {
			case FIRST_NAME:
				personDto.setFirstName(person.getFirstName());
				break;
			case LAST_NAME:
				personDto.setLastName(person.getLastName());
				break;
			case ADDRESS:
				personDto.setAddress(person.getAddress());
				break;
			case CITY:
				personDto.setCity(person.getCity());
				break;
			case ZIP:
				personDto.setZip(person.getZip());
				break;
			case PHONE:
				personDto.setPhone(person.getPhone());
				break;
			case EMAIL:
				personDto.setEmail(person.getEmail());
				break;
			default:
				break;

			}
		}
		return personDto;
	}

	/**
	 * Convert a {@link Person} in a DTO object with {@link MedicalRecord} related
	 * data
	 * 
	 * @param person        : Person to convert
	 * @param medicalRecord : the MedicalRecord from this Person
	 * @param fields        : attributes that should appear in output Json File
	 * @return {@link PersonDto}
	 */
	public PersonDto buildPersonDto(Person person, MedicalRecord medicalRecord, PersonField[] fields) {
		PersonDto personDto = buildPersonDto(person, fields);
		for (PersonField field : fields) {
			switch (field) {
			case AGE:
				int age = Period.between(medicalRecord.getBirthdate(), LocalDate.now()).getYears();
				personDto.setAge(age);
				break;
			case MEDICATIONS:
				personDto.setMedications(medicalRecord.getMedications());
				break;
			case ALLERGIES:
				personDto.setAllergies(medicalRecord.getAllergies());
				break;
			default:
				break;
			}
		}
		return personDto;
	}

}
