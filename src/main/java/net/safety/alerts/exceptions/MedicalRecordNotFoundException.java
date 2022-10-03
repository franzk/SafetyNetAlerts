package net.safety.alerts.exceptions;

/**
 * Thrown to indicate that no such Medical Record was found
 * 
 * @author FranzKa
 *
 */
@SuppressWarnings("serial")
public class MedicalRecordNotFoundException extends Exception {

	public MedicalRecordNotFoundException() {
		super("MerdicalRecord not found");
	}

}
