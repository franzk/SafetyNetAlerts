package net.safety.alerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
	public StationNumberDto stationNumber(@RequestParam Integer stationNumber) throws FirestationNotFoundException {
		return joinedDataRepository.stationNumber(stationNumber);
	}

	@GetMapping("/fire")
	public FireDto fire(@RequestParam String address) throws AddressNotFoundException {
		return joinedDataRepository.fire(address);
	}

}
