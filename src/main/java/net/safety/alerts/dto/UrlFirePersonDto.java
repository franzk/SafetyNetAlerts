package net.safety.alerts.dto;

import java.util.List;

import lombok.Data;

@Data
public class UrlFirePersonDto {
	
	String firstName;
	
	String lastName;
	
	String phone;
	
	Integer age;
	
	List<String> medications;
	
	List<String> allergies;
	
}
