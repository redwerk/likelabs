package com.redwerk.likelabs.application.impl.registration;


public interface CodeGenerator {

     String getEmailConfirmationCode(String email, long userId);

     String getAdminActivationCode(long userId, String email, String phone);
}
