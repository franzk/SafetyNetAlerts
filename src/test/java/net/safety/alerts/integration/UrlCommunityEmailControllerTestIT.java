package net.safety.alerts.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.safety.alerts.dto.UrlCommunityEmailDto;
import net.safety.alerts.model.Person;
import net.safety.alerts.repository.PersonRepository;
import net.safety.alerts.service.UrlService;
import net.safety.alerts.utils.PersonTestData;

@SpringBootTest
@AutoConfigureMockMvc
public class UrlCommunityEmailControllerTestIT {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private UrlService urlService;
	
	@Test
	public void testCommunityEmail() throws Exception {
		// Arrange
		String testCity = "city";
		List<Person> persons = PersonTestData.buildPersonList();
		persons.forEach(p -> p.setCity(testCity));
		personRepository.setListPersons(persons);
		
		UrlCommunityEmailDto testDto = urlService.urlCommunityEmail(testCity);
		
		ObjectMapper mapper = new ObjectMapper();
		String expectedBody = mapper.writeValueAsString(testDto);
		
		// Act
		mockMvc.perform(get("/communityEmail").param("city", testCity)).andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.content().string(expectedBody));
 	}
	
}
