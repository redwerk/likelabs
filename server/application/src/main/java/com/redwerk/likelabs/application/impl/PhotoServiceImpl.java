package com.redwerk.likelabs.application.impl;

import com.redwerk.likelabs.application.PhotoService;
import com.redwerk.likelabs.domain.model.photo.Photo;
import com.redwerk.likelabs.domain.model.photo.PhotoRepository;
import com.redwerk.likelabs.domain.model.photo.PhotoStatus;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserRepository;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PhotoServiceImpl implements PhotoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Photo> getPhotos(long userId, PhotoStatus photoStatus, Pager pager) {
        Validate.isTrue((photoStatus != null) && (photoStatus != PhotoStatus.SELECTED), "wrong photo status");
        User user = userRepository.get(userId);
        return photoRepository.findAll(user, photoStatus, pager);
    }

    @Override
    @Transactional(readOnly = true)
    public Photo getPhoto(long photoId) {
        return photoRepository.get(photoId);
    }

    @Override
    @Transactional
    public void updatePhoto(long photoId, PhotoStatus photoStatus) {
        Photo photo = photoRepository.get(photoId);
        photo.setStatus(photoStatus);
    }

}
