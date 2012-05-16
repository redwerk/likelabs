package com.redwerk.likelabs.domain.model.photo;

import com.redwerk.likelabs.domain.model.user.User;

import java.util.List;

public interface PhotoRepository {

    Photo get(long id);

    List<Photo> findAll(User user, PhotoStatus status, int offset, int limit);

    void add(Photo photo);

    void remove(Photo photo);

}
