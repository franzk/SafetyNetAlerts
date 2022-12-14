package net.safety.alerts.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.model.Firestation;

/**
 * Contains Firestation Data, CRUD and advanced filter methods
 * 
 * @author FranzKa
 *
 */
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
			listFirestations.set(firestationToUpdateIndex, firestation);
			return firestation;
		}
		throw new FirestationNotFoundException();
	}

	// delete
	public void deleteFirestation(Firestation firestation) throws FirestationNotFoundException {
		if (!listFirestations.remove(firestation)) {
			throw new FirestationNotFoundException();
		}
	}

	public void deleteByAddress(String address) throws FirestationNotFoundException {
		List<Firestation> firestations = this.getFirestationsByAddress(address);
		for (Firestation f : firestations) {
			this.deleteFirestation(f);
		}
	}

	public void deleteByStationNumber(Integer stationNumber) throws FirestationNotFoundException {
		List<Firestation> firestations = this.getFirestationsByStationNumber(stationNumber);
		for (Firestation f : firestations) {
			this.deleteFirestation(f);
		}
	}

	// read
	public List<Firestation> getFirestationsByStationNumber(Integer stationNumber) throws FirestationNotFoundException {

		List<Firestation> firestations = listFirestations.stream().filter(f -> f.getStation().equals(stationNumber))
				.toList();

		if (firestations.isEmpty()) {
			throw new FirestationNotFoundException();
		} else {
			return firestations;
		}

	}

	public List<Firestation> getFirestationsByAddress(String address) throws FirestationNotFoundException {
		List<Firestation> firestations = listFirestations.stream().filter(f -> f.getAddress().equals(address)).toList();

		if (firestations.isEmpty()) {
			throw new FirestationNotFoundException();
		} else {
			return firestations;
		}
	}

	public List<String> getFirestationAddresses(Integer StationNumber) throws FirestationNotFoundException {
		List<Firestation> firestations = this.getFirestationsByStationNumber(StationNumber);

		List<String> adresses = firestations.stream().map(f -> f.getAddress()).toList();
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

}
