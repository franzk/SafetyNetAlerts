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

/**
 * Handle "/firestationCoverage" URL
 * 
 * @author FranzKa
 *
 */
@RestController
public class UrlFirestationCoverageController {

	@Autowired
	private UrlService urlService;

	/**
	 * GET method of URL "/firestationCoverage"
	 * 
	 * @param stationNumber
	 * @return ResponseEntity with {@link UrlFirestationCoverageDto} and Http Status
	 *         OK
	 * @throws FirestationNotFoundException
	 */
	@GetMapping("/firestationCoverage")
	public ResponseEntity<UrlFirestationCoverageDto> firestationCoverage(@RequestParam Integer stationNumber)
			throws FirestationNotFoundException {
		return new ResponseEntity<>(urlService.urlFirestationCoverage(stationNumber), HttpStatus.OK);
	}
}
