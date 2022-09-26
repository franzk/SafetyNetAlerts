package net.safety.alerts.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class UrlFloodStationsDto {

	List<UrlFloodStationsAddress> addresses = new ArrayList<>();
		
}
