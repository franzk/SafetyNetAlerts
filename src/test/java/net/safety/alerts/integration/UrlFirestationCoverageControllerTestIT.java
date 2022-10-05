package net.safety.alerts.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.safety.alerts.dto.UrlFirestationCoverageDto;
import net.safety.alerts.model.Firestation;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.model.Person;
import net.safety.alerts.repository.FirestationRepository;
import net.safety.alerts.repository.MedicalRecordRepository;
import net.safety.alerts.repository.PersonRepository;
import net.safety.alerts.service.UrlService;
import net.safety.alerts.utils.FirestationTestData;
import net.safety.alerts.utils.MedicalRecordTestData;
import net.safety.alerts.utils.PersonTestData;

@SpringBootTest
@AutoConfigureMockMvc
public class UrlFirestationCoverageControllerTestIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private FirestationRepository firestationRepository;

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	@Autowired
	private UrlService urlService;

	@Autowired
	ObjectMapper mapper;

	@BeforeEach
	public void reset() {
		personRepository.setListPersons(new ArrayList<>());
		medicalRecordRepository.setListMedicalRecords(new ArrayList<>());
		firestationRepository.setListFirestations(new ArrayList<>());
	}

	@Test
	public void testFirestationCoverage() throws Exception {
		// Arrange
		String testAddress = "address";
		Integer testStationNumber = 42;
		Firestation testFirestation = FirestationTestData.buildFirestation(testAddress, testStationNumber);
		firestationRepository.addFirestation(testFirestation);

		Person child = PersonTestData.buildPerson("child", "test", testAddress, "city");
		Person adult1 = PersonTestData.buildPerson("adult", "one", testAddress, "city");
		Person adult2 = PersonTestData.buildPerson("adult", "two", testAddress, "city");
		List<Person> persons = List.of(child, adult1, adult2);
		personRepository.setListPersons(persons);

		MedicalRecord childMedicalRecord = MedicalRecordTestData.buildChildMedicalRecord("child", "test");
		MedicalRecord adult1MedicalRecord = MedicalRecordTestData.buildAdultMedicalRecord("adult", "one");
		MedicalRecord adult2MedicalRecord = MedicalRecordTestData.buildAdultMedicalRecord("adult", "two");
		List<MedicalRecord> medicalRecords = List.of(childMedicalRecord, adult1MedicalRecord, adult2MedicalRecord);
		medicalRecordRepository.setListMedicalRecords(medicalRecords);

		UrlFirestationCoverageDto testDto = urlService.urlFirestationCoverage(testStationNumber);

		String expectedBody = mapper.writeValueAsString(testDto);

		// Act
		mockMvc.perform(get("/firestationCoverage").param("stationNumber", Integer.toString(testStationNumber)))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().string(expectedBody));
	}

}
