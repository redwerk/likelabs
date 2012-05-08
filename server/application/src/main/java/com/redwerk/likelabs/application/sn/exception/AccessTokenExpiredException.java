package com.redwerk.likelabs.application.sn.exception;

import com.redwerk.likelabs.domain.model.user.UserSocialAccount;

public class AccessTokenExpiredException extends SNGeneralException {

    private final UserSocialAccount userAccount;

    public AccessTokenExpiredException(UserSocialAccount userAccount) {
        this.userAccount = userAccount;
    }

    public UserSocialAccount getExpiredAccount() {
        return userAccount;
    }

}
