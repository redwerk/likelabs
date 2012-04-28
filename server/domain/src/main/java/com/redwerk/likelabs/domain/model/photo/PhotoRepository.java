package com.redwerk.likelabs.domain.model.photo;

import com.redwerk.likelabs.domain.model.user.User;

import java.util.List;

public interface PhotoRepository {

    Photo find(Long id);

    List<Photo> findAll(User user, PhotoStatus status);

    void add(Photo photo);

    void remove(Photo photo);

}
