package com.home.simpleWebApp.controller;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.home.simpleWebApp.dto.CustomerDashboardDTO;
import com.home.simpleWebApp.dto.CustomerRequestDTO;
import com.home.simpleWebApp.dto.CustomerResponseDTO;
import com.home.simpleWebApp.model.UserRole;
import com.home.simpleWebApp.model.Users;
import com.home.simpleWebApp.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
	
	@Autowired
    private UserService customerService;
	
    // Customer Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {
    	Map<String, String> tokens = customerService.verify(user);
    	if (tokens.isEmpty()) {
    		return new ResponseEntity<>("{\"message\": \"Failed to authenticate User\"}", HttpStatus.UNAUTHORIZED);
    	}
    	// Get User
    	Users loggedInUser = customerService.getCustomerByUserName(user.getUserName())
    			.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "{\"message\": \"Invalid username\"}"));
    	
    	Map<String, Object> response = new HashMap<>();
    	response.put("user", CustomerResponseDTO.fromUser(loggedInUser).toHashMap());
    	response.put("tokens", tokens);
    	return ResponseEntity.ok(response);
    }
    
    // Refresh	-> TODO: Move it into some AuthController...
    @PostMapping("/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	customerService.refreshToken(request, response);
    }
    
    // Logout	-> TODO: Move it into some AuthController...
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
    	// TODO: Invalidate auth tokens...
        return ResponseEntity.ok("\"message\": \"Logged out successfully\"");
    }
	
    // Create a new customer
    @PostMapping("/register")
    public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody CustomerRequestDTO customerDTO) {
    	Users customer = Users.builder()
    						.userName(customerDTO.getUserName())
    		                .address(customerDTO.getAddress())
    		                .phoneNumber(customerDTO.getPhoneNumber())
    		                .age(customerDTO.getAge())
    		                .gender(customerDTO.getGender())
    		                .password(customerDTO.getPassword())
    		                .role(UserRole.CUSTOMER)
    						.build();
        Users createdCustomer = customerService.createCustomer(customer);
        return new ResponseEntity<>(CustomerResponseDTO.fromUser(createdCustomer), HttpStatus.CREATED);
    }
    
    // Get a customer by ID
    @GetMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.userId")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long id, Authentication authentication) {
        Optional<Users> customer = customerService.getCustomerById(id);
        return customer.map(cust -> ResponseEntity.ok(CustomerResponseDTO.fromUser(cust)))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    
    // Get all customers
    @GetMapping
    public ResponseEntity<List<CustomerDashboardDTO>> getAllUsers() {
        List<CustomerDashboardDTO> customerDashboardDTO = customerService.getAllCustomers().stream()
        	.map(c -> CustomerDashboardDTO.fromCustomer(c))
        	.collect(Collectors.toList());
        return new ResponseEntity<>(customerDashboardDTO, HttpStatus.OK);
    }
    
    // Update a customer by ID
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(
        @PathVariable Long id, @RequestBody Users customerDetails
    ) {
    	// TODO: Check -> id should be of current user!
        try {
            Users updatedCustomer = customerService.updateCustomer(id, customerDetails);
            return new ResponseEntity<>(CustomerResponseDTO.fromUser(updatedCustomer), HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    // Delete a customer by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
    	// TODO: Check -> id should be of current user!
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
