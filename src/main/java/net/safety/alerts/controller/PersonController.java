package net.safety.alerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.model.Person;
import net.safety.alerts.service.PersonService;

@RestController
@RequestMapping("/person")
public class PersonController {
	
	@Autowired
	private PersonService personService;
	
	// create
	@PostMapping("")
	public Person addPerson(@RequestBody Person p) {
		return personService.add(p);
	}
	
	// créer un get pour une person
	
	// update
	@PutMapping("")
	public Person updatePerson(@RequestBody Person p) throws PersonNotFoundException {
		return personService.update(p);
	}
		
	// delete
	@DeleteMapping("")
	public void deletePerson(@RequestParam String firstName, @RequestParam String lastName) throws PersonNotFoundException {
		personService.delete(firstName, lastName);
	}
	

	
}
