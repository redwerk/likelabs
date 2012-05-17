package com.redwerk.likelabs.domain.model.review.exception;

import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.user.User;

public class NotAuthorizedReviewUpdateException extends RuntimeException {

    private final User modifier;

    private final Review review;

    private final UpdateType updateType;


    public NotAuthorizedReviewUpdateException(User modifier, Review review, UpdateType updateType) {
        this.modifier = modifier;
        this.review = review;
        this.updateType = updateType;
    }

    public User getModifier() {
        return modifier;
    }

    public Review getReview() {
        return review;
    }

    public UpdateType getUpdateType() {
        return updateType;
    }

}
