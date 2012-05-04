package com.redwerk.likelabs.domain.model.user.exception;

import com.redwerk.likelabs.domain.model.user.User;

public class WrongUserPasswordException extends RuntimeException {
    
    private final User user;
    
    private final String password;

    public WrongUserPasswordException(User user, String password) {
        this.user = user;
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

}
