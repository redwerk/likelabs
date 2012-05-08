package com.redwerk.likelabs.application.impl.registration.exception;


public class IncorrectPasswordException extends RuntimeException {

    private final String phone;

    private final String password;


    public IncorrectPasswordException(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }
}
