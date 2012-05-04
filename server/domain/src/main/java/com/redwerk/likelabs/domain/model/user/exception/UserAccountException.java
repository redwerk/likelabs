package com.redwerk.likelabs.domain.model.user.exception;

import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.user.User;

public class UserAccountException extends RuntimeException {

    private final User user;

    private final SocialNetworkType accountType;

    public UserAccountException(User user, SocialNetworkType accountType) {
        this.user = user;
        this.accountType = accountType;
    }

    public User getUser() {
        return user;
    }

    public SocialNetworkType getAccountType() {
        return accountType;
    }

}
