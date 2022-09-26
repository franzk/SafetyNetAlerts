package net.safety.alerts.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.safety.alerts.dto.UrlFireDto;
import net.safety.alerts.dto.UrlFirestationCoverageDto;
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

	// endpoints
	public UrlFireDto fire(String address) throws AddressNotFoundException, FirestationNotFoundException {
		return joinedDataService.fire(address);
	}

	public UrlFirestationCoverageDto firestationCoverage(Integer stationNumber) throws FirestationNotFoundException {
		return joinedDataService.firestationCoverage(stationNumber);
	}

}
