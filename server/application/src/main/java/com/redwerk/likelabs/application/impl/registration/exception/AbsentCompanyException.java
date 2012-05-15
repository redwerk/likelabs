package com.redwerk.likelabs.application.impl.registration.exception;

import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.user.User;


public class AbsentCompanyException extends RuntimeException {
    
    private final User user;
    
    public AbsentCompanyException(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
