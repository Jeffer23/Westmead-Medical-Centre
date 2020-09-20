package com.westmead.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westmead.document.Admin;
import com.westmead.document.AvailableTime;
import com.westmead.document.Doctor;
import com.westmead.exception.LoginFailureException;
import com.westmead.exception.RegistrationFailureException;
import com.westmead.repository.DoctorRepository;

@Service
public class DoctorService {

	private Logger logger = LoggerFactory.getLogger(DoctorService.class);
	
	private static final DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Autowired
	private DoctorRepository doctorRepo;

	public String saveDoctor(Doctor doctor) throws RegistrationFailureException {
		doctor.setEmailId(doctor.getEmailId().toLowerCase());
		Optional<Doctor> optionalUser = this.doctorRepo.findById(doctor.getEmailId());
		if (!optionalUser.isPresent()) {
			this.doctorRepo.save(doctor);
			return "Success";
		} else {
			logger.info("E-mail id already registered");
			throw new RegistrationFailureException("E-mail id already registered");
		}
	}

	public Doctor validateDoctor(String emailId, String password) throws LoginFailureException {
		logger.debug(emailId);
		logger.debug(password);
		Optional<Doctor> optionalUser = this.doctorRepo.findById(emailId.toLowerCase());
		if (optionalUser.isPresent()) {
			Doctor doctor = optionalUser.get();
			if (doctor.getApproval().getStatus() == null) {
				logger.info("Please wait for admin to approve");
				throw new LoginFailureException("Please wait for admin to approve");
			} else if (doctor.getApproval().getStatus().equals("rejected")) {
				logger.info("Contact Admin!! your approval has been rejected");
				throw new LoginFailureException("Contact Admin!! your approval has been rejected");
			} else if (doctor.getApproval().getStatus().equals("approved") && doctor.getPassword().equals(password)) {
				doctor.setPassword("");
				doctor.getAvailableTimes().entrySet().parallelStream().forEach(entry -> {
					if (entry.getKey().isBefore(LocalDate.now())) {
						doctor.getAvailableTimes().remove(entry.getKey());
					} 

				});
				return doctor;
			} else {
				logger.info("Password mismatch");
				throw new LoginFailureException("Password mismatch");
			}
		} else {
			logger.info("Email id is not registered");
			throw new LoginFailureException("Email id is not registered");
		}

	}

	public List<Doctor> getNonApprovedDoctors() {
		return this.doctorRepo.findNonApprovedDoctors();
	}

	public boolean approveDoctor(Doctor doctor) {
		Optional<Doctor> optionalUser = this.doctorRepo.findById(doctor.getEmailId().toLowerCase());
		if (optionalUser.isPresent()) {
			Doctor doctorObj = optionalUser.get();
			doctorObj.setApproval(doctor.getApproval());
			this.doctorRepo.save(doctorObj);
			return true;
		} else {
			return false;
		}
	}

	public List<Doctor> getApprovedDoctors() {
		List<Doctor> doctors = this.doctorRepo.findApprovedDoctors();
		doctors.parallelStream().forEach(doctor -> {
			doctor.getAvailableTimes().entrySet().parallelStream().forEach(entry -> {
				if (entry.getKey().isBefore(LocalDate.now())) {
					doctor.getAvailableTimes().remove(entry.getKey());
				} 

			});
		});
		return doctors;
	}

	public Doctor getDoctor(String emailId) throws NoSuchElementException{
		return this.doctorRepo.findById(emailId).get();
	}
	
	public Doctor updateDoctor(Doctor doctor) {
		return this.doctorRepo.save(doctor);
	}
	
	public Doctor addAvailableTime(String doctorId, String date, List<String> times) throws RuntimeException {
		logger.info("Doctor Id = " + doctorId);
		logger.info("Date = " + date);
		logger.info("Times = " + times);
		Doctor doctor = this.doctorRepo.findById(doctorId).get();
		LocalDate localDate = LocalDate.parse(date, DATEFORMATTER);
		
		Map<LocalDate, List<AvailableTime>> availableTimesMap = doctor.getAvailableTimes();
		List<AvailableTime> availableTimeList = availableTimesMap.get(localDate);
		if(availableTimeList == null) {
			availableTimeList =  new ArrayList<AvailableTime>();
			availableTimesMap.put(localDate, availableTimeList);
		}
		List<AvailableTime> availableTimes = availableTimeList;
		
		times.parallelStream().forEach(timeStr->{
			LocalTime localTime = LocalTime.parse(timeStr);
			if(!availableTimes.parallelStream().anyMatch(availableTimeObj->{
				return availableTimeObj.getTime().compareTo(localTime) == 0;
			})) {
				AvailableTime availableTime = new AvailableTime();
				availableTime.setTime(localTime);
				availableTimes.add(availableTime);
			} else {
				throw new RuntimeException(localTime.toString() + " is already added.");
			}
		});
		
		
		doctor = this.doctorRepo.save(doctor);
		doctor.setPassword("");
		
		return doctor;
	}
	
	public Doctor removeAvailableTime(String doctorId, String date, List<String> times) throws RuntimeException {
		logger.info("Doctor Id = " + doctorId);
		logger.info("Date = " + date);
		logger.info("Times = " + times);
		Doctor doctor = this.doctorRepo.findById(doctorId).get();
		LocalDate localDate = LocalDate.parse(date, DATEFORMATTER);
		
		Map<LocalDate, List<AvailableTime>> availableTimesMap = doctor.getAvailableTimes();
		if(availableTimesMap == null) {
			throw new RuntimeException("The doctor has no available time");
		}
		List<AvailableTime> availableTimeList = availableTimesMap.get(localDate);
		if(availableTimeList == null) {
			throw new RuntimeException("The doctor has no available time on " + date);
		}
		
		List<AvailableTime> availableTimes = availableTimeList;
		
		times.parallelStream().forEach(timeStr->{
			LocalTime localTime = LocalTime.parse(timeStr);
			if(availableTimes.parallelStream().anyMatch(availableTimeObj->{
				return availableTimeObj.getTime().compareTo(localTime) == 0;
			})) {
				List<AvailableTime> filteredAvailableTime = availableTimes.parallelStream().filter(availableTimeObj->{
					return availableTimeObj.getTime().compareTo(localTime) == 0;
				}).collect(Collectors.toList());
				if(filteredAvailableTime.size() == 1) {
					availableTimeList.remove(filteredAvailableTime.get(0));
				} else {
					logger.error("Filtered List has more times " + filteredAvailableTime);
				}
				
			} else {
				throw new RuntimeException(localTime.toString() + " is not added already.");
			}
		});
		
		
		doctor = this.doctorRepo.save(doctor);
		doctor.setPassword("");
		
		return doctor;
	}
	
}
