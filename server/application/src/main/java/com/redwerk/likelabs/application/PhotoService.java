package com.redwerk.likelabs.application;

import com.redwerk.likelabs.domain.model.photo.Photo;
import com.redwerk.likelabs.domain.model.photo.PhotoStatus;

import java.util.List;

public interface PhotoService {

    List<Photo> getPhotos(long userId, PhotoStatus photoStatus);

    Photo getPhotos(long photoId);

    void updatePhoto(long photoId, PhotoStatus photoStatus);

}
