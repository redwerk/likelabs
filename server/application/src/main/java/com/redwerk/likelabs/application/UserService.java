package com.redwerk.likelabs.application;

import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.dto.user.UserData;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.photo.Photo;
import com.redwerk.likelabs.domain.model.photo.PhotoStatus;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserSocialAccount;

import java.util.List;

public interface UserService {

    Report<User> getRegularUsers(Pager pager);

    User getUser(long userId);

    User findUser(String phone);


    void updateUser(long userId, UserData userData);

    void updateEmail(long userId, String email);

    UserSocialAccount attachAccount(long userId, SocialNetworkType snType, String accessCode);

    void detachAccount(long userId, SocialNetworkType snType);


    void deleteUser(long userId);


}
