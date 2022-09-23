package net.safety.alerts;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.safety.alerts.model.Firestation;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.model.Person;
import net.safety.alerts.repository.FirestationRepository;
import net.safety.alerts.repository.MedicalRecordRepository;
import net.safety.alerts.repository.PersonRepository;
import net.safety.alerts.utils.JsonFileConstants;
import net.safety.alerts.utils.Utils;

@Component
public class DataInitializer {

	@Autowired
	private FirestationRepository firestationRepository;

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private CustomProperties props;

	private JsonNode root;

	@Value("${net.safety.alerts.dataUrl}")
	private String TotoUrl;
	
	@PostConstruct
	public void importJsonData() {

		ObjectMapper objectMapper = new ObjectMapper();
		String dataUrl = props.getDataUrl();

		try {
			root = objectMapper.readTree(new URL(dataUrl));

			loadPersons();
			loadFirestations();
			loadMedicalRecords();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadPersons() {
		JsonNode persons = root.path(JsonFileConstants.persons);

		for (JsonNode personNode : persons) {
			Person person = new Person();
			person.setFirstName(personNode.path(JsonFileConstants.person_firstName).asText());
			person.setLastName(personNode.path(JsonFileConstants.person_lastName).asText());
			person.setAddress(personNode.path(JsonFileConstants.person_address).asText());
			person.setCity(personNode.path(JsonFileConstants.person_city).asText());
			person.setZip(personNode.path(JsonFileConstants.person_zip).asText());
			person.setPhone(personNode.path(JsonFileConstants.person_phone).asText());
			person.setEmail(personNode.path(JsonFileConstants.person_email).asText());

			personRepository.addPerson(person);
		}

	}

	private void loadFirestations() {
		JsonNode firestations = root.path(JsonFileConstants.firestations);

		for (JsonNode firestationNode : firestations) {
			Firestation firestation = new Firestation();
			firestation.setAddress(firestationNode.path(JsonFileConstants.firestation_address).asText());
			firestation.setStation(firestationNode.path(JsonFileConstants.firestation_station).asInt());

			firestationRepository.addFirestation(firestation);
		}

	}

	private void loadMedicalRecords() throws DateTimeParseException {
		JsonNode medicalrecords = root.path(JsonFileConstants.medicalrecords);

		for (JsonNode m : medicalrecords) {
			MedicalRecord medicalRecord = new MedicalRecord();
			medicalRecord.setFirstName(m.path(JsonFileConstants.medicalrecord_firstName).asText());
			medicalRecord.setLastName(m.path(JsonFileConstants.medicalrecord_lastName).asText());

			medicalRecord.setBirthdate(Utils.StringToDate(m.path(JsonFileConstants.medicalrecord_birthdate).asText()));

			// medications
			List<String> medications = new ArrayList<>();
			JsonNode medicationData = m.path(JsonFileConstants.medicalrecord_medications);
			for (JsonNode medicalrecordNode : medicationData) {
				medications.add(medicalrecordNode.asText());
			}
			medicalRecord.setMedications(medications);

			// allergies
			List<String> allergies = new ArrayList<>();
			JsonNode allergiesData = m.path(JsonFileConstants.medicalrecord_allergies);
			for (JsonNode allergieNode : allergiesData) {
				allergies.add(allergieNode.asText());
			}
			medicalRecord.setAllergies(allergies);

			medicalRecordRepository.addMedicalRecord(medicalRecord);
		}

	}

}
