package net.safety.alerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.safety.alerts.dto.UrlFirestationDto;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.service.JoinedDataService;

@RestController
public class UrlPhoneAlert {

	@Autowired
	private JoinedDataService joinedDataService;
	
	@GetMapping("phoneAlert")
	public ResponseEntity<UrlFirestationDto> phoneAlert(@RequestParam Integer firestationNumber) throws FirestationNotFoundException {
		return new ResponseEntity<>(joinedDataService.urlPhoneAlert(firestationNumber), HttpStatus.OK);
	}
	
	
}
