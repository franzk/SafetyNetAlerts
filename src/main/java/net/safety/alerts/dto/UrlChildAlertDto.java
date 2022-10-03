package net.safety.alerts.dto;

import java.util.List;

import lombok.Data;

/**
 * Provide the results of URL "/childAlert"
 * 
 * @author FranzKa
 *
 */
@Data
public class UrlChildAlertDto {

	List<PersonDto> children;

	List<PersonDto> otherHouseHoldMembers;
}
