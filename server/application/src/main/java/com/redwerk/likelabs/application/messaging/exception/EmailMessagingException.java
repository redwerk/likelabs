package com.redwerk.likelabs.application.messaging.exception;

public class EmailMessagingException extends RuntimeException {

    private final String email;

    public EmailMessagingException(String  email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getMessage() {
        return "not send mail for address:" + email;
    }
}
