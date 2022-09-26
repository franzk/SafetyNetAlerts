package net.safety.alerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.safety.alerts.dto.UrlCommunityEmailDto;
import net.safety.alerts.exceptions.CityNotFoundException;
import net.safety.alerts.service.UrlService;

@RestController
public class UrlCommunityEmailController {

	@Autowired
	UrlService urlService;

	@GetMapping("communityEmail")
	public ResponseEntity<UrlCommunityEmailDto>  communityEmail(@RequestParam String city) throws CityNotFoundException {
		return new ResponseEntity<>(urlService.urlCommunityEmail(city), HttpStatus.OK);
	}
	
}
