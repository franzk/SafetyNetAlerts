package net.safety.alerts.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import net.safety.alerts.exceptions.AddressNotFoundException;
import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.model.Person;

@Repository
public class PersonRepository {

	private List<Person> listPersons = new ArrayList<>();

	// import
	public void setListPersons(List<Person> listPersons) {
		this.listPersons = listPersons;
	}

	// create
	public Person addPerson(Person p) {
		listPersons.add(p);
		return p;
	}

	// read
	public List<Person> getPersonsByName(String firstName, String lastName) {
		return listPersons.stream().filter(p -> p.getFirstName().equals(firstName))
				.filter(person -> person.getLastName().equals(lastName)).collect(Collectors.toList());
	}

	public Person getPersonByName(String firstName, String lastName) throws PersonNotFoundException {
		Optional<Person> person = listPersons.stream().filter(p -> p.getFirstName().equals(firstName))
				.filter(p -> p.getLastName().equals(lastName)).findFirst();

		if (person.isPresent()) {
			return person.get();
		} else {
			throw new PersonNotFoundException();
		}
	}

	public List<Person> getPersonsByAddress(String address) throws AddressNotFoundException {
		List<Person> persons = listPersons.stream().filter(person -> person.getAddress().equals(address)).collect(Collectors.toList());
		if (persons.size() <= 0) {
			throw new AddressNotFoundException();
		}
		else {
			return persons;
		}
	}

	public List<Person> getPersonsByAddresses(List<String> addresses) {
		return listPersons.stream()
				.filter(p -> addresses.stream()
						.anyMatch(a -> a.equals(p.getAddress()))).collect(Collectors.toList());
				
				//person.getAddress().equals(address)).collect(Collectors.toList());
	}
	
	// update
	public Person updatePerson(Person person) throws PersonNotFoundException {

		Optional<Person> personToUpdate = listPersons.stream()
				.filter(p -> p.getFirstName().equals(person.getFirstName()))
				.filter(p -> p.getLastName().equals(person.getLastName())).findFirst();

		if (personToUpdate.isPresent()) {
			listPersons.set(listPersons.indexOf(personToUpdate.get()), person);
			return person;
		} else {
			throw new PersonNotFoundException();
		}
	}

	// delete
	public void deletePerson(Person person) throws PersonNotFoundException {
		if (!listPersons.remove(person)) {
			throw new PersonNotFoundException();
		}
	}

	public void deletePersonByName(String firstName, String lastName) throws PersonNotFoundException {

		Optional<Person> personToDelete = listPersons.stream().filter(p -> p.getFirstName().equals(firstName))
				.filter(p -> p.getLastName().equals(lastName)).findFirst();

		if (personToDelete.isPresent()) {
			deletePerson(personToDelete.get());
		} else {
			throw new PersonNotFoundException();
		}
	}

}
