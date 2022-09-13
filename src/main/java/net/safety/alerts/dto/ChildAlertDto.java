package net.safety.alerts.dto;

import lombok.Data;

@Data
public class ChildAlertDto {
	
	ChildDto[] children;
	
	PersonNameDto[] otherHouseHoldMembers;
	
}
