package net.safety.alerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.safety.alerts.dto.UrlFirestationCoverageDto;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.service.UrlService;

@RestController
public class UrlFirestationCoverageController {

	@Autowired
	private UrlService urlService;
	
	@GetMapping("/firestationCoverage")
	public ResponseEntity<UrlFirestationCoverageDto> firestationCoverage(@RequestParam Integer stationNumber) throws FirestationNotFoundException {
		return new ResponseEntity<>(urlService.urlFirestationCoverage(stationNumber), HttpStatus.OK);
	}
}
