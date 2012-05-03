package com.redwerk.likelabs.application;

import com.redwerk.likelabs.application.dto.UserCreateData;
import com.redwerk.likelabs.application.dto.UserUpdateData;
import com.redwerk.likelabs.domain.model.photo.Photo;
import com.redwerk.likelabs.domain.model.photo.PhotoStatus;
import com.redwerk.likelabs.domain.model.user.User;

import java.util.List;

public interface UserService {

    User getUser(long userId);

    User getUser(String phone);


    User createUser(UserCreateData userData);

    User confirmUser(String phone, String password);


    User updateUser(long userId, UserUpdateData userData);


    void deleteUser(long userId);


    List<Photo> getUserPhotos(long userId, PhotoStatus photoStatus);

}
