package com.redwerk.likelabs.application.impl.registration.exception;

import com.redwerk.likelabs.domain.model.user.User;


public class AbsentSocialAccountException extends RuntimeException {

    User user;

    public AbsentSocialAccountException(User user) {
        this.user = user;
    }

    @Override
    public String getMessage() {
        return "user: " + user.getPhone() + "not heave any accounts";
    }
    
    public User getUser() {
        return user;
    }
}
