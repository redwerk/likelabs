package com.redwerk.likelabs.application.dto.user;

public class UserProfileData {

    private final String phone;

    private final String password;

    private final String email;

    public UserProfileData(String phone, String password, String email) {
        this.phone = phone;
        this.password = password;
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

}
