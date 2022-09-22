package net.safety.alerts.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Utils {

	public static LocalDate StringToDate(String strDate) throws DateTimeParseException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		return LocalDate.parse(strDate, formatter);
	}

	public static int calculateAge(LocalDate birthdate) {
		Period duration = Period.between(birthdate, LocalDate.now());
		return duration.getYears();

	}

}
