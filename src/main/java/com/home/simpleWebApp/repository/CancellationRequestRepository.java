package com.home.simpleWebApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.home.simpleWebApp.model.CancellationRequest;

@Repository
public interface CancellationRequestRepository extends JpaRepository<CancellationRequest, Long>{
	// Add custom queries as needed...
}
