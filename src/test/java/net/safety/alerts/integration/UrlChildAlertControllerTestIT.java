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

import net.safety.alerts.dto.UrlChildAlertDto;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.model.Person;
import net.safety.alerts.repository.MedicalRecordRepository;
import net.safety.alerts.repository.PersonRepository;
import net.safety.alerts.service.UrlService;
import net.safety.alerts.utils.MedicalRecordTestData;
import net.safety.alerts.utils.PersonTestData;

@SpringBootTest
@AutoConfigureMockMvc
public class UrlChildAlertControllerTestIT {

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	@Autowired
	private UrlService urlService;

	@Autowired
	public MockMvc mockMvc;

	@Test
	public void testChildAlert() throws Exception {
		// Arrange
		String testAddress = "test Address";
		Person child = PersonTestData.buildPerson("child", "lastName", testAddress, "city");
		Person houseHolder1 = PersonTestData.buildPerson("adult", "one", testAddress, "city");
		Person houseHolder2 = PersonTestData.buildPerson("adult", "two", testAddress, "city");
		List<Person> persons = List.of(child, houseHolder1, houseHolder2);
		personRepository.setListPersons(persons);

		MedicalRecord childMedicalRecord = MedicalRecordTestData.buildChildMedicalRecord("child", "lastName");
		MedicalRecord AdultOneMedicalRecord = MedicalRecordTestData.buildAdultMedicalRecord("adult", "one");
		MedicalRecord AdultTwoMedicalRecord = MedicalRecordTestData.buildAdultMedicalRecord("adult", "two");
		List<MedicalRecord> medicalRecords = List.of(childMedicalRecord, AdultOneMedicalRecord, AdultTwoMedicalRecord);
		medicalRecordRepository.setListMedicalRecords(medicalRecords);

		UrlChildAlertDto testDto = urlService.urlChildAlert(testAddress);

		ObjectMapper mapper = new ObjectMapper();
		String expectedBody = mapper.writeValueAsString(testDto);

		// Act + Assert
		mockMvc.perform(get("/childAlert").param("address", testAddress)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(expectedBody));
		
	}

}
