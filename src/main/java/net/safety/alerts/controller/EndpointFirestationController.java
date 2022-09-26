package net.safety.alerts.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
public class EndpointFirestationController {

	@Autowired
	private FirestationService firestationService;

	// create
	@PostMapping("")
	public ResponseEntity<Firestation> addFirestation(@RequestBody Firestation firestation) {
		return new ResponseEntity<>(firestationService.add(firestation), HttpStatus.OK);
	}

	// read
	@GetMapping("")
	public ResponseEntity<List<Firestation>> getFirestation(@RequestParam Optional<String> address,
			@RequestParam Optional<Integer> stationNumber) throws FirestationNotFoundException {
		if (address.isPresent()) {
			return new ResponseEntity<>(firestationService.getByAddress(address.get()), HttpStatus.OK);
		}
		else if (stationNumber.isPresent()) {
			return new ResponseEntity<>(firestationService.getByStationNumber(stationNumber.get()), HttpStatus.OK);
		}
		else {
			throw new IllegalArgumentException();
		}

	}

	// update
	@PutMapping("")
	public ResponseEntity<Firestation> updateFirestation(@RequestBody Firestation firestation)
			throws FirestationNotFoundException {
		return new ResponseEntity<>(firestationService.update(firestation), HttpStatus.OK);
	}

	// delete
	@DeleteMapping("")
	public ResponseEntity<String> deleteFirestations(@RequestParam Optional<String> address,
			@RequestParam Optional<Integer> stationNumber) throws FirestationNotFoundException {
		String message = "";
		if (address.isPresent()) {
			firestationService.deleteByAddress(address.get());
			message = "Firestation deleted";
		} else if (stationNumber.isPresent()) {
			firestationService.deleteByStationNumber(stationNumber.get());
			message = "Firestation(s) deleted";
		} else {
			throw new IllegalArgumentException();
		}

		return ResponseEntity.status(HttpStatus.OK).body(message);

	}
}
