package net.safety.alerts.model;

import lombok.Data;

@Data
public class JsonData {

	Person[] persons;
	
	Firestation[] firestations;
	
	MedicalRecord[] medicalrecords;
	
}
