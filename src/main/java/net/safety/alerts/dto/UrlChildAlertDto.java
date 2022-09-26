package net.safety.alerts.dto;

import java.util.List;

import lombok.Data;

@Data
public class UrlChildAlertDto {
	
	List<ChildDto> children;
	
	List<PersonNameDto> otherHouseHoldMembers;
	
}
