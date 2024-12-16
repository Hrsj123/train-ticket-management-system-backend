package com.home.simpleWebApp.controller;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.home.simpleWebApp.dto.TrainAvailabilityResponse;
import com.home.simpleWebApp.model.Train;
import com.home.simpleWebApp.service.TrainService;


@RestController
@RequestMapping("/api/v1/trains")
public class TrainController {
	
	@Autowired
	private TrainService trainService;
	
	@GetMapping("/search")
	ResponseEntity<List<Train>> getAllCustomers(
			@RequestParam("origin") String origin,
            @RequestParam("destination") String destination,
            @RequestParam("date") String date
	) {   
        LocalDate travelDate = LocalDate.parse(date); // Convert date to LocalDate
        List<Train> trains = trainService.searchTrains(origin, destination, travelDate);
        return ResponseEntity.ok(trains);
    }
	
	@GetMapping("/{trainId}/availability")
	public ResponseEntity<TrainAvailabilityResponse> checkTrainAvailability(
	    @PathVariable Long trainId, 
	    @RequestParam String classType,  // dummy parameter as per your request
	    @RequestParam String date
    ) {
	    LocalDateTime travelDate = LocalDateTime.parse(date + "T00:00:00"); // Assuming the date is in "YYYY-MM-DD" format
	    TrainAvailabilityResponse availabilityResponse = trainService.getTrainAvailability(trainId, travelDate);
	    
	    return ResponseEntity.ok(availabilityResponse);
	}
	
	@GetMapping
	ResponseEntity<List<Train>> getAllTrains() {
		List<Train> trains = trainService.allTrains();
		return ResponseEntity.ok(trains);
	}
}
