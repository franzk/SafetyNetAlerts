package net.safety.alerts.dto;

import java.util.List;

import lombok.Data;

@Data
public class UrlFireDto {
	
	List<PersonDto> persons; 
	
	Integer firestationNumber;

}
