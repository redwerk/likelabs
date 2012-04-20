package com.redwerk.likelabs.application;

import com.redwerk.likelabs.domain.model.Comment;
import com.redwerk.likelabs.domain.model.Review;

import java.util.List;

public interface ReviewService {

    Review getReview(long reviewId);

    List<Review> getReviewsForPoint(long pointId);

    List<Review> getReviewsByUser(long userId);

    Review createReview(long companyId, long userId, String text, byte[] imageData);

    void deleteReview(long pointId);

    Review updateReview(long reviewId, long userId, String text, byte[] imageData);

    Review moderateReview(long reviewId, long userId, boolean isApproved);


    Comment getComment(long commentId);

    List<Comment> getComments(long reviewId);

    Comment addComment(long reviewId, long userId, String text);

    void deleteComment(long commentId);

    Review updateComment(long commentId, long userId, String text);

    Review moderateComment(long commentId, long userId, boolean isApproved);

}
