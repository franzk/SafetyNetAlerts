package net.safety.alerts.exceptions;

@SuppressWarnings("serial")
public class AddressNotFoundException extends Exception {

	public AddressNotFoundException() {
		super("Adress not found");
	}

}
