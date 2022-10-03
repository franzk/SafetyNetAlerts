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

import net.safety.alerts.model.Firestation;
import net.safety.alerts.repository.FirestationRepository;
import net.safety.alerts.utils.FirestationTestData;

@ExtendWith(MockitoExtension.class)
public class LoadFirestationsServiceTest {

	@InjectMocks
	private LoadFirestationsService serviceUnderTest;
	
	@Mock
	FirestationRepository firestationRepository;
	
	@Test
	public void loadFirestationsTest() {
		List<Firestation> firestations = FirestationTestData.buildFirestationList();

		ObjectMapper mapper = new ObjectMapper();
		
		JsonNode firestationsNode = mapper.valueToTree(firestations);
		
		serviceUnderTest.loadFirestations(firestationsNode);
	
		verify(firestationRepository, times(firestations.size())).addFirestation(any());
	}
	
	
}
