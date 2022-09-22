package net.safety.alerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.safety.alerts.dto.ChildAlertDto;
import net.safety.alerts.service.PersonService;

@RestController
public class AlertController {
	
	@Autowired
	private PersonService personService;

	@GetMapping("childAlert")
	public ResponseEntity<ChildAlertDto> childAlert(@RequestParam String address) {
		return new ResponseEntity<>(personService.childAlert(address), HttpStatus.OK);
	}
	
}
