package net.safety.alerts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.model.Person;
import net.safety.alerts.service.PersonService;

@Controller
public class PersonController {
	
	@Autowired
	private PersonService personService;
	
	// create / update
	public Person savePerson(Person p) {
		return personService.save(p);
	}
	
	// read
	public List<Person> getPersonsByName(String firstName, String lastName) {
		return personService.getByName(firstName, lastName);
	}
		
	// delete
	public void deletePerson(String firstName, String lastName) throws PersonNotFoundException {
		personService.delete(firstName, lastName);
	}
	

	
}
