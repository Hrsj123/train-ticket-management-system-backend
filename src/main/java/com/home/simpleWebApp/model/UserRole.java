package com.home.simpleWebApp.model;

public enum UserRole {
	ADMIN,
	CUSTOMER;
	
	public String getRoleName() {
        return "ROLE_" + this.name();
    }
}
