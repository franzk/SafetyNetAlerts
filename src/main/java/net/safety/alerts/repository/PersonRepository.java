package net.safety.alerts.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.model.Person;

@Repository
public class PersonRepository {

	private List<Person> listPersons = new ArrayList<>();
	
	//create
	public Person addPerson(Person p) {
		listPersons.add(p);
		return p;
	}
	
	//read
	public List<Person> getAll() {
		return listPersons;
	}
	
	public List<Person> getPersonByName(String firstName, String lastName) {
		return null;
	}
	
	//update
	public Person updatePerson(Person p) throws PersonNotFoundException {
		List<Person> personsWithThisName = getPersonByName(p.getFirstName(), p.getLastName());
		if (personsWithThisName.size() > 0) {
			listPersons.set(listPersons.indexOf(personsWithThisName.get(0)), p);
			return p;
		}
		else {
			// generate ERROR
			throw new PersonNotFoundException();
		}
	}
	
	//delete
	public void deletePerson(Person p) throws PersonNotFoundException {
		if (!listPersons.remove(p)) {
			throw new PersonNotFoundException();
		}
	}
	
	
}
