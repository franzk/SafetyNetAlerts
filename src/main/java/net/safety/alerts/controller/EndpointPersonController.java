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

/**
 * Handle endpoints of URL "/persons". Cover all CRUD methods.
 * 
 * @author FranzKa
 *
 */
@RestController
@RequestMapping("/person")
public class EndpointPersonController {

	@Autowired
	private PersonService personService;

	/**
	 * POST method of URL "/person"
	 * 
	 * @param person
	 * @return ResponseEntity with Person creted and Http Status OK
	 */
	@PostMapping("")
	public ResponseEntity<Person> addPerson(@RequestBody Person person) {
		return new ResponseEntity<>(personService.add(person), HttpStatus.OK);
	}

	/**
	 * GET method of URL "/person"
	 * 
	 * @param firstName
	 * @param lastName
	 * @return ResponseEntity with Person result and Http Status OK
	 * @throws PersonNotFoundException
	 */
	@GetMapping("")
	public ResponseEntity<Person> getPersonByName(@RequestParam String firstName, @RequestParam String lastName)
			throws PersonNotFoundException {
		return new ResponseEntity<>(personService.getPersonByName(firstName, lastName), HttpStatus.OK);
	}

	/**
	 * UPDATE method of URL "/person"
	 * 
	 * @param person
	 * @return ResponseEntity with updated Person and Http Status OK
	 * @throws PersonNotFoundException
	 */
	@PutMapping("")
	public ResponseEntity<Person> updatePerson(@RequestBody Person person) throws PersonNotFoundException {
		return new ResponseEntity<>(personService.update(person), HttpStatus.OK);
	}

	/**
	 * DELETE method of URL "/person"
	 * 
	 * @param firstName
	 * @param lastName
	 * @return ResponseEntity with success message and Http Status OK
	 * @throws PersonNotFoundException
	 */
	@DeleteMapping("")
	public ResponseEntity<String> deletePerson(@RequestParam String firstName, @RequestParam String lastName)
			throws PersonNotFoundException {
		personService.delete(firstName, lastName);
		return ResponseEntity.status(HttpStatus.OK).body("The Person has been succesfully deleted");
	}

}
