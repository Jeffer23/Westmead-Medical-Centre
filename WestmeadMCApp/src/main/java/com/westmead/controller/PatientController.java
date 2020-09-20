package com.westmead.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.westmead.document.Patient;
import com.westmead.exception.BookingAppointmentFailureException;
import com.westmead.exception.LoginFailureException;
import com.westmead.exception.RegistrationFailureException;
import com.westmead.service.PatientService;

@CrossOrigin
@RestController
@RequestMapping("/patient")
public class PatientController {

	private static final DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@Autowired
	private PatientService patientService;

	@PostMapping("/register")
	public String registerUser(@RequestBody Patient user) throws RegistrationFailureException {
		return this.patientService.savePatient(user);
	}

	@PostMapping("/login")
	public Patient validateUser(@RequestParam String emailId, @RequestParam String password)
			throws LoginFailureException {
		return this.patientService.validatePatient(emailId, password);
	}

	@PostMapping("/bookAppointment")
	public boolean bookAppointment(@RequestParam String patientId, @RequestParam String doctorId,
			@RequestParam String date, @RequestParam String time, @RequestParam String reason) throws BookingAppointmentFailureException {
		return this.patientService.bookAppointment(patientId, doctorId, LocalDate.parse(date, DATEFORMATTER), LocalTime.parse(time), reason);
	}

	@GetMapping
	public String test() {
		return "Patient service working";
	}
}
