package com.home.simpleWebApp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

//import com.home.simpleWebApp.dto.BookingRequest;
import com.home.simpleWebApp.model.Booking;
import com.home.simpleWebApp.model.CancellationRequest;
import com.home.simpleWebApp.model.Train;
import com.home.simpleWebApp.model.Users;
import com.home.simpleWebApp.repository.BookingRepository;
import com.home.simpleWebApp.repository.CancellationRequestRepository;
import com.home.simpleWebApp.repository.TrainRepository;
import com.home.simpleWebApp.repository.UserRepository;

@Service
public class __BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CancellationRequestRepository cancellationRequestRepository;

    @Autowired
    private TrainRepository trainRepository;

    
//    public Booking createBooking(BookingRequest bookingRequest, String userName) {
//    	// Fetch the train
//    	Train train = trainRepository.findById(bookingRequest.getTrainId()).orElseThrow(() -> new RuntimeException("Train not found"));
//    	
//        // Fetch the user
//        Users customer = userRepository.findByUserName(userName)
//        		.orElseThrow();
//        
//        // TODO: Replace this with something else...
//    	List<String> bookedSeats = getBookedSeatsForTrain(bookingRequest.getTrainId());
//    	
//    	// Calculate available seats
//        List<String> allSeats = generateAllSeats(train, bookingRequest.getSeatClass()); // e.g., generates AC1, AC2... or SLEEPER1, etc. `DUMMY METHOD!`
//        List<String> availableSeats = new ArrayList<>(allSeats);
//        availableSeats.removeAll(bookedSeats);
//
//        // Check if there are any remaining seats
//        if (availableSeats.isEmpty()) {
//            throw new RuntimeException("No seats available for booking");
//        }
//        
//        // Check if there are enough seats for the requested count
//        if (availableSeats.size() < bookingRequest.getSeatCount()) {
//            throw new RuntimeException("Not enough available seats for the requested booking");
//        }
//        
//        // Allocate seats
//        List<String> allocatedSeats = allocateSeats(availableSeats, bookingRequest.getSeatCount());
//        
//        
//        Booking booking = Booking.builder()
//                .customer(customer)
//                .train(train)
//                .price(calculateBookingPrice(bookingRequest.getSeatCount(), bookingRequest.getSeatClass()))
//                .bookingDate(LocalDateTime.now())
//                .bookedSeatNumbers(allocatedSeats)
//                .bookingDate(LocalDateTime.now())
//                .build();
//        
//        if (booking.getCustomer() == null) {
//            throw new IllegalArgumentException("Customer cannot be null");
//        }
//        
//        // Save the booking
//        return bookingRepository.save(booking);
//    }
    
//    private List<String> generateAllSeats(Train train, String seatClass) {
//        // Dynamically generate seat numbers based on the train's total capacity
//        int seatCount = train.getTotalSeatCount();
//        List<String> allSeats = new ArrayList<>();
//
//        for (int i = 1; i <= seatCount; i++) {
//            allSeats.add(seatClass + i); // e.g., AC1, AC2, SLEEPER1, etc.
//        }
//
//        return allSeats;
//    }
//    
//    private List<String> allocateSeats(List<String> availableSeats, int seatCount) {
//        return availableSeats.subList(0, seatCount); // Allocate the first `seatCount` seats
//    }

//    private double calculateBookingPrice(int seatCount, String seatClass) {
//        double pricePerSeat = seatClass.equalsIgnoreCase("AC") ? 500.0 : 300.0; // Example pricing logic
//        return seatCount * pricePerSeat;
//    }
    
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
    
//    private List<String> getBookedSeatsForTrain(Long trainId) {
//        // Fetch all bookings for the given train ID
//        List<Booking> bookings = bookingRepository.findByTrainTrainId(trainId);
//
//        // Collect all booked seat numbers from each booking
//        List<String> bookedSeats = new ArrayList<>();
//        for (Booking booking : bookings) {
//            bookedSeats.addAll(booking.getBookedSeatNumbers());
//        }
//
//        return bookedSeats;
//    }
    
    public List<Booking> getBookingsWithTranStartAfterNow() {
    	return bookingRepository.findAll().stream()
    			.filter(booking -> booking.getTrain().getStartDateTime().isAfter(LocalDateTime.now()))
    			.collect(Collectors.toList());
    }

}
