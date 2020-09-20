package com.westmead.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.westmead.exception.BookingAppointmentFailureException;
import com.westmead.exception.LoginFailureException;
import com.westmead.exception.RegistrationFailureException;

@RestControllerAdvice
public class WestmeadRestExceptionHandler {

	@ExceptionHandler(LoginFailureException.class)
	public ResponseEntity<String> handleLoginFailureException(LoginFailureException exception) {
		return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(RegistrationFailureException.class)
	public ResponseEntity<String> handleRegistrationFailureException(RegistrationFailureException exception) {
		return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(BookingAppointmentFailureException.class)
	public ResponseEntity<String> handleBookingAppointmentFailureException(BookingAppointmentFailureException exception) {
		return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Throwable.class)
	public ResponseEntity<String> handleThrowable(Throwable exception) {
		return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	
}
