package com.home.simpleWebApp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.home.simpleWebApp.dto.booking.BookingRequest;
import com.home.simpleWebApp.dto.booking.PassengerRequest;
import com.home.simpleWebApp.model.Booking;
import com.home.simpleWebApp.model.CancellationRequest;
import com.home.simpleWebApp.model.Passenger;
import com.home.simpleWebApp.model.Train;
import com.home.simpleWebApp.model.Users;
import com.home.simpleWebApp.repository.BookingRepository;
import com.home.simpleWebApp.repository.CancellationRequestRepository;
import com.home.simpleWebApp.repository.TrainRepository;
import com.home.simpleWebApp.repository.UserRepository;

@Service
public class BookingService {
	// TODO: implement with passengers...
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CancellationRequestRepository cancellationRequestRepository;

    @Autowired
    private TrainRepository trainRepository;

    public Booking createBooking(BookingRequest bookingRequest, String username) {
        // Fetch User and Train
        Users user = userRepository.findByUserName(username)
                      .orElseThrow(() -> new RuntimeException("User not found"));
        Train train = trainRepository.findById(bookingRequest.getTrainId())
                      .orElseThrow(() -> new RuntimeException("Train not found"));

        // Build Booking Entity
        Booking booking = Booking.builder()
                .customer(user)
                .train(train)
                .price(calculatePrice(bookingRequest))
                .bookingDate(LocalDateTime.now())
                .build();
        
        // Build and Add Passengers
        List<Passenger> passengers = bookingRequest.getPassengers().stream()
        		.map(p -> Passenger.builder()
        	            .name(p.getName())
        	            .gender(p.getGender())
        	            .age(p.getAge())
        	            .classType(p.getClassType())
        	            .booking(booking) // Link to Booking
        	            .build()
	            )
        	    .collect(Collectors.toList());
        booking.setPassengers(passengers);

        // Save and Return
        return bookingRepository.save(booking);
    }

    private double calculatePrice(BookingRequest bookingRequest) {
        Map<String, Double> classBasePrices = Map.of(
                "First Class", 1500.0,
                "Second Class", 1000.0,
                "AC", 1200.0,
                "Sleeper", 800.0
        );
        
        double res = 0;
        for (PassengerRequest pr : bookingRequest.getPassengers()) {
        	res += classBasePrices.get(pr.getClassType());
        }
        
        // TODO: Can also add distance factor...
        
        return res;
    }
    

    public boolean cancelBooking(Long bookingId, String username) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            if (booking.getCustomer().getUserName().equals(username)) {
            	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
            }
            CancellationRequest cancellationRequest = new CancellationRequest(booking, "Customer requested cancellation", "Pending");
            cancellationRequestRepository.save(cancellationRequest);
            return true;
        }
        return false;
    }
    
    public List<Booking> getBookingHistory(Long customerId) {
        return bookingRepository.findByCustomerUserId(customerId);
    }
    
    public Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId).orElse(null);
    }
    
    public List<Booking> getBookingsWithTranStartAfterNow() {
    	return bookingRepository.findAll().stream()
    			.filter(booking -> booking.getTrain().getStartDateTime().isAfter(LocalDateTime.now()))
    			.collect(Collectors.toList());
    }
}
