package com.redwerk.likelabs.application;

import com.redwerk.likelabs.application.impl.registration.exception.AbsentCompanyException;
import com.redwerk.likelabs.application.impl.registration.exception.AbsentSocialAccountException;
import com.redwerk.likelabs.application.impl.registration.exception.PageAccessLevelException;

public interface RegistrationService {

    void createUser(String phone);
    
    void activateUser(String phone, String password);
    
    void confirmEmail(long userId, String email, String confirmationCode);

    boolean validateAdminCode(long userId, String activateCode);

    boolean validateAdminPassword(long userId, String password);

    void activateCompanyAdmin(long userId) throws AbsentSocialAccountException, AbsentCompanyException, PageAccessLevelException;

}
