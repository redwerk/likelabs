package com.redwerk.likelabs.application.dto;

import java.util.List;

public class UserUpdateData extends UserCreateData {
    
    private final String password;

    public UserUpdateData(String phone, String password, String email, boolean publishInSN, boolean notifyIfClient,
                          List<UserSocialAccountData> accounts) {
        super(phone, email, publishInSN, notifyIfClient, accounts);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

}
