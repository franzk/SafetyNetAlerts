package net.safety.alerts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.safety.alerts.dto.UrlFloodStationsDto;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.service.UrlService;

/**
 * Handle "/flood/stations" URL
 * 
 * @author FranzKa
 *
 */
@RestController
public class UrlFloodStationsController {

	@Autowired
	private UrlService urlService;

	/**
	 * GET method of URL "flood/stations"
	 * 
	 * @param stations
	 * @return ResponseEntity with {@link UrlFloodStationsDto} and Http Status OK
	 * @throws FirestationNotFoundException
	 */
	@GetMapping("flood/stations")
	public ResponseEntity<UrlFloodStationsDto> floodStations(@RequestParam List<Integer> stations)
			throws FirestationNotFoundException {
		return new ResponseEntity<>(urlService.urlFloodStations(stations), HttpStatus.OK);
	}

}
