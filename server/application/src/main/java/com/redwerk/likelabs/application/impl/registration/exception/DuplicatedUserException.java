package com.redwerk.likelabs.application.impl.registration.exception;


public class DuplicatedUserException extends RuntimeException {

    private final String phone;


    public DuplicatedUserException(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

}
