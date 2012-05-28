package com.redwerk.likelabs.domain.service.dto;

import com.redwerk.likelabs.domain.model.photo.PhotoStatus;

public class PhotoData {

    private final PhotoStatus status;
    
    private byte[] image;

    public PhotoData(PhotoStatus status, byte[] image) {
        this.status = status;
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }

    public PhotoStatus getStatus() {
        return status;
    }
    
}
