package net.safety.alerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;
import net.safety.alerts.dto.UrlPersonInfoDto;
import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.service.UrlService;

/**
 * URL "{@code /personInfo?firstName=<firstName>&lastName=<lastName>}" <br>
 * <br>
 * 
 * "Cette url doit retourner le nom, l'adresse, l'âge, l'adresse mail et les
 * antécédents médicaux (médicaments, posologie, allergies) de chaque habitant.
 * Si plusieurs personnes portent le même nom, elles doivent toutes apparaître."
 * 
 * 
 * @author FranzKa
 *
 */
@RestController
@Log4j2
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
		log.info("URL personInfo start. Param firstName = " + firstName + " / lastName = " + lastName);
		ResponseEntity<UrlPersonInfoDto> result = new ResponseEntity<>(urlService.urlPersonInfo(firstName, lastName),
				HttpStatus.OK);
		log.info("URL personInfo result : " + result);
		return result;
	}

}
