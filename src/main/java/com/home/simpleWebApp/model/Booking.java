package com.home.simpleWebApp.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long bookingId;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users customer;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "train_id")
	private Train train;
	
	@NotNull
	@Size(min = 0)
	private double price;
	
	@NotNull
	private LocalDateTime bookingDate;
	
	// TODO: Remove `bookedSeatNumbers`
//    @ElementCollection
//    @CollectionTable(name = "booked_seats", joinColumns = @JoinColumn(name = "booking_id"))
//    @Column(name = "seat_number")
//    private List<String> bookedSeatNumbers;
//    
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Passenger> passengers;
}
