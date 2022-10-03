package net.safety.alerts.dto;

import java.util.List;

import lombok.Data;

/**
 * Provide the results of URL "/communityEmail"
 * 
 * @author FranzKa
 *
 */
@Data
public class UrlCommunityEmailDto {

	List<String> emails;
	
}
