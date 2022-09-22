package net.safety.alerts.dto;

import java.util.List;

import lombok.Data;

@Data
public class FireEndpointPersonDto {
	
	String firstName;
	
	String lastName;
	
	String phone;
	
	Integer age;
	
	List<String> medications;
	
	List<String> allergies;
	
}
