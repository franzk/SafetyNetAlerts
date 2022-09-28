package net.safety.alerts.utils;

import java.util.ArrayList;
import java.util.List;

import net.safety.alerts.model.Firestation;

public class FirestationTestData {

	public static Firestation buildFirestation(String addressModifier, Integer stationNumberModifier) {
		Firestation f = new Firestation();
		f.setAddress(TestConstants.address + addressModifier);
		f.setStation(TestConstants.stationNumber + stationNumberModifier);
		return f;
	}

	public static Firestation buildFirestation() {
		return buildFirestation("", 0);
	}

	public static List<Firestation> buildFirestationList() {
		List<Firestation> listFirestations = new ArrayList<>();

		for (int i = 0; i < 24; i++) {
			listFirestations.add(buildFirestation(Integer.toString(i), i));
		}
		return listFirestations;
	}

}
