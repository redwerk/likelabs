package com.redwerk.likelabs.application.sn.exception;

public class WrongPageUrlException extends SNGeneralException {

    private final String pageUrl;

    public WrongPageUrlException(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getPageUrl() {
        return pageUrl;
    }

}
