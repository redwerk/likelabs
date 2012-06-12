package com.redwerk.likelabs.application.dto.company;

public class CompanyData {
    
    private final String name;

    private final String phone;

    private final String email;

    private final boolean moderateReviews;

    public CompanyData(String name, String phone, String email, boolean moderateReviews) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.moderateReviews = moderateReviews;
    }

    public CompanyData(String name, String phone, String email) {
        this(name, phone, email, true);
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public boolean isModerateReviews() {
        return moderateReviews;
    }

}
