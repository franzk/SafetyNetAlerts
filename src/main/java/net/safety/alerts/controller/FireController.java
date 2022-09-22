package net.safety.alerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.safety.alerts.dto.FireEndpointDto;
import net.safety.alerts.exceptions.AddressNotFoundException;
import net.safety.alerts.service.FirestationService;

@RestController
public class FireController {
	
	@Autowired
	FirestationService firestationService;
	
	@GetMapping("/fire")
	public ResponseEntity<FireEndpointDto> fire(@RequestParam String address) throws AddressNotFoundException {
		return new ResponseEntity<>(firestationService.fire(address), HttpStatus.OK);
	}
}
