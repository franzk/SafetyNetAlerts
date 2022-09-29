package net.safety.alerts.dto;

import java.util.List;

import lombok.Data;

@Data
public class UrlFloodStationsAddressDto {

	String address;
	List<PersonDto> inhabitants;
	
}
