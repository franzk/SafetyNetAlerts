package net.safety.alerts.exceptions;

/**
 * Thrown to indicate that no such firestation was found
 * 
 * @author FranzKa
 *
 */
@SuppressWarnings("serial")
public class FirestationNotFoundException extends Exception {

	public FirestationNotFoundException() {
		super("Firestation not found");
	}

	
}
