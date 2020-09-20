package com.westmead.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.westmead.document.Admin;
import com.westmead.exception.LoginFailureException;
import com.westmead.exception.RegistrationFailureException;
import com.westmead.service.AdminService;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;
	
	@PostMapping("/register")
	public String registerUser(@RequestBody Admin admin) throws RegistrationFailureException {
		return this.adminService.saveAdmin(admin);
	}
	
	@PostMapping("/login")
	public Admin validateAdminUser(@RequestParam String emailId, @RequestParam String password) throws LoginFailureException {
		return this.adminService.validateAdmin(emailId, password);
	}
	
	@GetMapping("/getNonApproved")
	public List<Admin> getNonApprovedAdmins(){
		return this.adminService.getNonApprovedAdmins();
	}
	
	@PostMapping("/update")
	public boolean updateAdmin(@RequestBody Admin admin){
		return this.adminService.updateAdmin(admin);
	}
	
	@GetMapping
	public String test() {
		return "Admin service working";
	}
}
