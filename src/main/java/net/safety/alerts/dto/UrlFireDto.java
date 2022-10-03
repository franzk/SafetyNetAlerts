package net.safety.alerts.dto;

import java.util.List;

import lombok.Data;

/**
 * Provide the results of URL "/fire"
 * 
 * @author FranzKa
 *
 */
@Data
public class UrlFireDto {
	
	List<PersonDto> persons; 
	
	Integer firestationNumber;

}
