package net.safety.alerts.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class MedicalRecord {
	
	private String firstName;
	
	private String lastName;
	
	private LocalDate birthdate;
	
	private List<String> medications = new ArrayList<>();
	
	private List<String> allergies = new ArrayList<>();
	
	
}
