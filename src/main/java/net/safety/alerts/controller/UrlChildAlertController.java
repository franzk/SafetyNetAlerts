package net.safety.alerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.safety.alerts.dto.UrlChildAlertDto;
import net.safety.alerts.exceptions.AddressNotFoundException;
import net.safety.alerts.service.UrlService;

/**
 * URL "{@code /childAlert?address=<address>}" <br><br>
 * 
 * "Cette url doit retourner une liste d'enfants (tout individu âgé de 18 ans ou
 * moins) habitant à cette adresse. La liste doit comprendre le prénom et le nom
 * de famille de chaque enfant, son âge et une liste des autres membres du
 * foyer. S'il n'y a pas d'enfant, cette url peut renvoyer une chaîne vide. "
 * 
 * @author FranzKa
 *
 */
@RestController
public class UrlChildAlertController {

	@Autowired
	private UrlService urlService;

	/**
	 * GET method of URL "/childAlert"
	 * 
	 * @param address
	 * @return ResponseEntity with {@link UrlChildAlertDto} and Http Status OK
	 * @throws AddressNotFoundException
	 */
	@GetMapping("childAlert")
	public ResponseEntity<UrlChildAlertDto> childAlert(@RequestParam String address) throws AddressNotFoundException {
		return new ResponseEntity<>(urlService.urlChildAlert(address), HttpStatus.OK);
	}

}
