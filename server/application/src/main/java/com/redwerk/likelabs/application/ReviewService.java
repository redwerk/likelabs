package com.redwerk.likelabs.application;

import com.redwerk.likelabs.domain.model.review.Review;

import java.util.List;

public interface ReviewService {

    Review getReview(long reviewId);

    List<Review> getReviewsForPoint(long pointId);

    List<Review> getReviewsByUser(long userId);

    Review createReview(long companyId, long userId, String text, byte[] imageData);

    void deleteReview(long pointId);

    Review updateReview(long reviewId, long userId, String text, byte[] imageData);

    Review moderateReview(long reviewId, long userId, boolean isApproved);

}
