package com.home.simpleWebApp.model;

import jakarta.persistence.*;
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
public class Passenger {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long passengerId;
    
    @NotNull
    @Size(min = 2, message = "Name must be at least 2 characters long")
    private String name;

    @NotNull
    private String gender; // Can also use an Enum for Gender
    
    @NotNull
    private int age;

    @NotNull
    private String classType; // e.g., AC, Sleeper, etc.
    
    @NotNull
    private String seatNumber;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
}
