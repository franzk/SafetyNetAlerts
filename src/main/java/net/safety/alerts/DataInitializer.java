package net.safety.alerts;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.safety.alerts.service.LoadFirestationsService;
import net.safety.alerts.service.LoadMedicalRecordsService;
import net.safety.alerts.service.LoadPersonsService;
import net.safety.alerts.utils.JsonFileConstants;

/**
 * 
 * Import data from a Json File to Repositories.
 * 
 * <pre>
 * Use 3 services : 
 * 
 * - {@link LoadPersonsService}
 * - {@link LoadFirestationsService}
 * - {@link LoadMedicalRecordsService}
 * 
 * </pre>
 * 
 * * @author FranzKa
 * 
 */

@Component
public class DataInitializer {

	@Autowired
	LoadPersonsService loadPersonsService;

	@Autowired
	LoadFirestationsService loadFirestationsService;

	@Autowired
	LoadMedicalRecordsService loadMedicalRecordsService;

	@Value("${net.safety.alerts.dataUrl}")
	private String dataUrl;

	/**
	 * Import the JsonFile found at the URL stored in application.properties
	 * 
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	@PostConstruct
	public void importJsonData() throws MalformedURLException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();

		JsonNode root = objectMapper.readTree(new URL(dataUrl));

		// loadPersons();
		loadPersonsService.loadPersons(root.path(JsonFileConstants.persons));
		loadFirestationsService.loadFirestations(root.path(JsonFileConstants.firestations));
		loadMedicalRecordsService.loadMedicalRecords(root.path(JsonFileConstants.medicalrecords));
	}

}
