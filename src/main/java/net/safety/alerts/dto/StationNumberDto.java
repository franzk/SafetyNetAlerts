package net.safety.alerts.dto;

import lombok.Data;

@Data
public class StationNumberDto {

	PersonDto[] persons;
	Integer adultsCount;
	Integer childrenCount;
	
}
