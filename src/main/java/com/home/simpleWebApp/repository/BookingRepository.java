package com.home.simpleWebApp.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.home.simpleWebApp.model.Booking;
import com.home.simpleWebApp.model.Train;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
	
	List<Booking> findByTrainAndBookingDate(Train train, LocalDateTime bookingDate);
	
	List<Booking> findByCustomerUserId(Long customerId);
	
	List<Booking> findByTrainTrainId(Long trainId);
	
	List<Booking> findByBookingDateAfter(LocalDateTime date);
}
