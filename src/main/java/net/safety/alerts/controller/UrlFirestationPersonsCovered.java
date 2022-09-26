package net.safety.alerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.safety.alerts.dto.StationNumberDto;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.service.FirestationService;

@RestController
public class UrlFirestationPersonsCovered {

	@Autowired
	private FirestationService firestationService;
	
	@GetMapping("/firestationPersonsCovered")
	public ResponseEntity<StationNumberDto> firestationPersonsCovered(@RequestParam Integer stationNumber) throws FirestationNotFoundException {
		return new ResponseEntity<>(firestationService.firestationPersonsCovered(stationNumber), HttpStatus.OK);
	}
}
