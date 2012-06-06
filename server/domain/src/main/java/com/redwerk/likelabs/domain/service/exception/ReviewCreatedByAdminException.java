package com.redwerk.likelabs.domain.service.exception;

import com.redwerk.likelabs.domain.model.user.User;

public class ReviewCreatedByAdminException extends RuntimeException {

    private final User admin;

    public ReviewCreatedByAdminException(User admin) {
        this.admin = admin;
    }

    public User getAdmin() {
        return admin;
    }

}
