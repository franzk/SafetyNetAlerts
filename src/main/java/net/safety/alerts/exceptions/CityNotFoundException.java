package net.safety.alerts.exceptions;

/**
 * Thrown to indicate that no such city was found
 * 
 * @author FranzKa
 *
 */
@SuppressWarnings("serial")
public class CityNotFoundException extends Exception {

	public CityNotFoundException() {
		super("City not found");
	}
}
