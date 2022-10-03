package net.safety.alerts.exceptions;

/**
 * Thrown to indicate that no such address was found
 * 
 * @author FranzKa
 *
 */
@SuppressWarnings("serial")
public class AddressNotFoundException extends Exception {

	public AddressNotFoundException() {
		super("Adress not found");
	}

}
