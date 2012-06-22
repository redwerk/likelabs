package com.redwerk.likelabs.domain.service.sn.exception;

public class WrongAccessTokenException extends SNException {

    private final String accessToken;

    public WrongAccessTokenException(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

}
