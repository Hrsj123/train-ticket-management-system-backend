package com.home.simpleWebApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.home.simpleWebApp.model.UserRole;
import com.home.simpleWebApp.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
	
	Optional<Users> findByUserName(String userName);
	
	Users findByUserNameAndRole(String userName, UserRole role);

}
