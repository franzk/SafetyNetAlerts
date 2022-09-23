package net.safety.alerts.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

	@ExceptionHandler(AddressNotFoundException.class)
	protected ResponseEntity<Object> handleAddressNotFound(AddressNotFoundException ex, WebRequest request) {

		String body = "Adress not found";

		return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);

	}

	@ExceptionHandler(FirestationNotFoundException.class)
	protected ResponseEntity<Object> handleFirestationNotFound(FirestationNotFoundException ex, WebRequest request) {

		String body = "Firestation not found";

		return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);		
		
	}
	
	@ExceptionHandler(MedicalRecordNotFoundException.class)
	protected ResponseEntity<Object> handleMedicalRecordNotFound(MedicalRecordNotFoundException ex, WebRequest request) {

		String body = "MedicalRecord not found";

		return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);		
		
	}

	@ExceptionHandler(PersonNotFoundException.class)
	protected ResponseEntity<Object> handleMedicalRecordNotFound(PersonNotFoundException ex, WebRequest request) {

		String body = "Person not found";

		return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);		
		
	}
	
}
