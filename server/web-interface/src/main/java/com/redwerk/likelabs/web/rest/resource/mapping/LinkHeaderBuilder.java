package com.redwerk.likelabs.web.rest.resource.mapping;

public class LinkHeaderBuilder {
    
    private final String baseUrl;
    
    private final StringBuilder header = new StringBuilder();

    public LinkHeaderBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public LinkHeaderBuilder append(String url, String rel) {
        appendLinkHeader(url, rel);
        return this;
    }
    
    public LinkHeaderBuilder appendRelated(String url) {
        appendLinkHeader(url, "related");
        return this;
    }
    
    private void appendLinkHeader(String url, String rel) {
        if(header.length() != 0) {
            header.append(", ");
        }
        header.append("<").append(baseUrl).append(url.startsWith("/") ? "" : "/").append(">; rel=\"").append(rel).append("\"");
    }
}
