package net.safety.alerts.dto;

import java.util.List;

import lombok.Data;

/**
 * Provide the results of URL "/personInfo" 
 * 
 * @author FranzKa
 *
 */
@Data
public class UrlPersonInfoDto {

	List<PersonDto> persons;
	
}
