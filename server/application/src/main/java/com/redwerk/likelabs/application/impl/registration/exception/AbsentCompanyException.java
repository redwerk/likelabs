package com.redwerk.likelabs.application.impl.registration.exception;

import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.user.User;


public class AbsentCompanyException extends RuntimeException {
    
    User user;
    
    public AbsentCompanyException(User user) {
        this.user = user;
    }

    @Override
    public String getMessage() {
        return "user: " + user.getPhone() + "not belong any company";
    }

    public User getUser() {
        return user;
    }
}
