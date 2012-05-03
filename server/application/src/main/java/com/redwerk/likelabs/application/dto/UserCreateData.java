package com.redwerk.likelabs.application.dto;

import java.util.List;

public class UserCreateData {
    
    private final String phone;

    private final String email;
    
    private final boolean publishInSN;

    private final boolean notifyIfClient;

    private final List<UserSocialAccountData> accounts;

    public UserCreateData(String phone, String email, boolean publishInSN, boolean notifyIfClient,
                          List<UserSocialAccountData> accounts) {
        this.phone = phone;
        this.email = email;
        this.publishInSN = publishInSN;
        this.notifyIfClient = notifyIfClient;
        this.accounts = accounts;
    }

    public String getPhone() {
        return phone;
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

    public List<UserSocialAccountData> getAccounts() {
        return accounts;
    }

}
