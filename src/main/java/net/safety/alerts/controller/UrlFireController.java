package net.safety.alerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;
import net.safety.alerts.dto.UrlFireDto;
import net.safety.alerts.exceptions.AddressNotFoundException;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.service.UrlService;

/**
 * URL "{@code /fire?address=<address>}" <br><br>
 * 
 * "Cette url doit retourner la liste des habitants vivant à l’adresse donnée
 * ainsi que le numéro de la caserne de pompiers la desservant. La liste doit
 * inclure le nom, le numéro de téléphone, l'âge et les antécédents médicaux
 * (médicaments, posologie et allergies) de chaque personne. "
 * 
 * @author FranzKa
 *
 */
@RestController
@Log4j2
public class UrlFireController {

	@Autowired
	UrlService urlService;

	/**
	 * GET method of URL "/fire"
	 * 
	 * @param address
	 * @return ResponseEntity with {@link UrlFireDto} and Http Status OK
	 * @throws AddressNotFoundException
	 * @throws FirestationNotFoundException
	 */
	@GetMapping("/fire")
	public ResponseEntity<UrlFireDto> fire(@RequestParam String address)
			throws AddressNotFoundException, FirestationNotFoundException {
		log.info("URL fire start. Param address = " + address);
		ResponseEntity<UrlFireDto> result = new ResponseEntity<>(urlService.urlFire(address), HttpStatus.OK);
		log.info("URL fire result : " + result);
		return result;
	}
}
