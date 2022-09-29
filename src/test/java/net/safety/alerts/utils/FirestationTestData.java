package net.safety.alerts.utils;

import java.util.ArrayList;
import java.util.List;

import net.safety.alerts.model.Firestation;

public class FirestationTestData {

	public static Firestation buildFirestation(String address, Integer station) {
		Firestation f = new Firestation();
		f.setAddress(address);
		f.setStation(station);
		return f;
	}

	public static Firestation buildFirestation() {
		return buildFirestation(TestConstants.address, TestConstants.stationNumber);
	}

	public static List<Firestation> buildFirestationList() {
		List<Firestation> listFirestations = new ArrayList<>();

		for (int i = 0; i < 24; i++) {
			listFirestations.add(
					buildFirestation(TestConstants.address + Integer.toString(i), TestConstants.stationNumber + i));
		}
		return listFirestations;
	}

}
