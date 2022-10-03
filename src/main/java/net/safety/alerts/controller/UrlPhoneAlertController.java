package net.safety.alerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.safety.alerts.dto.UrlPhoneAlertDto;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.service.UrlService;

/**
 * Handle "/phoneAlert" URL
 * 
 * @author FranzKa
 *
 */
@RestController
public class UrlPhoneAlertController {

	@Autowired
	private UrlService urlService;

	/**
	 * GET method of URL "/phoneAlert"
	 * 
	 * @param firestationNumber
	 * @return ResponseEntity with {@link UrlPhoneAlertDto} and Http Status OK
	 * @throws FirestationNotFoundException
	 */
	@GetMapping("phoneAlert")
	public ResponseEntity<UrlPhoneAlertDto> phoneAlert(@RequestParam Integer firestationNumber)
			throws FirestationNotFoundException {
		return new ResponseEntity<>(urlService.urlPhoneAlert(firestationNumber), HttpStatus.OK);
	}

}
