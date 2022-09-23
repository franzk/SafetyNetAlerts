package net.safety.alerts.exceptions;

@SuppressWarnings("serial")
public class MedicalRecordNotFoundException extends Exception {

	public MedicalRecordNotFoundException() {
		super("MerdicalRecord not found");
	}

}
