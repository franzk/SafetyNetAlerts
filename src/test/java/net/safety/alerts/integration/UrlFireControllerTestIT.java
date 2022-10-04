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

import net.safety.alerts.dto.UrlFireDto;
import net.safety.alerts.model.Firestation;
import net.safety.alerts.model.Person;
import net.safety.alerts.repository.FirestationRepository;
import net.safety.alerts.repository.PersonRepository;
import net.safety.alerts.service.UrlService;
import net.safety.alerts.utils.FirestationTestData;
import net.safety.alerts.utils.PersonTestData;

@SpringBootTest
@AutoConfigureMockMvc
public class UrlFireControllerTestIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	PersonRepository personRepository;

	@Autowired
	FirestationRepository firestationRepository;

	@Autowired
	UrlService urlService;

	@Autowired
	ObjectMapper mapper;

	@Test
	public void testFire() throws Exception {
		// Arrange
		String testAddress = "address";
		Integer testStationNumber = 42;
		Firestation testFirestation = FirestationTestData.buildFirestation(testAddress, testStationNumber);
		firestationRepository.addFirestation(testFirestation);

		List<Person> persons = PersonTestData.buildPersonList();
		persons.forEach(p -> p.setAddress(testAddress));
		personRepository.setListPersons(persons);

		UrlFireDto testDto = urlService.urlFire(testAddress);

		String expectedBody = mapper.writeValueAsString(testDto);

		// Act
		mockMvc.perform(get("/fire").param("address", testAddress)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(expectedBody));
	}

}
