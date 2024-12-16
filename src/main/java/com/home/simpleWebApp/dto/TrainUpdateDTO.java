package com.home.simpleWebApp.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainUpdateDTO {
	private String trainName;
	private String startLocation;
	private String endLocation;
	private boolean status;
	private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int totalSeatCount;
}
