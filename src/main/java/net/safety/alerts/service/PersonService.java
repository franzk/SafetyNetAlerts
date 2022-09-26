package net.safety.alerts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.model.Person;
import net.safety.alerts.repository.PersonRepository;

@Service
public class PersonService {

	@Autowired
	private PersonRepository personRepository;

	// read
	public List<Person> getPersonsWithSameName(String firstName, String lastName) throws PersonNotFoundException {
		return personRepository.getPersonsByName(firstName, lastName);
	}

	public Person getPersonByName(String firstName, String lastName) throws PersonNotFoundException {
		return personRepository.getPersonByName(firstName, lastName);

	}

	// create
	public Person add(Person p) {
		return personRepository.addPerson(p);
	}

	// update
	public Person update(Person p) throws PersonNotFoundException {
		return personRepository.updatePerson(p);
	}

	// delete
	public void delete(String firstName, String lastName) throws PersonNotFoundException {
		personRepository.deletePersonByName(firstName, lastName);
	}



}
