package net.safety.alerts.dto;

import lombok.Data;

@Data
public class FireDto {
	
	PersonFireDto[] persons;
	
	Integer firestationNumber;

}
