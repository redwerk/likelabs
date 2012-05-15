package com.redwerk.likelabs.domain.model.photo;

public class PhotoNotFoundException extends RuntimeException {
    
    private final long photoId;

    public PhotoNotFoundException(long photoId) {
        this.photoId = photoId;
    }

    public long getPhotoId() {
        return photoId;
    }

}
