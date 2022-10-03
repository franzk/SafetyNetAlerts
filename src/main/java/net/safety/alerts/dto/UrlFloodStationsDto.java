package net.safety.alerts.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Provide the results of URL "/flood/stations" 
 * 
 * @author FranzKa
 *
 */
@Data
public class UrlFloodStationsDto {

	List<UrlFloodStationsAddressDto> addresses = new ArrayList<>();
		
}
