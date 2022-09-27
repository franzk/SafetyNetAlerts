package net.safety.alerts.utils;

import java.time.LocalDate;
import java.util.List;

public class TestConstants {

	
	public static final String firstName = "Angus";
	public static final String lastName = "Young";
	public static final String address = "666, Highway to hell";
	public static final String city = "Sydney";
	public static final String zip = "12345";
	public static final String phone = "012456789";
	public static final String email = "a@b.com";
	
	public static final Integer stationNumber = 42;

	public static final LocalDate birthdate = Utils.StringToDate("12/14/2021");
	public static final List<String> medications = List.of(new String[] {"Doliprane", "Vogalene"});
	public static final List<String> allergies = List.of(new String[] {"Pollen", "Arachides"});
	
}
