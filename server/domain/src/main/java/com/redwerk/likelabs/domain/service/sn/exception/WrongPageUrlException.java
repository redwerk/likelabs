package com.redwerk.likelabs.domain.service.sn.exception;

public class WrongPageUrlException extends SNException {

    private final String pageUrl;

    public WrongPageUrlException(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getPageUrl() {
        return pageUrl;
    }

}
