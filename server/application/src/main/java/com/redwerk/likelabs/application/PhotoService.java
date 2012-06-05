package com.redwerk.likelabs.application;

import com.redwerk.likelabs.domain.model.photo.Photo;
import com.redwerk.likelabs.domain.model.photo.PhotoStatus;
import com.redwerk.likelabs.domain.model.query.Pager;

import java.util.List;

public interface PhotoService {

    List<Photo> getPhotos(long userId, PhotoStatus photoStatus, Pager pager);

    Photo getPhoto(long photoId);

    void updatePhoto(long photoId, PhotoStatus photoStatus);

}
