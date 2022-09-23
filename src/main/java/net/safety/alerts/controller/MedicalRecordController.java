package net.safety.alerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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
public class MedicalRecordController {

	@Autowired
	MedicalRecordService medicalRecordService;
	
	// create
	@PostMapping
	public MedicalRecord addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		return medicalRecordService.add(medicalRecord);
	}
	
	// update
	@PutMapping("")
	public MedicalRecord updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws MedicalRecordNotFoundException {
		return medicalRecordService.update(medicalRecord);
	}
	
	// delete
	@DeleteMapping("")
	public void deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName) throws MedicalRecordNotFoundException {
		medicalRecordService.deleteByName(firstName, lastName);
	}
	
}
