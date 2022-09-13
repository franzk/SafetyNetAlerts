package net.safety.alerts.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.model.Firestation;

@Repository
public class FirestationRepository {

	private List<Firestation> listFirestations = new ArrayList<>();
	

	//import
	public void setListFirestations(List<Firestation> listFirestations) {
		this.listFirestations = listFirestations;
	}

	//create
	public Firestation addFirestation(Firestation f) {
		listFirestations.add(f);
		return f;
	}
	
	//update
	public Firestation updateFirestation(Firestation f) throws FirestationNotFoundException {
		int firestationToUpdateIndex = listFirestations.indexOf(f);
		if (firestationToUpdateIndex >=0) {
			listFirestations.set(firestationToUpdateIndex, f);
			return f;
		}
		else {
			throw new FirestationNotFoundException();
		}
	}
	
}
