package net.safety.alerts.dto;

import java.util.List;

import lombok.Data;

@Data
public class UrlFirestationCoverageDto {

	List<PersonDto> persons;
	Integer adultsCount;
	Integer childrenCount;
	
}
