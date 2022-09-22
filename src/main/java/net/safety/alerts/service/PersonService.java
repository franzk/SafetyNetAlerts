package net.safety.alerts.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.safety.alerts.dto.ChildAlertDto;
import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.model.Person;
import net.safety.alerts.repository.PersonRepository;

@Service
public class PersonService {
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private JoinedDataService joinedDataService;

	//read
	public List<Person> getByName(String firstName, String lastName) {
		return personRepository.getPersonsByName(firstName, lastName);
	}
	
	
	// create / update
	public Person save(Person p) {
		
		try {
			personRepository.updatePerson(p);
		}
		catch (PersonNotFoundException e) {
			personRepository.addPerson(p);
		}
	
		return p;
	}
	
	// delete
	public void delete(String firstName, String lastName) throws PersonNotFoundException {
		personRepository.deletePersonByName(firstName, lastName);
	}

	// endpoints
	
	public ChildAlertDto childAlert(String address) {
		return joinedDataService.childAlert(address, this);
	}
	
	
	//utils
	public boolean isLastNamePresentInPersonList(String lastName, List<Person> listPerson) {
		Optional<Person> person = listPerson.stream().filter(p -> p.getLastName().equals(lastName)).findFirst();
		return person.isPresent();
	}


}
