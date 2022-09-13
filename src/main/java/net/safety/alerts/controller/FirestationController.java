package net.safety.alerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.safety.alerts.dto.StationNumberDto;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.repository.JoinedDataRepository;
import net.safety.alerts.service.FirestationService;

@RestController
public class FirestationController {
	
	@Autowired
	private FirestationService firestationService;
	
	@Autowired
	private JoinedDataRepository joinedDataRepository;


	@GetMapping("/firestation")
	public StationNumberDto stationNumber(@RequestParam Integer stationNumber) throws FirestationNotFoundException {
		return joinedDataRepository.stationNumber(stationNumber);
	}
	
}
