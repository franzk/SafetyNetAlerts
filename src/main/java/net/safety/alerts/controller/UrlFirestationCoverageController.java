package net.safety.alerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;
import net.safety.alerts.dto.UrlFirestationCoverageDto;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.service.UrlService;

/**
 * URL "{@code /firestationCoverage?stationNumber=<station_number>}" <br>
 * <br>
 * 
 * "Cette url doit retourner une liste des personnes couvertes par la caserne de
 * pompiers correspondante. Donc, si le numéro de station = 1, elle doit
 * renvoyer les habitants couverts par la station numéro 1. La liste doit
 * inclure les informations spécifiques suivantes : prénom, nom, adresse, numéro
 * de téléphone. De plus, elle doit fournir un décompte du nombre d'adultes et
 * du nombre d'enfants (tout individu âgé de 18 ans ou moins) dans la zone
 * desservie"
 * 
 * @author FranzKa
 *
 */
@RestController
@Log4j2
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
		log.info("URL firestationCoverage start. Param stationNumber = " + stationNumber);
		ResponseEntity<UrlFirestationCoverageDto> result = new ResponseEntity<>(
				urlService.urlFirestationCoverage(stationNumber), HttpStatus.OK);
		log.info("URL firestationCoverage result : " + result);
		return result;
	}
}
