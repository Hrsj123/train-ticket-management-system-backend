package com.home.simpleWebApp.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Train {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long trainId;
	
	@NotNull
	private String trainName;
	
	@NotNull
	private String startLocation;
	
	@NotNull
	
	private String endLocation;
	
	@NotNull
    private boolean status;
	
	@NotNull
	private LocalDateTime startDateTime;
	
	@NotNull
    private LocalDateTime endDateTime;
	
	@Size(min = 5, max = 10)
    private int seatsCountPerClass;  // Renamed...
	
	
}
