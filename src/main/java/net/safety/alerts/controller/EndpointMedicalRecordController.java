package net.safety.alerts.controller;

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
import net.safety.alerts.exceptions.MedicalRecordNotFoundException;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.service.MedicalRecordService;

/**
 * Endpoint "/medicalRecord" <br>
 * 
 * "Cet endpoint permettra d’effectuer les actions suivantes via Post/Put/Delete
 * HTTP : <br>
 * ● ajouter un dossier médical ; <br>
 * ● mettre à jour un dossier médical existant (comme évoqué précédemment,
 * supposer que le prénom et le nom de famille ne changent pas) ; <br>
 * ● supprimer un dossier médical (utilisez une combinaison de prénom et de nom
 * comme identificateur unique)."
 * 
 * @author FranzKa
 *
 */
@RestController
@RequestMapping("medicalRecord")
@Log4j2
public class EndpointMedicalRecordController {

	@Autowired
	MedicalRecordService medicalRecordService;

	/**
	 * POST method of URL "/medicalRecord"
	 * 
	 * @param medicalRecord
	 * @return ResponseEntity with MedicalRecord created and Http Status OK
	 */
	@PostMapping
	public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		log.info("MedicalRecord Controller POST Request start. Param medicalRecord = " + medicalRecord);
		ResponseEntity<MedicalRecord> result = new ResponseEntity<>(medicalRecordService.add(medicalRecord),
				HttpStatus.OK);
		log.info("MedicalRecord Controller POST Request result : " + result);
		return result;
	}

	/**
	 * GET method of URL "/medicalRecord"
	 * 
	 * @param firstName
	 * @param lastName
	 * @return ResponseEntity with MedicalRecord result and Http Status OK
	 * @throws MedicalRecordNotFoundException
	 */
	@GetMapping("")
	public ResponseEntity<MedicalRecord> getMedicalRecord(@RequestParam String firstName, @RequestParam String lastName)
			throws MedicalRecordNotFoundException {
		log.info("MedicalRecord Controller GET Request start. Param firstName = " + firstName + " / lastName = " + lastName);
		ResponseEntity<MedicalRecord> result = new ResponseEntity<>(
				medicalRecordService.getMedicalRecordByName(firstName, lastName), HttpStatus.OK);
		log.info("MedicalRecord Controller GET Request result : " + result);
		return result;
	}

	/**
	 * PUT method of URL "/medicalRecord"
	 * 
	 * @param medicalRecord
	 * @return ResponseEntity with updated MedicalRecord and Http Status OK
	 * @throws MedicalRecordNotFoundException
	 */
	@PutMapping("")
	public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord)
			throws MedicalRecordNotFoundException {
		log.info("MedicalRecord Controller PUT Request start. Param medicalRecord = " + medicalRecord);
		ResponseEntity<MedicalRecord> result = new ResponseEntity<>(medicalRecordService.update(medicalRecord),
				HttpStatus.OK);
		log.info("MedicalRecord Controller PUT Request result : " + result);
		return result;
	}

	/**
	 * DELETE method of URL "/medicalRecord"
	 * 
	 * @param firstName
	 * @param lastName
	 * @return ResponseEntity with success message and Http Status OK
	 * @throws MedicalRecordNotFoundException
	 */
	@DeleteMapping("")
	public ResponseEntity<String> deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName)
			throws MedicalRecordNotFoundException {
		log.info("MedicalRecord Controller DELETE Request start. Param firstName  " + firstName + " / lastName = " + lastName);
		medicalRecordService.deleteByName(firstName, lastName);
		ResponseEntity<String> result = ResponseEntity.status(HttpStatus.OK)
				.body("The Medical Record has been successfully deleted");
		log.info("MedicalRecord Controller PUT Request result : " + result);
		return result;

	}

}
