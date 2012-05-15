package com.redwerk.likelabs.application.impl.registration.exception;

import com.redwerk.likelabs.domain.model.user.User;


public class AbsentSocialAccountException extends Exception {

    private final User user;

    public AbsentSocialAccountException(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
