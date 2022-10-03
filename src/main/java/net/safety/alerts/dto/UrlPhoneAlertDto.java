package net.safety.alerts.dto;

import java.util.Set;

import lombok.Data;

/**
 * Provide the results of URL "/phoneAlert" 
 * 
 * @author FranzKa
 *
 */
@Data
public class UrlPhoneAlertDto {

	Set<String> phoneNumbers;
	
}
