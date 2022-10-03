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
 * Handle endpoints of URL "/medicalRecord". Cover all CRUD methods.
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
		log.info("POST Request for MedicalRecord Controller.");
		return new ResponseEntity<>(medicalRecordService.add(medicalRecord), HttpStatus.OK);
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
		log.info("GET Request for MedicalRecord Controller.");
		return new ResponseEntity<>(medicalRecordService.getMedicalRecordByName(firstName, lastName), HttpStatus.OK);
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
		log.info("PUT Request for MedicalRecord Controller.");
		return new ResponseEntity<>(medicalRecordService.update(medicalRecord), HttpStatus.OK);
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
		log.info("DELETE Request for MedicalRecord Controller.");
		medicalRecordService.deleteByName(firstName, lastName);
		return ResponseEntity.status(HttpStatus.OK).body("The Medical Record has been successfully deleted");
	}

}
