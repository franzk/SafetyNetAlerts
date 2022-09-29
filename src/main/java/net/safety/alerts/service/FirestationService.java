package net.safety.alerts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.model.Firestation;
import net.safety.alerts.repository.FirestationRepository;

@Service
public class FirestationService {

	@Autowired
	private FirestationRepository firestationRepository;

	// create
	public Firestation add(Firestation f) {
		return firestationRepository.addFirestation(f);
	}

	// read
	public List<Firestation> getByAddress(String address) throws FirestationNotFoundException {
		return firestationRepository.getFirestationsByAddress(address);
	}

	public List<Firestation> getByStationNumber(Integer stationNumber) throws FirestationNotFoundException {
		return firestationRepository.getFirestationsByStationNumber(stationNumber);
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
		firestationRepository.deleteByAddress(address);
	}

	public void deleteByStationNumber(Integer stationNumber) throws FirestationNotFoundException {
		firestationRepository.deleteByStationNumber(stationNumber);
	}

}
