package com.redwerk.likelabs.application.impl.registration.exception;

public class NoSendMailException extends RuntimeException {

    private final String email;
    private final long userId;

    public NoSendMailException(String  email, long userId) {
        this.email = email;
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public String getMessage() {
        return "not send mail for address:" + email + ", and text body:" + String.valueOf(userId);
    }
}
