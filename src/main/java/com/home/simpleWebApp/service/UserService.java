package com.home.simpleWebApp.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.simpleWebApp.dto.TokenDTO;
import com.home.simpleWebApp.model.Users;
import com.home.simpleWebApp.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserService {
	
	@Autowired
    private UserRepository customerRepository;
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	ApplicationContext context;
	
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
	 @Autowired
	 AuthenticationManager authManager;
	
	// Create a new customer
    public Users createCustomer(Users customer) {
    	customer.setPassword(encoder.encode(customer.getPassword()));
        return customerRepository.save(customer);
    }
    
    // Read a customer by ID
    public Optional<Users> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }
    
    // Read a customer by UserName
    public Optional<Users> getCustomerByUserName(String username) {
    	return customerRepository.findByUserName(username);
    }
    
    // Read all customers
    public List<Users> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    // Update a customer by ID
    public Users updateCustomer(Long id, Users customerDetails) {
        return customerRepository.findById(id)
            .map(customer -> {
                customer.setUserName(customerDetails.getUserName());
                customer.setAddress(customerDetails.getAddress());
                customer.setPhoneNumber(customerDetails.getPhoneNumber());
                customer.setPassword(encoder.encode(customer.getPassword()));
                return customerRepository.save(customer);
            })
            .orElseThrow(() -> new RuntimeException("Customer not found with id " + id));
    }
    
    // Delete a customer by ID
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
    
    // .. Update password service

	public Map<String, String> verify(Users user) {
		Authentication authentication = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));

		Map<String, String> tokens = new HashMap<>();
		if (authentication.isAuthenticated()) {
	        tokens.put("accessToken", jwtService.generateAccessToken(user.getUserName()));
	        tokens.put("refreshToken", jwtService.generateRefreshToken(user.getUserName()));
		}
		return tokens;
	}


	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		String refreshToken = null;
		String username = null;
		
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}
		refreshToken = authHeader.substring(7);
		username = jwtService.extractUserName(refreshToken);
		
		if (username != null) {
			UserDetails userDetails = context.getBean(CustomUserDetailsService.class).loadUserByUsername(username);
			// Or revoke old refresh token here ... if using DB
			System.out.println("USER DETAILS...");
			System.out.println(userDetails);
			if (jwtService.validateToken(refreshToken, userDetails, "refresh")) {
				String accessToken = jwtService.generateAccessToken(userDetails.getUsername());
				var authResponse = TokenDTO.builder()
						.accessToken(accessToken)
						.refreshToken(refreshToken)
						.build();
				new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
			} else {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "{\"message\": \"Invalid token provided\"}");
			}
		}
	}
    
}
