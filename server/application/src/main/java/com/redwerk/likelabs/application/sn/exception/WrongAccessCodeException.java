package com.redwerk.likelabs.application.sn.exception;

public class WrongAccessCodeException extends SNGeneralException {

    private final String accessCode;

    public WrongAccessCodeException(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getAccessCode() {
        return accessCode;
    }

}
