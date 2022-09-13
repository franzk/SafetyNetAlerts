package net.safety.alerts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import net.safety.alerts.dto.ChildAlertDto;
import net.safety.alerts.dto.StationNumberDto;
import net.safety.alerts.exceptions.FirestationNotFoundException;
import net.safety.alerts.model.Person;
import net.safety.alerts.repository.JoinedDataRepository;

@Service
public class JoinedDataService {

	@Autowired
	private JoinedDataRepository joinedDataRepository;
	
	public StationNumberDto stationNumber(@RequestParam Integer stationNumber) throws FirestationNotFoundException {
		return joinedDataRepository.stationNumber(stationNumber);
	}
	
	public ChildAlertDto getChildAlertResult(String address) {
		return joinedDataRepository.childAlert(address);
	}
	
}
