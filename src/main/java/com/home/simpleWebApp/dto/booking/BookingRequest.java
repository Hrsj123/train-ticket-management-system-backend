package com.home.simpleWebApp.dto.booking;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    private Long trainId;
    private int numberOfTickets;
    private LocalDate travelDate;
    private List<PassengerRequest> passengers;
}
