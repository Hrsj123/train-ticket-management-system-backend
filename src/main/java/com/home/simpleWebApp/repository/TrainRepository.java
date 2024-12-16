package com.home.simpleWebApp.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.home.simpleWebApp.model.Train;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {
	
	// Search train query
	@Query("SELECT t FROM Train t WHERE LOWER(t.startLocation) LIKE LOWER(CONCAT('%', :origin, '%')) " +
	           "AND LOWER(t.endLocation) LIKE LOWER(CONCAT('%', :destination, '%')) " +
	           "AND t.startDateTime BETWEEN :startDate AND :endDate")
	List<Train> findByOriginAndDestinationAndDateRange(String origin, String destination, LocalDateTime startDate, LocalDateTime endDate);
}
