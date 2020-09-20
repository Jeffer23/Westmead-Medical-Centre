package com.westmead;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.westmead.document.AvailableTime;
import com.westmead.document.Doctor;

@SpringBootApplication
public class WestmeadMcApplication {

	public static void main(String[] args) {
		SpringApplication.run(WestmeadMcApplication.class, args);
		
		/*Map<LocalDate, List<AvailableTime>> availableTimes =  new ConcurrentHashMap<LocalDate, List<AvailableTime>>();
		for(int d=1, m=9 ; d<31 && m<11; d++) {
			LocalDate date = LocalDate.of(2020, m, d);
			List<AvailableTime> times = new ArrayList<AvailableTime>();
			
			for(int h=8, min=0; h<16 && min<60; min++) {
				
				if(min % 20 == 0) {
					AvailableTime time = new AvailableTime();
					time.setTime(LocalTime.of(h, min));
					times.add(time);
				}
				if(min == 59) {
					h++;
					min = -1;
				}
			}
			availableTimes.put(date, times);
			if(d == 30) {
				m++;
				d = 0;
			}
		}
		
		Doctor doctor = new Doctor();
		doctor.setAvailableTimes(availableTimes);
		
		//this.doctorRepo.save(doctors.get(0));
		System.out.println("Updated Times");*/
	}

}
