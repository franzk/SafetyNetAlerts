package net.safety.alerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.safety.alerts.dto.UrlPersonInfoDto;
import net.safety.alerts.exceptions.PersonNotFoundException;
import net.safety.alerts.service.JoinedDataService;

@RestController
public class UrlPersonInfoController {

	@Autowired
	private JoinedDataService joinedDataService;
	
	@GetMapping("personInfo")
	public ResponseEntity<UrlPersonInfoDto> urlPersonInfo(@RequestParam String firstName, @RequestParam String lastName) throws PersonNotFoundException {
		return new ResponseEntity<>(joinedDataService.getPersonInfoByName(firstName, lastName), HttpStatus.OK);
	}
	
}
