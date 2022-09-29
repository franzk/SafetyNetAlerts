package net.safety.alerts.integration;

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

import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.model.Firestation;
import net.safety.alerts.repository.FirestationRepository;
import net.safety.alerts.utils.FirestationTestData;
import net.safety.alerts.utils.TestConstants;

@SpringBootTest
@AutoConfigureMockMvc
public class EndpointFirestationControllerTestIT {

	@Autowired
	public MockMvc mockMvc;

	@Autowired
	FirestationRepository firestationRepository;

	ObjectMapper mapper = new ObjectMapper();

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@BeforeEach
	public void reset() {
		firestationRepository.setListFirestations(new ArrayList<>());
	}

	@Test
	public void testPostFirestation() throws Exception {
		// Arrange
		Firestation testFirestation = FirestationTestData.buildFirestation();

		String requestJson = mapper.writeValueAsString(testFirestation);

		// Act + Assert
		String expectedBody = mapper.writeValueAsString(testFirestation);
		mockMvc.perform(post("/firestation").contentType(APPLICATION_JSON_UTF8).content(requestJson))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().string(expectedBody));

	}

	@Test
	public void getFirestationByAddress() throws Exception {
		// Arrange
		List<Firestation> testListFirestation = new ArrayList<>();
		String testAddress = TestConstants.address;
		Firestation f1 = FirestationTestData.buildFirestation(testAddress, TestConstants.stationNumber);
		Firestation f2 = FirestationTestData.buildFirestation(testAddress, TestConstants.stationNumber + 1);
		testListFirestation.add(f1);
		testListFirestation.add(f2);
		firestationRepository.addFirestation(f1);
		firestationRepository.addFirestation(f2);

		// Act + Assert
		String expectedBody = mapper.writeValueAsString(testListFirestation);
		mockMvc.perform(get("/firestation").param("address", testAddress)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(expectedBody));

	}

	@Test
	public void getFirestationByStationNumber() throws Exception {
		// Arrange
		List<Firestation> testListFirestation = new ArrayList<>();
		Integer testStationNumber = TestConstants.stationNumber;
		Firestation f1 = FirestationTestData.buildFirestation(TestConstants.address, testStationNumber);
		Firestation f2 = FirestationTestData.buildFirestation(TestConstants.address + "2", testStationNumber);
		testListFirestation.add(f1);
		testListFirestation.add(f2);
		firestationRepository.addFirestation(f1);
		firestationRepository.addFirestation(f2);

		// Act + Assert
		String expectedBody = mapper.writeValueAsString(testListFirestation);
		mockMvc.perform(get("/firestation").param("stationNumber", Integer.toString(testStationNumber)))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().string(expectedBody));

	}

	@Test
	public void updateFirestationTest() throws Exception {
		// Arrange
		Firestation testFirestation = FirestationTestData.buildFirestation();
		firestationRepository.addFirestation(testFirestation);

		Firestation updatedFirestation = new Firestation();
		updatedFirestation.setAddress(testFirestation.getAddress());
		updatedFirestation.setStation(testFirestation.getStation() + 1);

		String requestJson = mapper.writeValueAsString(updatedFirestation);

		// Act + Assert
		String expectedBody = mapper.writeValueAsString(updatedFirestation);
		mockMvc.perform(put("/firestation").contentType(APPLICATION_JSON_UTF8).content(requestJson))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().string(expectedBody));

	}

	@Test
	public void deleteFirestationByAddress() throws Exception {
		// Arrange
		String testAddress = TestConstants.address;
		Firestation f1 = FirestationTestData.buildFirestation(testAddress, TestConstants.stationNumber);
		Firestation f2 = FirestationTestData.buildFirestation(testAddress, TestConstants.stationNumber + 1);
		firestationRepository.addFirestation(f1);
		firestationRepository.addFirestation(f2);

		// Act + Assert
		mockMvc.perform(delete("/firestation").param("address", testAddress)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(containsString("deleted")));

		// Verify
		assertThrows(FirestationNotFoundException.class,
				() -> firestationRepository.getFirestationsByAddress(testAddress));
	}

	@Test
	public void deleteFirestationByStationNumber() throws Exception {
		// Arrange
		Integer testStationNumber = TestConstants.stationNumber;
		Firestation f1 = FirestationTestData.buildFirestation(TestConstants.address, testStationNumber);
		Firestation f2 = FirestationTestData.buildFirestation(TestConstants.address + "2", testStationNumber);
		firestationRepository.addFirestation(f1);
		firestationRepository.addFirestation(f2);

		// Act + Assert
		mockMvc.perform(delete("/firestation").param("stationNumber", Integer.toString(testStationNumber)))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(containsString("deleted")));

		// Verify
		assertThrows(FirestationNotFoundException.class,
				() -> firestationRepository.getFirestationsByStationNumber(testStationNumber));
	}

}
