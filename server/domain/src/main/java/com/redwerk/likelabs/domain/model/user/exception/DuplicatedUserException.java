package com.redwerk.likelabs.domain.model.user.exception;

import com.redwerk.likelabs.domain.model.user.User;

public class DuplicatedUserException extends RuntimeException {

    private final String phone;

    private final User existingUser;

    public DuplicatedUserException(String phone, User existingUser) {
        this.phone = phone;
        this.existingUser = existingUser;
    }

    public String getPhone() {
        return phone;
    }

    public User getExistingUser() {
        return existingUser;
    }

}
