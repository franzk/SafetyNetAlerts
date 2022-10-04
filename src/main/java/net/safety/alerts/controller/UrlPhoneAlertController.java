package net.safety.alerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;
import net.safety.alerts.dto.UrlPhoneAlertDto;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.service.UrlService;

/**
 * URL "{@code /phoneAlert?firestation=<firestation_number>}" <br>
 * <br>
 * 
 * "Cette url doit retourner une liste des numéros de téléphone des résidents
 * desservis par la caserne de pompiers. Nous l'utiliserons pour envoyer des
 * messages texte d'urgence à des foyers spécifiques"
 * 
 * @author FranzKa
 *
 */
@RestController
@Log4j2
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
		log.info("URL phoneAlert start. Param firestationNumber = " + firestationNumber);
		ResponseEntity<UrlPhoneAlertDto> result = new ResponseEntity<>(urlService.urlPhoneAlert(firestationNumber),
				HttpStatus.OK);
		log.info("URL phoneAlert result : " + result);
		return result;

	}

}
