package net.safety.alerts.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.safety.alerts.dto.UrlFloodStationsDto;
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
public class UrlFloodStationsControllerTestIT {
	
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
	public void testFloodStations() throws Exception {
		// Arrange
		List<Firestation> firestations = FirestationTestData.buildFirestationList();
		firestationRepository.setListFirestations(firestations);

		List<Person> persons = PersonTestData.buildPersonList();
		persons.get(0).setAddress(firestations.get(0).getAddress());
		persons.get(1).setAddress(firestations.get(0).getAddress());
		persons.get(2).setAddress(firestations.get(1).getAddress());
		personRepository.setListPersons(persons);

		MedicalRecord medicalRecord1 = MedicalRecordTestData.buildChildMedicalRecord(persons.get(0).getFirstName(),
				persons.get(0).getLastName());
		MedicalRecord medicalRecord2 = MedicalRecordTestData.buildAdultMedicalRecord(persons.get(1).getFirstName(),
				persons.get(1).getLastName());
		MedicalRecord medicalRecord3 = MedicalRecordTestData.buildAdultMedicalRecord(persons.get(2).getFirstName(),
				persons.get(2).getLastName());
		List<MedicalRecord> medicalRecords = List.of(medicalRecord1, medicalRecord2, medicalRecord3);
		medicalRecordRepository.setListMedicalRecords(medicalRecords);

		List<Integer> stationNumbers = firestations.stream().map(f -> f.getStation()).collect(Collectors.toList());

		UrlFloodStationsDto testDto = urlService.urlFloodStations(stationNumbers);
		String expectedBody = mapper.writeValueAsString(testDto);

		// Act
		String strStations = firestations.stream().map(f -> Integer.toString(f.getStation()))
				.collect(Collectors.joining(","));
		mockMvc.perform(get("/flood/stations").param("stations", strStations)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(expectedBody));

	}

}
