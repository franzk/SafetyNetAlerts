package net.safety.alerts.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.model.Firestation;
import net.safety.alerts.service.FirestationService;

/**
 * Endpoint "/firestation" <br>
 * 
 * "Cet endpoint permettra d’effectuer les actions suivantes via Post/Put/Delete
 * avec HTTP :<br>
 * ● ajout d'un mapping caserne/adresse ;<br>
 * ● mettre à jour le numéro de la caserne de pompiers d'une adresse ; <br>
 * ● supprimer le mapping d'une caserne ou d'une adresse. "
 * 
 * @author FranzKa
 *
 */
@RestController
@RequestMapping("firestation")
@Log4j2
public class EndpointFirestationController {

	// private static final Logger logger =
	// LogManager.getLogger(AlertsApplication.class);

	@Autowired
	private FirestationService firestationService;

	/**
	 * POST method of URL "/firestation"
	 * 
	 * @param firestation
	 * @return ResponseEntity with Firestation created and Http Status OK
	 */
	@PostMapping("")
	public ResponseEntity<Firestation> addFirestation(@RequestBody Firestation firestation) {
		log.info("Firestation Controller POST Request start. Param firestation = " + firestation);
		ResponseEntity<Firestation> result = new ResponseEntity<>(firestationService.add(firestation), HttpStatus.OK);
		log.info("Firestation Controller POST Request result : " + result);
		return result;
	}

	/**
	 * GET method of URL "/firestation"
	 * 
	 * @param address
	 * @param stationNumber
	 * @return ResponseEntity with List of Firestation result and Http Status OK
	 * @throws FirestationNotFoundException
	 */
	@GetMapping("")
	public ResponseEntity<List<Firestation>> getFirestation(@RequestParam Optional<String> address,
			@RequestParam Optional<Integer> stationNumber) throws FirestationNotFoundException {

		log.info("Firestation Controller GET Request start. Param address = " + address + " / stationNumber = " + stationNumber);
		ResponseEntity<List<Firestation>> result = null;
		if (address.isPresent()) {
			result = new ResponseEntity<>(firestationService.getByAddress(address.get()), HttpStatus.OK);
		} else if (stationNumber.isPresent()) {
			result = new ResponseEntity<>(firestationService.getByStationNumber(stationNumber.get()), HttpStatus.OK);
		} else {
			throw new IllegalArgumentException();
		}
		log.info("Firestation Controller GET Request result : " + result);
		return result;
	}

	/**
	 * PUT method of URL "/firestation"
	 * 
	 * @param firestation
	 * @return ResponseEntity with updated Firestation and Http Status OK
	 * @throws FirestationNotFoundException
	 */
	@PutMapping("")
	public ResponseEntity<Firestation> updateFirestation(@RequestBody Firestation firestation)
			throws FirestationNotFoundException {
		log.info("Firestation Controller PUT Request start. Param firestation = " + firestation);
		ResponseEntity<Firestation> result = new ResponseEntity<>(firestationService.update(firestation),
				HttpStatus.OK);
		log.info("Firestation Controller PUT Request result : " + result);
		return result;
	}

	/**
	 * DELETE method of URL "/firestation"
	 * 
	 * @param address
	 * @param stationNumber
	 * @return ResponseEntity with a success message and Http Status OK
	 * @throws FirestationNotFoundException
	 */
	@DeleteMapping("")
	public ResponseEntity<String> deleteFirestations(@RequestParam Optional<String> address,
			@RequestParam Optional<Integer> stationNumber) throws FirestationNotFoundException {
		log.info("Firestation Controller DELETE Request start. Param address = " + address + " / stationNumber = " + stationNumber);
		String message = "";
		if (address.isPresent()) {
			firestationService.deleteByAddress(address.get());
			message = "Firestation deleted";
		} else if (stationNumber.isPresent()) {
			firestationService.deleteByStationNumber(stationNumber.get());
			message = "Firestation(s) deleted";
		} else {
			throw new IllegalArgumentException();
		}

		ResponseEntity<String> result = ResponseEntity.status(HttpStatus.OK).body(message);
		log.info("Firestation Controller DELETE Request result : " + result);
		return result;

	}
}
