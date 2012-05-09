package com.redwerk.likelabs.application.impl.registration;


public interface ActivateEmailCodeGenerator {

     String getActivateEmailCode(String email, long userId);
}
