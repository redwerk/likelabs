package com.redwerk.likelabs.domain.service.sn.exception;

import com.redwerk.likelabs.domain.model.user.UserSocialAccount;

public class AccessTokenExpiredException extends SNException {

    private final UserSocialAccount userAccount;

    public AccessTokenExpiredException(UserSocialAccount userAccount) {
        this.userAccount = userAccount;
    }

    public UserSocialAccount getExpiredAccount() {
        return userAccount;
    }

}
