package net.safety.alerts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.model.Firestation;
import net.safety.alerts.repository.FirestationRepository;

@Service
public class FirestationService {

	@Autowired
	private FirestationRepository firestationRepository;
	
	public Firestation addFirestation(Firestation f) {
		return firestationRepository.addFirestation(f);
	}
	
	public Firestation save(Firestation f) {
		try {
			firestationRepository.updateFirestation(f);
		} catch (FirestationNotFoundException e) {
			firestationRepository.addFirestation(f);
		}
		return f;
	}
	
}
