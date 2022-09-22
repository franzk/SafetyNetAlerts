package net.safety.alerts.dto;

import java.util.List;

import lombok.Data;

@Data
public class FireEndpointDto {
	
	List<FireEndpointPersonDto> persons; 
	
	Integer firestationNumber;

}
