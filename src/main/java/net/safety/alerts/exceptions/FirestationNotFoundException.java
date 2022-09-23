package net.safety.alerts.exceptions;

@SuppressWarnings("serial")
public class FirestationNotFoundException extends Exception {

	public FirestationNotFoundException() {
		super("Firestation not found");
	}

	
}
