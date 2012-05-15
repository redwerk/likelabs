package com.redwerk.likelabs.domain.model.review.exception;

public class ReviewNotFoundException extends RuntimeException {

    private final long reviewId;

    public ReviewNotFoundException(long reviewId) {
        this.reviewId = reviewId;
    }

    public long getReviewId() {
        return reviewId;
    }

}
