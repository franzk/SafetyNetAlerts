package net.safety.alerts.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import net.safety.alerts.dto.ChildDto;
import net.safety.alerts.dto.UrlFirePersonDto;
import net.safety.alerts.dto.UrlPersonInfoPersonDto;
import net.safety.alerts.dto.PersonDto;
import net.safety.alerts.dto.PersonNameDto;
import net.safety.alerts.model.Person;

@Service
public class DtoService {

	private ModelMapper mapper = new ModelMapper();

	public ChildDto convertPersonToChildDto(Person person, Integer age) {
		ChildDto childDto = new ChildDto();
		childDto.setFirstName(person.getFirstName());
		childDto.setLastName(person.getLastName());
		childDto.setAge(age);
		return childDto;
	}

	public PersonDto convertPersonToPersonDto(Person person) {
		return mapper.map(person, PersonDto.class);
	}

	public PersonNameDto convertPersonToPersonNameDto(Person person) {
		return mapper.map(person, PersonNameDto.class);
	}

	public UrlFirePersonDto buildUrlFirePersonDto(Person person, Integer age, List<String> medications, List<String> allergies) {
		UrlFirePersonDto personFireDto = new UrlFirePersonDto();
		personFireDto.setFirstName(person.getFirstName());
		personFireDto.setLastName(person.getLastName());
		personFireDto.setPhone(person.getPhone());
		personFireDto.setAge(age);
		personFireDto.setMedications(medications);
		personFireDto.setAllergies(allergies);
		return personFireDto;
	}

	public UrlPersonInfoPersonDto buildUrlPersonInfoPersonDTO(Person person, List<String> medications, List<String> allergies) {
		UrlPersonInfoPersonDto personDto = new UrlPersonInfoPersonDto();
		personDto.setFirstName(person.getFirstName());
		personDto.setLastName(person.getLastName());
		personDto.setEmail(person.getEmail());
		personDto.setMedications(medications);
		personDto.setAllergies(allergies);
		return personDto;
	}
	
}
