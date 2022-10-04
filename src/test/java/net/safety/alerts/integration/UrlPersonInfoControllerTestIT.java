package net.safety.alerts.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.safety.alerts.dto.UrlPersonInfoDto;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.model.Person;
import net.safety.alerts.repository.MedicalRecordRepository;
import net.safety.alerts.repository.PersonRepository;
import net.safety.alerts.service.UrlService;
import net.safety.alerts.utils.MedicalRecordTestData;
import net.safety.alerts.utils.PersonTestData;

@SpringBootTest
@AutoConfigureMockMvc
public class UrlPersonInfoControllerTestIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	@Autowired
	private UrlService urlService;

	@Autowired
	ObjectMapper mapper;

	@Test
	public void testPersonInfo() throws Exception {
		// Arrange
		Person testPerson = PersonTestData.buildPerson();
		String testFirstName = testPerson.getFirstName();
		String testLastName = testPerson.getLastName();
		personRepository.addPerson(testPerson);

		MedicalRecord testMedicalRecord = MedicalRecordTestData.buildAdultMedicalRecord(testFirstName, testLastName);
		medicalRecordRepository.addMedicalRecord(testMedicalRecord);

		UrlPersonInfoDto testDto = urlService.urlPersonInfo(testFirstName, testLastName);
		String expectedBody = mapper.writeValueAsString(testDto);

		// Act
		mockMvc.perform(get("/personInfo").param("firstName", testFirstName).param("lastName", testLastName))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().string(expectedBody));

	}
}
