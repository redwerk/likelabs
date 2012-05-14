package com.redwerk.likelabs.application;

public interface RegistrationService {

    void createUser(String phone);
    
    void activateUser(String phone, String password);
    
    void confirmEmail(long userId, String email, String confirmationCode);

    void activateAdminCompany(long userId);

    boolean validateAdminCode(long id, String activateCode);

    boolean validateAdminPassword(long id, String password);

}
