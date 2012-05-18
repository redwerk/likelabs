package com.redwerk.likelabs.domain.model.company.exception;

public class PageNotFoundException extends RuntimeException {
    
    private final String pageId;

    public PageNotFoundException(String pageId) {
        this.pageId = pageId;
    }

    public String getPageId() {
        return pageId;
    }

}
