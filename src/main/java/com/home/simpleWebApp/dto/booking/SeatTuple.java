package com.home.simpleWebApp.dto.booking;

public class SeatTuple {
    String seatNumber;
    boolean isAvailable;

    public SeatTuple(String seatNumber, boolean isAvailable) {
        this.seatNumber = seatNumber;
        this.isAvailable = isAvailable;
    }
}