package net.safety.alerts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import net.safety.alerts.model.Person;
import net.safety.alerts.repository.PersonRepository;
import net.safety.alerts.utils.JsonFileConstants;

/**
 * Import Json data into Person Repository
 * 
 * @author FranzKa
 *
 */
@Service
public class LoadPersonsService {

	@Autowired
	private PersonRepository personRepository;
	
	/**
	 * Import Json data into Person Repository
	 * 
	 * @param personsNode
	 */
	public void loadPersons(JsonNode personsNode) {

		for (JsonNode personNode : personsNode) {
			Person person = new Person();
			person.setFirstName(personNode.path(JsonFileConstants.person_firstName).asText());
			person.setLastName(personNode.path(JsonFileConstants.person_lastName).asText());
			person.setAddress(personNode.path(JsonFileConstants.person_address).asText());
			person.setCity(personNode.path(JsonFileConstants.person_city).asText());
			person.setZip(personNode.path(JsonFileConstants.person_zip).asText());
			person.setPhone(personNode.path(JsonFileConstants.person_phone).asText());
			person.setEmail(personNode.path(JsonFileConstants.person_email).asText());

			personRepository.addPerson(person);
		}

	}
}
