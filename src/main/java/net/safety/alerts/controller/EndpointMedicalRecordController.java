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

import net.safety.alerts.exceptions.MedicalRecordNotFoundException;
import net.safety.alerts.model.MedicalRecord;
import net.safety.alerts.service.MedicalRecordService;

@RestController
@RequestMapping
public class EndpointMedicalRecordController {

	@Autowired
	MedicalRecordService medicalRecordService;

	// create
	@PostMapping
	public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		return new ResponseEntity<>(medicalRecordService.add(medicalRecord), HttpStatus.OK);
	}

	// read
	@GetMapping("")
	public ResponseEntity<MedicalRecord> getMedicalRecord(@RequestParam String firstName, @RequestParam String lastName)
			throws MedicalRecordNotFoundException {		
		return new ResponseEntity<>(medicalRecordService.getMedicalRecordByName(firstName, lastName), HttpStatus.OK);
	}

	// update
	@PutMapping("")
	public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord)
			throws MedicalRecordNotFoundException {
		return new ResponseEntity<>(medicalRecordService.update(medicalRecord), HttpStatus.OK);
	}

	// delete
	@DeleteMapping("")
	public ResponseEntity<String> deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName)
			throws MedicalRecordNotFoundException {
		medicalRecordService.deleteByName(firstName, lastName);
		return ResponseEntity.status(HttpStatus.OK).body("The Medical Record has been successfully deleted");
	}

}
