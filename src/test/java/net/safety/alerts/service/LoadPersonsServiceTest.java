package net.safety.alerts.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.safety.alerts.model.Person;
import net.safety.alerts.repository.PersonRepository;
import net.safety.alerts.utils.PersonTestData;

@ExtendWith(MockitoExtension.class)
public class LoadPersonsServiceTest {

	@InjectMocks
	private LoadPersonsService serviceUnderTest;

	@Mock
	private PersonRepository personRepository;

	@Test
	public void loadPersonsTest() {
		
		List<Person> persons = PersonTestData.buildPersonList();
		
		ObjectMapper mapper = new ObjectMapper();
		
		JsonNode personsNode = mapper.valueToTree(persons);
		
		serviceUnderTest.loadPersons(personsNode);
		
		verify(personRepository, times(persons.size())).addPerson(any());
	}

}
