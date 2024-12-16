package com.home.simpleWebApp.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerRequest {
    private String name;
    private String gender;
    private int age;
    private String classType;
    private String seatNumber;
}
