package net.safety.alerts.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.model.Firestation;

@Repository
public class FirestationRepository {

	private List<Firestation> listFirestations = new ArrayList<>();

	// import
	public void setListFirestations(List<Firestation> listFirestations) {
		this.listFirestations = listFirestations;
	}

	// create
	public Firestation addFirestation(Firestation firestation) {
		listFirestations.add(firestation);
		return firestation;
	}

	// update
	public Firestation updateFirestation(Firestation firestation) throws FirestationNotFoundException {
		Optional<Firestation> firestationToUpdate = listFirestations.stream()
				.filter(f -> f.getAddress().equals(firestation.getAddress())).findFirst();

		if (firestationToUpdate.isPresent()) {
			int firestationToUpdateIndex = listFirestations.indexOf(firestationToUpdate.get());
			if (firestationToUpdateIndex >= 0) {
				listFirestations.set(firestationToUpdateIndex, firestation);
				return firestation;
			}
		}
		throw new FirestationNotFoundException();
	}

	// delete
	public void deleteFirestation(Firestation firestation) throws FirestationNotFoundException {
		if (!listFirestations.remove(firestation)) {
			throw new FirestationNotFoundException();
		}
	}

	// read
	public List<Firestation> getFirestationByStationNumber(Integer stationNumber) throws FirestationNotFoundException {
		return listFirestations.stream().filter(f -> f.getStation().equals(stationNumber)).collect(Collectors.toList());
	}

	public List<String> getFirestationAddresses(Integer StationNumber) throws FirestationNotFoundException {
		List<Firestation> firestations = this.getFirestationByStationNumber(StationNumber);
		List<String> adresses = firestations.stream().map(Firestation::getAddress).collect(Collectors.toList());
		return adresses;
	}

	public Integer getFirestationNumber(String address) throws FirestationNotFoundException {
		Optional<Firestation> firestation = listFirestations.stream().filter(f -> f.getAddress().equals(address))
				.findFirst();

		if (firestation.isPresent()) {
			return firestation.get().getStation();
		} else {
			throw new FirestationNotFoundException();
		}
	}

	public Firestation getFirestationByAddress(String address) throws FirestationNotFoundException {
		Optional<Firestation> firestation = listFirestations.stream().filter(f -> f.getAddress().equals(address))
				.findFirst();

		if (firestation.isPresent()) {
			return firestation.get();
		} else {
			throw new FirestationNotFoundException();
		}
	}

}
