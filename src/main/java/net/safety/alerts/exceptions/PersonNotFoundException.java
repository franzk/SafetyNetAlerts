package net.safety.alerts.exceptions;

@SuppressWarnings("serial")
public class PersonNotFoundException extends Exception {

	public PersonNotFoundException() {
		super("Person not found");
	}

}
