package net.safety.alerts.dto;

import java.util.List;

import lombok.Data;

@Data
public class ChildAlertDto {
	
	ChildDto[] children;
	
	PersonNameDto[] otherHouseHoldMembers;
	
}
