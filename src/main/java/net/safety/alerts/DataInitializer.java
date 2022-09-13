package net.safety.alerts;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.safety.alerts.model.JsonData;
import net.safety.alerts.repository.FirestationRepository;
import net.safety.alerts.repository.MedicalRecordRepository;
import net.safety.alerts.repository.PersonRepository;

@Component
public class DataInitializer {

	@Autowired
	private FirestationRepository firestationRepository;

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	@Autowired
	private PersonRepository personRepository;

	public void importJsonData() {
		ObjectMapper objectMapper = new ObjectMapper();
		String dataUrl = "https://s3-eu-west-1.amazonaws.com/course.oc-static.com/projects/DA+Java+EN/P5+/data.json";

		try {
			JsonData sourceData = objectMapper.readValue(new URL(dataUrl), JsonData.class);

			firestationRepository.setListFirestations(Arrays.asList(sourceData.getFirestations()));
			medicalRecordRepository.setListMedicalRecords(Arrays.asList(sourceData.getMedicalrecords()));
			personRepository.setListPersons(Arrays.asList(sourceData.getPersons()));

		} catch (StreamReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
