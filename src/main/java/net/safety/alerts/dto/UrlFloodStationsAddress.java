package net.safety.alerts.dto;

import java.util.List;

import lombok.Data;

@Data
public class UrlFloodStationsAddress {

	String address;
	List<PersonDto> inhabitants;
	
}
