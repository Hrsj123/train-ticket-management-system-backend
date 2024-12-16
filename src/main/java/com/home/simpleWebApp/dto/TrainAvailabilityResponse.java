package com.home.simpleWebApp.dto;

import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TrainAvailabilityResponse {
    private Long trainId;
    private Map<String, Map<String, Boolean>> availableSeats;		// For 4 class
}