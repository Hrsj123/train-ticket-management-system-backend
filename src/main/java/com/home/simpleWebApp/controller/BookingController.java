package com.home.simpleWebApp.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.home.simpleWebApp.dto.BookingDashboardDTO;
import com.home.simpleWebApp.dto.booking.BookingRequest;
import com.home.simpleWebApp.model.Booking;
import com.home.simpleWebApp.service.BookingService;


@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

	@Autowired
	private BookingService bookingService;
	
	@PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest) {
    	//  POST args required  	Long trainId, Long customerId, int seatCount, String seatClass
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    
        Booking createdBooking = bookingService.createBooking(bookingRequest, username);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

	@PostMapping("/{bookingId}/cancel")
	public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
		// TODO: A user can only cancel his own booking
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		boolean isCancelled = bookingService.cancelBooking(bookingId, username);	// ...
		if (isCancelled) {
			return ResponseEntity.ok("Booking cancelled successfully");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found");
	}

	// TODO: Protect this route based on id...
	@GetMapping("/history")
	public ResponseEntity<List<Booking>> getBookingHistory(@RequestParam Long customerId) {
		List<Booking> bookings = bookingService.getBookingHistory(customerId);
		return ResponseEntity.ok(bookings);
	}
	
	@GetMapping		// For admin
	public ResponseEntity<List<BookingDashboardDTO>> getBookingHistory() {
		List<Booking> bookings = bookingService.getBookingsWithTranStartAfterNow();
		List<BookingDashboardDTO> bookingsDashboard = bookings.stream()
			.map(booking -> BookingDashboardDTO.fromBooking(booking))
			.collect(Collectors.toList());
		return ResponseEntity.ok(bookingsDashboard);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Booking> getBooking(@PathVariable Long bookingId) {
		Booking booking = bookingService.getBookingById(bookingId);
		if (booking != null) {
			return ResponseEntity.ok(booking);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
}
