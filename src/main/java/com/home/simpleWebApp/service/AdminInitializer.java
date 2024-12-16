package com.home.simpleWebApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.home.simpleWebApp.config.AdminProperties;
import com.home.simpleWebApp.model.UserRole;
import com.home.simpleWebApp.model.Users;
import com.home.simpleWebApp.repository.UserRepository;

@Component
public class AdminInitializer implements CommandLineRunner {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AdminProperties adminProperties;
	
    @Override
    public void run(String... args) {
    	String username = adminProperties.getUsername();
        String password = adminProperties.getPassword();
        
        if (userRepository.findByUserNameAndRole(username, UserRole.ADMIN) == null) { // Add check to avoid duplication
            Users defaultAdmin = new Users();
            defaultAdmin.setUserName(username);
            defaultAdmin.setPassword(new BCryptPasswordEncoder(12).encode(password));
            defaultAdmin.setRole(UserRole.ADMIN);
            userRepository.save(defaultAdmin);
            System.out.println("Default admin created.");
        }
    }
}
