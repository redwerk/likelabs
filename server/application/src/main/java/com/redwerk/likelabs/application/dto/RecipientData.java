package com.redwerk.likelabs.application.dto;

import com.redwerk.likelabs.domain.model.review.RecipientType;

public class RecipientData {
    
    private final RecipientType type;
    
    private final String address;

    public RecipientData(RecipientType type, String address) {
        this.type = type;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public RecipientType getType() {
        return type;
    }
    
}
