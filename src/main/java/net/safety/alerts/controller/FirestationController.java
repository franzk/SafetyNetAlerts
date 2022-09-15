package net.safety.alerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.safety.alerts.dto.FireDto;
import net.safety.alerts.dto.StationNumberDto;
import net.safety.alerts.exceptions.AddressNotFoundException;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.repository.JoinedDataRepository;

@RestController
public class FirestationController {

	@Autowired
	private JoinedDataRepository joinedDataRepository;

	@GetMapping("/firestation")
	public ResponseEntity<StationNumberDto> stationNumber(@RequestParam Integer stationNumber) throws FirestationNotFoundException {
		return new ResponseEntity<>(joinedDataRepository.stationNumber(stationNumber), HttpStatus.OK);
	}

	@GetMapping("/fire")
	public ResponseEntity<FireDto> fire(@RequestParam String address) throws AddressNotFoundException {
		return new ResponseEntity<>(joinedDataRepository.fire(address), HttpStatus.OK);
	}

}
