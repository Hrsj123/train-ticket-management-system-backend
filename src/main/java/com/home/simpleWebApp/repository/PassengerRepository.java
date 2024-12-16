package com.home.simpleWebApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.home.simpleWebApp.model.Passenger;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

}
