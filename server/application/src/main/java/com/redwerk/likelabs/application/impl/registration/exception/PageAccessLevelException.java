package com.redwerk.likelabs.application.impl.registration.exception;

import com.redwerk.likelabs.domain.model.company.CompanySocialPage;
import com.redwerk.likelabs.domain.model.user.User;


public class PageAccessLevelException extends RuntimeException {

    private final User user;
    
    private final CompanySocialPage page;

    public PageAccessLevelException(User user, CompanySocialPage companySocialPage) {
        this.user = user;
        this.page = companySocialPage;
    }

    public CompanySocialPage getCompanySocialPage() {
        return page;
    }

    public User getUser() {
        return user;
    }

}
