package net.safety.alerts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.safety.alerts.dto.FireEndpointDto;
import net.safety.alerts.dto.StationNumberDto;
import net.safety.alerts.exceptions.AddressNotFoundException;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.model.Firestation;
import net.safety.alerts.repository.FirestationRepository;

@Service
public class FirestationService {

	@Autowired
	private FirestationRepository firestationRepository;
	
	@Autowired
	private JoinedDataService joinedDataService;
	
	// create / update	
	public Firestation save(Firestation f) {
		try {
			firestationRepository.updateFirestation(f);
		} catch (FirestationNotFoundException e) {
			firestationRepository.addFirestation(f);
		}
		return f;
	}
	
	// delete
	public void delete(Firestation f) throws FirestationNotFoundException {
		firestationRepository.deleteFirestation(f);
	}
	
	// endpoints
	public FireEndpointDto fire(String address) throws AddressNotFoundException {
		return joinedDataService.fire(address);
	}
	
	public StationNumberDto stationNumber(Integer stationNumber) throws FirestationNotFoundException {
		return joinedDataService.stationNumber(stationNumber);
	}
	
}
