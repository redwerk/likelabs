package com.redwerk.likelabs.web.ui.controller.dto;

public class ProfileData {
    
    private String phone = "";

    private String password = "";

    private String email = "";
    
    public ProfileData() {
    }
    
    public ProfileData(String phone, String password, String email) {
        this.phone = phone;
        this.password = password;
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
