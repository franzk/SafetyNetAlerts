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
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import net.safety.alerts.exceptions.MedicalRecordNotFoundException;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.repository.MedicalRecordRepository;
import net.safety.alerts.utils.MedicalRecordTestData;

@SpringBootTest
@AutoConfigureMockMvc
public class EndpointMedicalRecordControllerIT {

	@Autowired
	public MockMvc mockMvc;

	@Autowired
	MedicalRecordRepository medicalRecordRepository;
	
	@Autowired
	MedicalRecordTestData medicalRecordTestData;

	private static ObjectMapper mapper = new ObjectMapper();

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@BeforeAll
	private static void setUp() {
		mapper.registerModule(new JavaTimeModule());
	}

	@BeforeEach
	public void reset() {
		medicalRecordRepository.setListMedicalRecords(new ArrayList<>());
	}

	@Test
	public void testPostMedicalRecord() throws Exception {
		// Arrange
		MedicalRecord testMedicalRecord = medicalRecordTestData.buildMedicalRecord();
		String requestJson = mapper.writeValueAsString(testMedicalRecord);

		// Act
		mockMvc.perform(post("/medicalRecord").contentType(APPLICATION_JSON_UTF8).content(requestJson))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().string(requestJson));

		// Assert
		assertThat(medicalRecordRepository.getMedicalRecordByName(testMedicalRecord.getFirstName(),
				testMedicalRecord.getLastName())).isEqualTo(testMedicalRecord);

	}

	@Test
	public void testGetMedicalRecord() throws Exception {
		// Arrange
		MedicalRecord testMedicalRecord = medicalRecordTestData.buildMedicalRecord();
		medicalRecordRepository.addMedicalRecord(testMedicalRecord);

		// Act + Assert
		String expectedBody = mapper.writeValueAsString(testMedicalRecord);
		mockMvc.perform(get("/medicalRecord").param("firstName", testMedicalRecord.getFirstName()).param("lastName",
				testMedicalRecord.getLastName())).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(expectedBody));
	}

	@Test
	public void testPutMedicalRecordt() throws Exception {
		MedicalRecord testMedicalRecord = medicalRecordTestData.buildMedicalRecord();
		medicalRecordRepository.addMedicalRecord(testMedicalRecord);
		MedicalRecord updatedMedicalRecord = medicalRecordTestData.buildMedicalRecord();
		updatedMedicalRecord.setBirthdate(LocalDate.now());

		// Act
		String requestJson = mapper.writeValueAsString(updatedMedicalRecord);
		mockMvc.perform(put("/medicalRecord").contentType(APPLICATION_JSON_UTF8).content(requestJson))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().string(requestJson));

		// Assert
		MedicalRecord result = medicalRecordRepository.getMedicalRecordByName(testMedicalRecord.getFirstName(),
				testMedicalRecord.getLastName());
		assertThat(result).isEqualTo(updatedMedicalRecord);
	}

	@Test
	public void testDeleteMedicalRecord() throws Exception {
		// Arrange
		MedicalRecord testMedicalRecord = medicalRecordTestData.buildMedicalRecord();
		medicalRecordRepository.addMedicalRecord(testMedicalRecord);
		String testFirstName = testMedicalRecord.getFirstName();
		String testLastName = testMedicalRecord.getLastName();

		// Act
		mockMvc.perform(delete("/medicalRecord").param("firstName", testFirstName).param("lastName", testLastName))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(containsString("deleted")));

		// Assert
		assertThrows(MedicalRecordNotFoundException.class,
				() -> medicalRecordRepository.getMedicalRecordByName(testFirstName, testLastName));
	}

}
