package com.redwerk.likelabs.domain.service;

import com.redwerk.likelabs.domain.model.user.User;

public interface UserRegistrator {
    
    User registerUser(String phone);

}
