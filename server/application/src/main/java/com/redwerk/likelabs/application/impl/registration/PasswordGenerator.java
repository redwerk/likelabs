package com.redwerk.likelabs.application.impl.registration;

public interface PasswordGenerator {
    
    String getPassword(String phone);

    String getActivateEmailCode(String email, long userId);

}
