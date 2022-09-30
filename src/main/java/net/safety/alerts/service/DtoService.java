package net.safety.alerts.service;

import org.springframework.stereotype.Service;

import net.safety.alerts.dto.PersonDto;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.model.Person;
import net.safety.alerts.utils.DtoConstants.PersonField;
import net.safety.alerts.utils.Utils;

@Service
public class DtoService {

	// construction d'un DTO Person
	public static PersonDto buildPersonDto(Person person, PersonField[] fields) {
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

	public static PersonDto buildPersonDto(Person person, MedicalRecord medicalRecord, PersonField[] fields) {
		PersonDto personDto = buildPersonDto(person, fields);
		for (PersonField field : fields) {
			switch (field) {
			case AGE:
				personDto.setAge(Utils.calculateAge(medicalRecord.getBirthdate()));
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
