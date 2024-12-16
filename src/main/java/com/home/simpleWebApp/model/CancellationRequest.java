package com.home.simpleWebApp.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancellationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;    // Foreign-key reference to Booking

    @NotNull
    private LocalDateTime requestDate; // Date and time of the cancellation request

    @NotNull
    private String reason;      // Reason for cancellation

    @NotNull
    private String status;      // Status of the request: "Pending", "Approved", "Rejected"

    // Constructor to automatically set requestDate to the current time
    public CancellationRequest(Booking booking, String reason, String status) {
        this.booking = booking;
        this.reason = reason;
        this.status = status;
        this.requestDate = LocalDateTime.now();
    }
}
