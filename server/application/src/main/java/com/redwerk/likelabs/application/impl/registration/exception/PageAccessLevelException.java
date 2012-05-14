package com.redwerk.likelabs.application.impl.registration.exception;

import com.redwerk.likelabs.domain.model.company.CompanySocialPage;
import com.redwerk.likelabs.domain.model.user.User;


public class PageAccessLevelException extends  RuntimeException {

    private User user;
    
    private CompanySocialPage page;

    public PageAccessLevelException(User user, CompanySocialPage companySocialPage) {
        this.user = user;
        this.page = companySocialPage;
    }

    @Override
    public String getMessage() {
        return "user: " + user.getPhone() + " not admin for page: " + page.toString();
    }

    public CompanySocialPage getCompanySocialPage() {
        return page;
    }

    public User getUser() {
        return user;
    }
}
