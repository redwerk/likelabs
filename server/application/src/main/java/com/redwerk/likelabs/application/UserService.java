package com.redwerk.likelabs.application;

import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.dto.user.UserData;
import com.redwerk.likelabs.application.dto.user.UserProfileData;
import com.redwerk.likelabs.application.dto.user.UserSettingsData;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.photo.Photo;
import com.redwerk.likelabs.domain.model.photo.PhotoStatus;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserSocialAccount;
import com.redwerk.likelabs.domain.model.user.UserStatus;

import java.util.List;

public interface UserService {

    Report<User> getRegularUsers(Pager pager);


    User getUser(long userId);

    User getUser(SocialNetworkType snType, String accountId);

    User findUser(String phone);

    
    User createUser(long creatorId, UserProfileData userProfile);


    @Deprecated
    void updateUser(long userId, UserData userData);

    void updateProfile(long userId, UserProfileData userProfile);

    void updateSettings(long userId, UserSettingsData userSettings);

    void updateEmail(long userId, String email);

    void updateStatus(long userId, long updaterId, UserStatus status);


    UserSocialAccount attachAccount(long userId, SocialNetworkType snType, String accessCode);

    UserSocialAccount attachAccount(long userId, UserSocialAccount account);

    void detachAccount(long userId, SocialNetworkType snType);

    @Deprecated
    void deleteUser(long userId);

}
