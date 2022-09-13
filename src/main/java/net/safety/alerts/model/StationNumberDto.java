package net.safety.alerts.model;

import lombok.Data;

@Data
public class StationNumberDto {

	PersonDto[] persons;
	Integer adultsCount;
	Integer childrenCount;
	
}
