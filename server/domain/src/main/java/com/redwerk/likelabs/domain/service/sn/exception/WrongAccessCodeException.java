package com.redwerk.likelabs.domain.service.sn.exception;

public class WrongAccessCodeException extends SNGeneralException {

    private final String accessCode;

    public WrongAccessCodeException(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getAccessCode() {
        return accessCode;
    }

}
