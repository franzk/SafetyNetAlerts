package net.safety.alerts.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.model.Person;
import net.safety.alerts.model.PersonDto;

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
	public List<Person> getAll() {
		return listPersons;
	}

	public List<Person> getPersonsByName(String firstName, String lastName) {
		return listPersons.stream().filter(person -> person.getFirstName().equals(firstName))
				.filter(person -> person.getLastName().equals(lastName)).collect(Collectors.toList());
	}

	public List<Person> getPersonsByAddress(String address) {
		return listPersons.stream().filter(person -> person.getAddress().equals(address)).collect(Collectors.toList());
	}

	// update
	public Person updatePerson(Person p) throws PersonNotFoundException {

		Optional<Person> personToUpdate = listPersons.stream()
				.filter(person -> person.getFirstName().equals(p.getFirstName()))
				.filter(person -> person.getLastName().equals(p.getLastName())).findFirst();

		if (personToUpdate.isPresent()) {
			listPersons.set(listPersons.indexOf(personToUpdate.get()), p);
			return p;
		} else {
			// generate ERROR
			throw new PersonNotFoundException();
		}
	}

	// delete
	public void deletePerson(Person p) throws PersonNotFoundException {
		if (!listPersons.remove(p)) {
			throw new PersonNotFoundException();
		}
	}

	public void deleteByName(String firstName, String lastName) throws PersonNotFoundException {

		Optional<Person> personToDelete = listPersons.stream().filter(person -> person.getFirstName().equals(firstName))
				.filter(person -> person.getLastName().equals(lastName)).findFirst();

		if (personToDelete.isPresent()) {
			deletePerson(personToDelete.get());
		} else {
			throw new PersonNotFoundException();
		}
	}

	// Utils
	public PersonDto convertPersonToDto(Person person) {
		ModelMapper mapper = new ModelMapper();
		return mapper.map(person, PersonDto.class);
	}

}
