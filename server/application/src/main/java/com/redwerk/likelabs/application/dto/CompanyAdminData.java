package com.redwerk.likelabs.application.dto;

public class CompanyAdminData {
    
    private final String phone;
    
    private final String email;

    public CompanyAdminData(String phone, String email) {
        this.phone = phone;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
    
}
