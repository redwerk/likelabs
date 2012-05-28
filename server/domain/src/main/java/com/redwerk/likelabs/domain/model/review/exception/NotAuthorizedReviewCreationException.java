package com.redwerk.likelabs.domain.model.review.exception;

import com.redwerk.likelabs.domain.model.user.User;

public class NotAuthorizedReviewCreationException extends RuntimeException {

    private final User author;

    public NotAuthorizedReviewCreationException(User author) {
        this.author = author;
    }

    public User getAuthor() {
        return author;
    }

}
