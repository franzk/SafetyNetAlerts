package net.safety.alerts.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.model.Person;
import net.safety.alerts.repository.PersonRepository;
import net.safety.alerts.utils.PersonTestData;

@SpringBootTest
@AutoConfigureMockMvc
public class EndpointPersonControllerTestIT {

	@Autowired
	public MockMvc mockMvc;

	@Autowired
	public PersonRepository personRepository;

	ObjectMapper mapper = new ObjectMapper();

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@BeforeEach
	public void reset() {
		personRepository.setListPersons(new ArrayList<>());
	}

	@Test
	public void testPostPerson() throws Exception {
		// Arrange
		Person testPerson = PersonTestData.buildPerson();

		String requestJson = mapper.writeValueAsString(testPerson);

		// Act
		String expectedBody = mapper.writeValueAsString(testPerson);
		mockMvc.perform(post("/person").contentType(APPLICATION_JSON_UTF8).content(requestJson))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().string(expectedBody));

		// Assert
		assertThat(personRepository.getPersonByName(testPerson.getFirstName(), testPerson.getLastName()))
				.isEqualTo(testPerson);

	}

	@Test
	public void testGetPerson() throws Exception {
		// Arrange
		Person testPerson = PersonTestData.buildPerson();
		String testFirstName = testPerson.getFirstName();
		String testLastName = testPerson.getLastName();
		personRepository.addPerson(testPerson);

		// Act + Assert
		String expectedBody = mapper.writeValueAsString(testPerson);
		mockMvc.perform(get("/person").param("firstName", testFirstName).param("lastName", testLastName))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().string(expectedBody));

	}

	@Test
	public void testPutPerson() throws Exception {
		// Arrange
		Person testPerson = PersonTestData.buildPerson("lastName", "firstName", "address", "city");
		personRepository.addPerson(testPerson);

		Person updatedPerson = PersonTestData.buildPerson("lastName", "firstName", "other address", "other city");

		String requestJson = mapper.writeValueAsString(updatedPerson);

		// Act + Assert
		String expectedBody = mapper.writeValueAsString(updatedPerson);
		mockMvc.perform(put("/person").contentType(APPLICATION_JSON_UTF8).content(requestJson))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().string(expectedBody));

	}

	@Test
	public void testDeletePerson() throws Exception {
		// Arrange
		List<Person> testPersonList = PersonTestData.buildPersonList();
		Person testPerson = testPersonList.get(0);
		String testFirstName = testPerson.getFirstName();
		String testLastName = testPerson.getLastName();
		personRepository.setListPersons(testPersonList);

		// Act + Assert
		mockMvc.perform(delete("/person").param("firstName", testFirstName).param("lastName", testLastName))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(containsString("deleted")));

		// Verify
		assertThrows(PersonNotFoundException.class,
				() -> personRepository.getPersonByName(testFirstName, testLastName));
	}

}
