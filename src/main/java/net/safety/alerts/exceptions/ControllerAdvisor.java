package net.safety.alerts.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.log4j.Log4j2;

/**
 * Handle Exceptions to convert them in http response
 * 
 * @author FranzKa
 *
 */
@ControllerAdvice
@Log4j2
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

	@ExceptionHandler(AddressNotFoundException.class)
	protected ResponseEntity<Object> handleAddressNotFound(AddressNotFoundException ex, WebRequest request) {
		log.error("Error : AddressNotFoundException was thrown !");

		String body = "Adress not found";

		return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);

	}

	@ExceptionHandler(FirestationNotFoundException.class)
	protected ResponseEntity<Object> handleFirestationNotFound(FirestationNotFoundException ex, WebRequest request) {
		log.error("Error : FirestationNotFoundException was thrown !");

		String body = "Firestation not found";

		return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);

	}

	@ExceptionHandler(MedicalRecordNotFoundException.class)
	protected ResponseEntity<Object> handleMedicalRecordNotFound(MedicalRecordNotFoundException ex,
			WebRequest request) {
		log.error("Error : MedicalRecordNotFoundException was thrown !");

		String body = "MedicalRecord not found";

		return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);

	}

	@ExceptionHandler(PersonNotFoundException.class)
	protected ResponseEntity<Object> handlePersonNotFound(PersonNotFoundException ex, WebRequest request) {
		log.error("Error : PersonNotFoundException was thrown !");

		String body = "Person not found";

		return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);

	}

	@ExceptionHandler(IllegalArgumentException.class)
	protected ResponseEntity<Object> handleMedicalRecordNotFound(IllegalArgumentException ex, WebRequest request) {
		log.error("Error : IllegalArgumentException was thrown !");

		String body = "Wrong parameters";

		return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

	}

	@ExceptionHandler(CityNotFoundException.class)
	protected ResponseEntity<Object> handleCityNotFound(CityNotFoundException ex, WebRequest request) {
		log.error("Error : CityNotFoundException was thrown !");

		String body = "City not found";

		return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);

	}

}
