package com.redwerk.likelabs.domain.model.review;

import com.redwerk.likelabs.domain.model.review.exception.ApprovedReviewRemovingException;

public abstract class ReviewRepository {

    public abstract ReviewQuery getQuery();

    public abstract Review get(long id);

    public abstract void add(Review review);

    public void remove(Review review) {
        if (review.getStatus() == ReviewStatus.APPROVED) {
            throw new ApprovedReviewRemovingException(review);
        }
        removeInternal(review);
    }

    protected abstract void removeInternal(Review review);

}
