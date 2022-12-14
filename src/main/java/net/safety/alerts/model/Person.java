package net.safety.alerts.model;

import lombok.Data;

/**
 * Person Model
 * 
 * @author FranzKa
 *
 */
@Data
public class Person {
	
	private String firstName;
	
	private String lastName;
	
	private String address;
	
	private String city;

	private String zip;
		
	private String phone;
	
	private String email;
	
}
