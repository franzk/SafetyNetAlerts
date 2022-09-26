package net.safety.alerts.dto;

import java.util.List;

import lombok.Data;

@Data
public class UrlPersonInfoPersonDto {
	
	String firstName;
	
	String lastName;
	
	String email;
	
	List<String> medications;
	
	List<String> allergies;
	
}
