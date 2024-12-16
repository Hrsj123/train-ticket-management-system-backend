package com.home.simpleWebApp.dto;

import java.util.HashMap;
import java.util.Map;

import com.home.simpleWebApp.model.Users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDTO {
	private Long userId;
    private String userName;
    private String address;
    private String phoneNumber;
    private int age;
    private String gender;
    
    public static CustomerResponseDTO fromUser(Users user) {
    	return CustomerResponseDTO.builder()
    				.userId(user.getUserId())
    				.userName(user.getUserName())
    				.address(user.getAddress())
    				.age(user.getAge())
    				.gender(user.getGender())
    				.phoneNumber(user.getPhoneNumber())
    				.build();
    }
    
    public Map<String, Object> toHashMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("userName", userName);
        map.put("address", address);
        map.put("phoneNumber", phoneNumber);
        map.put("age", age);
        map.put("gender", gender);
        return map;
    }
}
