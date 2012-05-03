package com.redwerk.likelabs.application.dto;

import com.redwerk.likelabs.domain.model.SocialNetworkType;

public class UserSocialAccountData {

    private final SocialNetworkType type;

    private final String accountId;

    private final String accessToken;

    private final String name;

    public UserSocialAccountData(SocialNetworkType type, String accountId, String accessToken, String name) {
        this.type = type;
        this.accountId = accountId;
        this.accessToken = accessToken;
        this.name = name;
    }

    public SocialNetworkType getType() {
        return type;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getName() {
        return name;
    }

}
