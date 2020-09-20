package com.westmead.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westmead.document.Appointment;
import com.westmead.document.Doctor;
import com.westmead.document.Patient;
import com.westmead.exception.BookingAppointmentFailureException;
import com.westmead.exception.LoginFailureException;
import com.westmead.exception.RegistrationFailureException;
import com.westmead.repository.AppointmentRepository;
import com.westmead.repository.PatientRepository;

@Service
public class PatientService {

	Logger logger = LoggerFactory.getLogger(PatientService.class);

	@Autowired
	private PatientRepository patientRepo;

	@Autowired
	private AppointmentRepository appointmentRepo;

	@Autowired
	private DoctorService doctorService;

	public String savePatient(Patient user) throws RegistrationFailureException {
		user.setEmailId(user.getEmailId().toLowerCase());
		Optional<Patient> optionalUser = this.patientRepo.findById(user.getEmailId());
		if (!optionalUser.isPresent()) {
			this.patientRepo.save(user);
			return "Success";
		} else {
			logger.info("E-mail id already registered");
			throw new RegistrationFailureException("E-mail id already registered");
		}
	}

	public Patient validatePatient(String emailId, String password) throws LoginFailureException {
		System.out.println(emailId);
		System.out.println(password);
		Optional<Patient> optionalUser = this.patientRepo.findById(emailId.toLowerCase());
		if (optionalUser.isPresent()) {
			Patient user = optionalUser.get();
			if (user.getPassword().equals(password)) {
				user.setPassword("");
				return user;
			} else {
				logger.info("Password mismatch");
				throw new LoginFailureException("Password mismatch");
			}
		} else {
			logger.info("Email id is not registered");
			throw new LoginFailureException("Email id is not registered");
		}
	}

	public boolean bookAppointment(String patientId, String doctorId, LocalDate date, LocalTime time, String reason)
			throws BookingAppointmentFailureException {
		Doctor doctor = null;
		try {
			doctor = this.doctorService.getDoctor(doctorId);
		} catch (NoSuchElementException e) {
			throw new BookingAppointmentFailureException("Invalid Doctor Id");
		}

		Optional<Patient> optionalPatient = this.patientRepo.findById(patientId);
		if (!optionalPatient.isPresent()) {
			throw new BookingAppointmentFailureException("Invalid patient Id");
		}
		LocalDateTime dateTime = LocalDateTime.of(date, time);
		Appointment appointment = new Appointment();
		appointment.setDoctorId(doctorId);
		appointment.setPatientId(patientId);
		appointment.setReason(reason);
		appointment.setBookedBy("self");
		appointment.setAppointmentTime(dateTime);
		appointment.setAppointmentId(UUID.randomUUID().toString());

		Patient patient = optionalPatient.get();
		patient.getAppointmentIds().add(appointment.getAppointmentId());

		doctor.getAvailableTimes().get(date).parallelStream().forEach(availableTime -> {
			if (availableTime.getTime().compareTo(time) == 0) {
				if (availableTime.getAppointmentId() != null)
					throw new RuntimeException("The selected time is not available");
				availableTime.setAppointmentId(appointment.getAppointmentId());
			}
		});

		try {
			this.appointmentRepo.save(appointment);
			this.patientRepo.save(patient);
			this.doctorService.updateDoctor(doctor);
			return true;
		} catch (Exception e) {
			logger.error("Error during appointment booking", e);
			throw new BookingAppointmentFailureException("Booking Failed");
		}

	}

}
