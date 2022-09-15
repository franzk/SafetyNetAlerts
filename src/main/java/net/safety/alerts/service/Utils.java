package net.safety.alerts.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

public class Utils {

	public static Date StringToDate(String strDate) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    df.setTimeZone(TimeZone.getTimeZone("UTC"));

	    return df.parse(strDate);
	}
	
	public static int calculateAge(Date birthday) {
		Period duration = Period.between(
				birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
				LocalDate.now());
		return duration.getYears();

	}
	
}
