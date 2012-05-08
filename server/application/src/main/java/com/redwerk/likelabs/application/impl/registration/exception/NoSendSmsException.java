package com.redwerk.likelabs.application.impl.registration.exception;

public class NoSendSmsException extends RuntimeException {

    private final String phone;


    public NoSendSmsException(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String getMessage() {
        return "not send SMS for phone:" + phone;
    }


}
