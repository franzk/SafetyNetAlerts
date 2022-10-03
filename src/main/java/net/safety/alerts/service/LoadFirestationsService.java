package net.safety.alerts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import net.safety.alerts.model.Firestation;
import net.safety.alerts.repository.FirestationRepository;
import net.safety.alerts.utils.JsonFileConstants;

@Service
public class LoadFirestationsService {

	@Autowired
	private FirestationRepository firestationRepository;

	/**
	 * Importe les firestations dans firestationRepository à partir du path
	 * "firestations" du fichier Json
	 */
	public void loadFirestations(JsonNode firestationsNode) {

		for (JsonNode firestationNode : firestationsNode) {
			Firestation firestation = new Firestation();
			firestation.setAddress(firestationNode.path(JsonFileConstants.firestation_address).asText());
			firestation.setStation(firestationNode.path(JsonFileConstants.firestation_station).asInt());

			firestationRepository.addFirestation(firestation);
		}

	}

}
