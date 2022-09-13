package net.safety.alerts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.safety.alerts.dto.ChildAlertDto;
import net.safety.alerts.model.Person;
import net.safety.alerts.repository.JoinedDataRepository;
import net.safety.alerts.service.PersonService;

@RestController
public class PersonController {
	
	@Autowired
	private PersonService personService;
	
	@Autowired 
	private JoinedDataRepository joinedDataRepository;

	@GetMapping("/persons")
	public List<Person> getAll() {
		return personService.getAll();
	}
	
	@GetMapping("childAlert")
	public ChildAlertDto childAlert(@RequestParam String address) {
		return joinedDataRepository.childAlert(address);
	}
	
	
}
