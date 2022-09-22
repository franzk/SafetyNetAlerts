package net.safety.alerts.dto;

import java.util.List;

import lombok.Data;

@Data
public class ChildAlertDto {
	
	List<ChildDto> children;
	
	List<PersonNameDto> otherHouseHoldMembers;
	
}
