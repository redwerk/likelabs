package com.redwerk.likelabs.application.dto.company;

import com.redwerk.likelabs.domain.model.SocialNetworkType;

public class CompanyPageData {

    private final SocialNetworkType snType;

    private final String url;

    public CompanyPageData(SocialNetworkType snType, String url) {
        this.snType = snType;
        this.url = url;
    }

    public SocialNetworkType getSnType() {
        return snType;
    }

    public String getUrl() {
        return url;
    }

}
