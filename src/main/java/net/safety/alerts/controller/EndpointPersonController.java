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

import lombok.extern.log4j.Log4j2;
import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.model.Person;
import net.safety.alerts.service.PersonService;

/**
 * Endpoint "/person".<br>
 * 
 * "Cet endpoint permettra d’effectuer les actions suivantes via Post/Put/Delete
 * avec HTTP :<br>
 * ● ajouter une nouvelle personne ; <br>
 * ● mettre à jour une personne existante (pour le moment, supposons que le
 * prénom et le nom de famille ne changent pas, mais que les autres champs
 * peuvent être modifiés) ;<br>
 * ● supprimer une personne (utilisez une combinaison de prénom et de nom comme
 * identificateur unique)"
 * 
 * @author FranzKa
 *
 */
@RestController
@RequestMapping("/person")
@Log4j2
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
		log.info("Person Controller POST Request start. Param person = " + person);
		ResponseEntity<Person> result = new ResponseEntity<>(personService.add(person), HttpStatus.OK);
		log.info("Person Controller POST Request result : " + result);
		return result;
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
		log.info("Person Controller GET Request start. Param firstName = " + firstName + " / lastName = " + lastName);
		ResponseEntity<Person> result = new ResponseEntity<>(personService.getPersonByName(firstName, lastName),
				HttpStatus.OK);
		log.info("Person Controller GET Request result : " + result);
		return result;
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
		log.info("Person Controller PUT Request start. Param person = " + person);
		ResponseEntity<Person> result = new ResponseEntity<>(personService.update(person), HttpStatus.OK);
		log.info("Person Controller PUT Request result : " + result);
		return result;
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
		log.info("Person Controller DELETE Request start. Param firstName = " + firstName + " / lastName = " + lastName);
		personService.delete(firstName, lastName);
		ResponseEntity<String> result = ResponseEntity.status(HttpStatus.OK)
				.body("The Person has been succesfully deleted");
		log.info("Person Controller DELETE Request result : " + result);
		return result;
	}

}
