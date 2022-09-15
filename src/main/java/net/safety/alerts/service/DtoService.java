package net.safety.alerts.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.safety.alerts.dto.ChildDto;
import net.safety.alerts.dto.PersonDto;
import net.safety.alerts.dto.PersonFireDto;
import net.safety.alerts.dto.PersonNameDto;
import net.safety.alerts.model.Person;

@Service
public class DtoService {

	@Autowired
	ModelMapper mapper;

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

	public PersonFireDto convertPersonToFireDto(Person person, Integer age, List<String> medications, List<String> allergies) {
		PersonFireDto personFireDto = new PersonFireDto();
		personFireDto.setFirstName(person.getFirstName());
		personFireDto.setLastName(person.getLastName());
		personFireDto.setPhone(person.getPhone());
		personFireDto.setAge(age);
		personFireDto.setMedications(medications.toArray(new String[0]));
		personFireDto.setAllergies(allergies.toArray(new String[0]));
		return personFireDto;
	}

}