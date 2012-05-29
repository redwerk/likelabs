package com.redwerk.likelabs.domain.model.tablet.exception;

public class TabletByApiKeyNotFoundException extends TabletNotFoundException {
    
    private final String apiKey;

    public TabletByApiKeyNotFoundException(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

}
