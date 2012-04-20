package com.redwerk.likelabs.application.dto;

import java.util.Date;

public class UserData {
    
    private final String phone;
    
    private final String email;
    
    private final boolean isAdmin;
    
    private final Date created;
    
    private final boolean notifyAboutComments;

    public UserData(String phone, String email, boolean admin, Date created, boolean notifyAboutComments) {
        this.phone = phone;
        this.email = email;
        isAdmin = admin;
        this.created = created;
        this.notifyAboutComments = notifyAboutComments;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public Date getCreated() {
        return created;
    }

    public boolean isNotifyAboutComments() {
        return notifyAboutComments;
    }

}
