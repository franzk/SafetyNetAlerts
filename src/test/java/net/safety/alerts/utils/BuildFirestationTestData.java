package net.safety.alerts.utils;

import java.util.ArrayList;
import java.util.List;

import net.safety.alerts.model.Firestation;

public class BuildFirestationTestData {

	public Firestation getFirestation(String addressModifier, Integer stationNumberModifier) {
		Firestation f = new Firestation();
		f.setAddress(TestConstants.address + addressModifier);
		f.setStation(TestConstants.stationNumber + stationNumberModifier);
		return f;
	}

	public Firestation getFirestation() {
		return getFirestation("", 0);
	}

	public List<Firestation> getFirestationList() {
		List<Firestation> listFirestations = new ArrayList<>();

		for (int i = 0; i < 24; i++) {
			listFirestations.add(getFirestation(Integer.toString(i), i));
		}
		return listFirestations;
	}

}
