package com.redwerk.likelabs.application.dto;

public class UserData {

    private final String phone;

    private final String password;

    private final String email;

    private final boolean publishInSN;

    private final boolean notifyIfClient;

    public UserData(String phone, String password, String email, boolean publishInSN, boolean notifyIfClient) {
        this.phone = phone;
        this.password = password;
        this.email = email;
        this.publishInSN = publishInSN;
        this.notifyIfClient = notifyIfClient;
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

    public boolean isPublishInSN() {
        return publishInSN;
    }

    public boolean isNotifyIfClient() {
        return notifyIfClient;
    }

}

