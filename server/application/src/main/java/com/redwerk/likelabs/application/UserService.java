package com.redwerk.likelabs.application;

import com.redwerk.likelabs.application.dto.UserData;
import com.redwerk.likelabs.domain.model.photo.Photo;
import com.redwerk.likelabs.domain.model.user.User;

import java.util.List;

public interface UserService {

    User getUser(long userId);

    User getUser(String userPhone);

    User createUser(UserData userData);

    void deleteUser(long userId);

    void updateUser(long userId, UserData userData);


    List<Photo> getUserPhotos(long userId);

}
