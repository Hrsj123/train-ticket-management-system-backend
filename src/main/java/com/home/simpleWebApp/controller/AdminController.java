package com.home.simpleWebApp.controller;

import com.home.simpleWebApp.dto.CustomerResponseDTO;
import com.home.simpleWebApp.model.Train;
import com.home.simpleWebApp.model.Users;
import com.home.simpleWebApp.service.TrainService;
import com.home.simpleWebApp.service.UserService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
	
    @Autowired
    private UserService userService;

    @Autowired
    private TrainService trainService;
    
    // Admin Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {
    	Map<String, String> tokens = userService.verify(user);
    	if (tokens.isEmpty()) {
    		return new ResponseEntity<>("{\"message\": \"Failed to authenticate User\"}", HttpStatus.UNAUTHORIZED);
    	}
    	// Get User
    	Users loggedInUser = userService.getCustomerByUserName(user.getUserName())
    			.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "{\"message\": \"Invalid username\"}"));
    	
    	Map<String, Object> response = new HashMap<>();
    	response.put("user", CustomerResponseDTO.fromUser(loggedInUser).toHashMap());
    	response.put("tokens", tokens);
    	return ResponseEntity.ok(response);
    }
    
    // Refresh	-> TODO: Move it into some AuthController...
    @PostMapping("/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	userService.refreshToken(request, response);
    }

    // Logout	-> TODO: Move it into some AuthController...
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
    	// TODO: Invalidate auth tokens...
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }

    // Create a new Train
    @PostMapping("/trains")
    public ResponseEntity<Train> createTrain(@Valid @RequestBody Train train) {
        Train createdTrain = trainService.createTrain(train);
        return new ResponseEntity<>(createdTrain, HttpStatus.CREATED);
    }
    
    // Update an existing Train
    @PutMapping("/trains/{trainId}")
    public ResponseEntity<Train> updateTrain(@PathVariable Long trainId, @Valid @RequestBody Train train) {
        Train updatedTrain = trainService.updateTrain(trainId, train);
        if (updatedTrain != null) {
            return new ResponseEntity<>(updatedTrain, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete a Train
    @DeleteMapping("/trains/{trainId}")
    public ResponseEntity<Void> deleteTrain(@PathVariable Long trainId) {
        boolean isDeleted = trainService.deleteTrain(trainId);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get occupancy report for all trains
    @GetMapping("/reports/occupancy")
    public ResponseEntity<String> getOccupancyReport() {
        // Placeholder for actual occupancy report generation logic
        String report = "Occupancy Report: Train X - 75% Occupied, Train Y - 60% Occupied";
        return new ResponseEntity<>(report, HttpStatus.OK);
    }
}
