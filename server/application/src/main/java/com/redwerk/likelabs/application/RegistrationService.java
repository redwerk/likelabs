package com.redwerk.likelabs.application;

import com.redwerk.likelabs.application.dto.UserSocialAccountData;
import com.redwerk.likelabs.domain.model.user.User;

import java.util.List;

public interface RegistrationService {

    void createUser(String phone);
    
    void activateUser(String phone, String password);
    
    void confirmEmail(long userId, String confirmationCode);

}
