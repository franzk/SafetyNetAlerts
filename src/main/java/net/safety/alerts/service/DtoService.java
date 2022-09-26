package net.safety.alerts.service;

import java.util.List;

import org.springframework.stereotype.Service;

import net.safety.alerts.dto.PersonDto;
import net.safety.alerts.model.Person;
import net.safety.alerts.utils.DtoConstants.PersonField;

@Service
public class DtoService {

	// construction d'un DTO Person
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
	
	public PersonDto buildPersonDto(Person person, Integer age, PersonField[] fields) {
		PersonDto personDto = buildPersonDto(person, fields);
		personDto.setAge(age);
		return personDto;
	}

	public PersonDto buildPersonDto(Person person, List<String> medications, List<String> allergies, PersonField[] fields) {
		PersonDto personDto = buildPersonDto(person, fields);
		personDto.setMedications(medications);
		personDto.setAllergies(allergies);
		return personDto;
	}	
	
	public PersonDto buildPersonDto(Person person, Integer age, List<String> medications, List<String> allergies, PersonField[] fields) {
		PersonDto personDto = buildPersonDto(person, fields);
		personDto.setAge(age);
		personDto.setMedications(medications);
		personDto.setAllergies(allergies);
		return personDto;
	}	
	
}
