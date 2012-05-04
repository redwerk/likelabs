package com.redwerk.likelabs.application;

import com.redwerk.likelabs.application.dto.UserSocialAccountData;
import com.redwerk.likelabs.application.dto.UserData;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.photo.Photo;
import com.redwerk.likelabs.domain.model.photo.PhotoStatus;
import com.redwerk.likelabs.domain.model.user.User;

import java.util.List;

public interface UserService {

    User getUser(long userId);

    User findUser(String phone);


    void updateUser(long userId, UserData userData);

    void updateUserEmail(long userId, String email);

    void attachToSN(long userId, UserSocialAccountData accountData);

    void detachFromSN(long userId, SocialNetworkType snType);


    void deleteUser(long userId);


    List<Photo> getUserPhotos(long userId, PhotoStatus photoStatus);

}
