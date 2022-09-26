package net.safety.alerts.exceptions;

@SuppressWarnings("serial")
public class CityNotFoundException extends Exception {

	public CityNotFoundException() {
		super("City not found");
	}
}
