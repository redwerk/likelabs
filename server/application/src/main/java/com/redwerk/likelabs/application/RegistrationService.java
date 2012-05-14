package com.redwerk.likelabs.application;

public interface RegistrationService {

    void createUser(String phone);
    
    void activateUser(String phone, String password);
    
    void confirmEmail(long userId, String email, String confirmationCode);

}
