package com.home.simpleWebApp.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.simpleWebApp.dto.TrainAvailabilityResponse;
import com.home.simpleWebApp.model.Booking;
import com.home.simpleWebApp.model.Passenger;
import com.home.simpleWebApp.model.Train;
import com.home.simpleWebApp.repository.BookingRepository;
import com.home.simpleWebApp.repository.TrainRepository;

@Service
public class TrainService {
	private static String[] classPrefixes = {"A", "B", "C", "D"};
	
	@Autowired
	private TrainRepository trainRepository;
	
    @Autowired
    private BookingRepository bookingRepository;
	
    public List<Train> searchTrains(String origin, String destination, LocalDate date) {
    	// Convert LocalDate to LocalDateTime (start of the day)
        LocalDateTime startDateTime = date.atStartOfDay();
        // Convert LocalDate to LocalDateTime (end of the day)
        LocalDateTime endDateTime = date.atTime(23, 59, 59);

        return trainRepository.findByOriginAndDestinationAndDateRange(origin, destination, startDateTime, endDateTime);
    }
    
    // Create a new train
    public Train createTrain(Train train) {
        return trainRepository.save(train);
    }
    
    // Update an existing train
    public Train updateTrain(Long trainId, Train train) {
        Optional<Train> existingTrain = trainRepository.findById(trainId);
        if (existingTrain.isPresent()) {
            Train updatedTrain = existingTrain.get();
            updatedTrain.setTrainName(train.getTrainName());
            updatedTrain.setStartLocation(train.getStartLocation());
            updatedTrain.setEndLocation(train.getEndLocation());
            updatedTrain.setStatus(train.isStatus());
            updatedTrain.setStartDateTime(train.getStartDateTime());
            updatedTrain.setEndDateTime(train.getEndDateTime());
            updatedTrain.setSeatsCountPerClass(train.getSeatsCountPerClass());
            return trainRepository.save(updatedTrain);
        }
        return null;
    }
    
    public List<Train> allTrains() {
    	return trainRepository.findAll();
    }
    
    // Delete a train
    public boolean deleteTrain(Long trainId) {
        Optional<Train> existingTrain = trainRepository.findById(trainId);
        if (existingTrain.isPresent()) {
            trainRepository.delete(existingTrain.get());
            return true;
        }
        return false;
    }
    
    // Placeholder for generating occupancy report (custom logic needed)
    public String generateOccupancyReport() {
        // This should generate a report based on actual train occupancy data
        return "Occupancy Report Placeholder";
    }
    
    // TODO: Update this method...
    public TrainAvailabilityResponse getTrainAvailability(Long trainId, LocalDateTime travelDate) {
        Train train = trainRepository.findById(trainId)
                                     .orElseThrow(() -> new RuntimeException("Train not found"));

        // Fetch all bookings for this train on the given date
        List<Booking> bookings = bookingRepository.findByTrainAndBookingDate(train, travelDate);

        // Calculate the total number of booked seats
        Map<String, Map<String, Boolean>> seatAvailabilityMapping = new HashMap<>();
        
        Map<String, List<String>> allSeats = generateSeatsForTrain(train);
        
        // Set which contains booked seats...
        Set<String> bookedSeats = bookings.stream()
                .filter(booking -> booking.getPassengers().size() > 0)
                .flatMap(booking -> booking.getPassengers().stream())
                .map(Passenger::getSeatNumber)
                .collect(Collectors.toSet());
        
        for (String seatClass : allSeats.keySet()) {
        	Map<String, Boolean> seatAvailablilty = new HashMap<>();
        	for (String seatNumber : allSeats.get(seatClass)) {
        		seatAvailablilty.put(seatNumber, bookedSeats.contains(seatNumber));
        	}
        	seatAvailabilityMapping.put(seatClass, seatAvailablilty);
        }

        // Prepare the response object
        TrainAvailabilityResponse response = new TrainAvailabilityResponse();
        response.setTrainId(trainId);
        response.setAvailableSeats(seatAvailabilityMapping);

        return response;
    }
    
    // Helper method in 
    private Map<String, List<String>> generateSeatsForTrain(Train train) {  // Mapping->Class: seatNums
        Map<String, List<String>> seats = new HashMap<>();
         // Define class prefixes
        int seatsPerClass = train.getSeatsCountPerClass();

        for (int i = 0; i < 4; i++) {
            String prefix = classPrefixes[i % classPrefixes.length]; // Rotate through class prefixes
            List<String> seatsList = new ArrayList<>();
            for (int j = 1; j <= seatsPerClass; j++) {
                String seatNumber = prefix + j;
                String seat = String.format("%s-%s-%d", seatNumber, prefix, train.getTrainId());
                seatsList.add(seat);
            }
            seats.put(prefix, seatsList);
        }

        return seats;
    }

}
