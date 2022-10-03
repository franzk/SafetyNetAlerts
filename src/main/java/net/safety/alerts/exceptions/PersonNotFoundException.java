package net.safety.alerts.exceptions;

/**
 * Thrown to indicate that no such person was found
 * 
 * @author FranzKa
 *
 */
@SuppressWarnings("serial")
public class PersonNotFoundException extends Exception {

	public PersonNotFoundException() {
		super("Person not found");
	}

}
