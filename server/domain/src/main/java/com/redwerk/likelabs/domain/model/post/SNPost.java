package com.redwerk.likelabs.domain.model.post;

import com.redwerk.likelabs.domain.model.SocialNetworkType;

public class SNPost extends Post {
    
    private SocialNetworkType snType;
    
    private String snPostId;

    public SocialNetworkType getSnType() {
        return snType;
    }

    public String getSnPostId() {
        return snPostId;
    }

}
