package net.safety.alerts.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.model.Firestation;
import net.safety.alerts.service.FirestationService;

@RestController
@RequestMapping("firestation")
public class FirestationController {

	 @Autowired
	 private FirestationService firestationService;
	
	// create
	@PostMapping("")
	public Firestation addFirestation(@RequestBody Firestation firestation) {
		return firestationService.add(firestation);
	}
	
	// update
	@PutMapping("")
	public Firestation updateFirestation(@RequestBody Firestation firestation) throws FirestationNotFoundException {
		return firestationService.update(firestation);
	}
	
	// delete
	@DeleteMapping("")
	public void deleteFirestationByAdress(@RequestParam Optional<String> address, @RequestParam Optional<Integer> station) throws FirestationNotFoundException {
		if (address.isPresent()) {
			firestationService.deleteByAddress(address.get());
		}
		else if (station.isPresent()) {
			firestationService.deleteByStation(station.get());
		}
	}		
}
