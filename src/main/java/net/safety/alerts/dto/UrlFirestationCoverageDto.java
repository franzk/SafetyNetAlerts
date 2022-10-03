package net.safety.alerts.dto;

import java.util.List;

import lombok.Data;

/**
 * Provide the results of URL "/firestationCoverage" 
 * 
 * @author FranzKa
 *
 */
@Data
public class UrlFirestationCoverageDto {

	List<PersonDto> persons;
	Integer adultsCount;
	Integer childrenCount;
	
}
