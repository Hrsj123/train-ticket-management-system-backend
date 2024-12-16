package com.home.simpleWebApp.dto;

import com.home.simpleWebApp.model.UserRole;
import com.home.simpleWebApp.model.Users;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDashboardDTO {
	String userName;
	String address;
	String phoneNumber;
	UserRole role; 
	
	public static CustomerDashboardDTO fromCustomer(Users customer) {
		return CustomerDashboardDTO.builder()
				.userName(customer.getUserName())
				.address(customer.getAddress())
				.phoneNumber(customer.getPhoneNumber())
				.role(customer.getRole())
				.build();
	}
}
