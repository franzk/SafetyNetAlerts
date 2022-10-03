package net.safety.alerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.safety.alerts.dto.UrlPersonInfoDto;
import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.service.UrlService;

/**
 * Handle "/personInfo" URL
 * 
 * @author FranzKa
 *
 */
@RestController
public class UrlPersonInfoController {

	@Autowired
	private UrlService urlService;

	/**
	 * GET method of URL "/personInfo"
	 * 
	 * @param firstName
	 * @param lastName
	 * @return ResponseEntity with {@link UrlPersonInfoDto} and Http Status OK
	 * @throws PersonNotFoundException
	 */
	@GetMapping("personInfo")
	public ResponseEntity<UrlPersonInfoDto> personInfo(@RequestParam String firstName, @RequestParam String lastName)
			throws PersonNotFoundException {
		return new ResponseEntity<>(urlService.urlPersonInfo(firstName, lastName), HttpStatus.OK);
	}

}
