package net.safety.alerts.service;

import java.util.ArrayList;
import java.util.List;

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

	// create
	public Firestation add(Firestation f) {
		return firestationRepository.addFirestation(f);
	}

	// read
	public List<Firestation> getByAddress(String address) throws FirestationNotFoundException {
		List<Firestation> firestations = new ArrayList<>();
		firestations.add(firestationRepository.getFirestationByAddress(address));
		return firestations;
	}

	public List<Firestation> getByStationNumber(Integer stationNumber) throws FirestationNotFoundException {
		return firestationRepository.getFirestationByStationNumber(stationNumber);
	}

	// update
	public Firestation update(Firestation f) throws FirestationNotFoundException {
		return firestationRepository.updateFirestation(f);
	}

	// delete
	public void delete(Firestation f) throws FirestationNotFoundException {
		firestationRepository.deleteFirestation(f);
	}

	public void deleteByAddress(String address) throws FirestationNotFoundException {
		Firestation firestation = firestationRepository.getFirestationByAddress(address);
		firestationRepository.deleteFirestation(firestation);
	}

	public void deleteByStationNumber(Integer stationNumber) throws FirestationNotFoundException {
		List<Firestation> firestations = firestationRepository.getFirestationByStationNumber(stationNumber);
		for(Firestation f : firestations) {
			firestationRepository.deleteFirestation(f);
		}
	}

	// endpoints
	public FireEndpointDto fire(String address) throws AddressNotFoundException {
		return joinedDataService.fire(address);
	}

	public StationNumberDto firestationPersonsCovered(Integer stationNumber) throws FirestationNotFoundException {
		return joinedDataService.firestationPersonsCovered(stationNumber);
	}

}
