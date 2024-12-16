package com.home.simpleWebApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequestDTO {
    private String userName;
    private String address;
    private String phoneNumber;
    private int age;
    private String gender;
    private String password;
}
