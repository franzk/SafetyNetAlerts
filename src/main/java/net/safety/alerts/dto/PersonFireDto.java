package net.safety.alerts.dto;

import lombok.Data;

@Data
public class PersonFireDto {
	
	String firstName;
	
	String lastName;
	
	String phone;
	
	Integer age;
	
	String[] medications;
	
	String[] allergies;
	
}
