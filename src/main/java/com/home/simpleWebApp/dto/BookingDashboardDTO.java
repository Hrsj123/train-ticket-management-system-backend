package com.home.simpleWebApp.dto;

import com.home.simpleWebApp.model.Booking;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingDashboardDTO {
	Long pnr;
	String trainName;
	String passengerName;
	
	public static BookingDashboardDTO fromBooking(Booking booking) {
		return BookingDashboardDTO.builder()
				.pnr(booking.getBookingId())
				.trainName(booking.getTrain().getTrainName())
				.passengerName(booking.getCustomer().getUserName())
				.build();
				
	}
}
