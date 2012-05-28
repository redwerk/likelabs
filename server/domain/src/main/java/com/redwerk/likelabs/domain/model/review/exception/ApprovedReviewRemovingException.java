package com.redwerk.likelabs.domain.model.review.exception;

import com.redwerk.likelabs.domain.model.review.Review;

public class ApprovedReviewRemovingException extends RuntimeException {

    private final Review review;

    public ApprovedReviewRemovingException(Review review) {
        this.review = review;
    }

    public Review getReview() {
        return review;
    }

}
