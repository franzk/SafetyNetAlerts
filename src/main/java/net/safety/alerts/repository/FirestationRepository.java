package net.safety.alerts.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	// read
	public String getFirestationAdress(Integer StationNumber) throws FirestationNotFoundException {
		Optional<Firestation> firestation = listFirestations.stream()
				.filter(f -> f.getStation().equals(StationNumber)).findFirst();

		if (firestation.isPresent()) {
			return firestation.get().getAddress();
		} else {
			throw new FirestationNotFoundException();
		}
	}

	// create
	public Firestation addFirestation(Firestation firestation) {
		listFirestations.add(firestation);
		return firestation;
	}

	// update
	public Firestation updateFirestation(Firestation firestation) throws FirestationNotFoundException {
		int firestationToUpdateIndex = listFirestations.indexOf(firestation);
		if (firestationToUpdateIndex >= 0) {
			listFirestations.set(firestationToUpdateIndex, firestation);
			return firestation;
		} else {
			throw new FirestationNotFoundException();
		}
	}

}
