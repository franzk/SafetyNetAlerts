package net.safety.alerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
public class EndpointPersonController {
	
	@Autowired
	private PersonService personService;
	
	// create
	@PostMapping("")
	public ResponseEntity<Person> addPerson(@RequestBody Person p) {
		return new ResponseEntity<>(personService.add(p), HttpStatus.OK);
	}
	
	// read
	@GetMapping("")
	public ResponseEntity<Person> getPersonByName(@RequestParam String firstName, @RequestParam String lastName) throws PersonNotFoundException {
		return new ResponseEntity<>(personService.getPersonByName(firstName, lastName), HttpStatus.OK);
	}
	
	
	// update
	@PutMapping("")
	public ResponseEntity<Person> updatePerson(@RequestBody Person p) throws PersonNotFoundException {
		return new ResponseEntity<>(personService.update(p), HttpStatus.OK);
	}
		
	// delete
	@DeleteMapping("")
	public ResponseEntity<String> deletePerson(@RequestParam String firstName, @RequestParam String lastName) throws PersonNotFoundException {
		personService.delete(firstName, lastName);
		return ResponseEntity.status(HttpStatus.OK).body("The Person has been succesfully deleted");
	}
	

	
}
