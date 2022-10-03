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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.repository.MedicalRecordRepository;
import net.safety.alerts.utils.MedicalRecordTestData;

@ExtendWith(MockitoExtension.class)
public class LoadMedicalRecordsServiceTest {

	@InjectMocks
	private LoadMedicalRecordsService serviceUnderTest;

	@Mock
	private MedicalRecordRepository medicalRecordRepository;

	@Test
	public void loadMedicalRecordsTest() throws JsonProcessingException {

		List<MedicalRecord> medicalRecords = MedicalRecordTestData.buildMedicalRecordList();

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		JsonNode medicalRecordsNode = mapper.valueToTree(medicalRecords);

		serviceUnderTest.loadMedicalRecords(medicalRecordsNode);

		verify(medicalRecordRepository, times(medicalRecords.size())).addMedicalRecord(any());
	}

}
