package com.redwerk.likelabs.application.impl.registration;


public interface CodeGenerator {

     String getConfirmEmailCode(String email, long userId);

     String getActivateAdminCode(long id,String email,String phone);
}
