package net.safety.alerts.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class PersonDto {

	String firstName;

	String lastName;

	String address;
	
	String city;
	
	String zip;

	String phone;
	
	String email;

	Integer age;

	List<String> medications;

	List<String> allergies;

}
