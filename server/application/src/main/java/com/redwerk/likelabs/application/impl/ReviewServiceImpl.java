package com.redwerk.likelabs.application.impl;

import com.redwerk.likelabs.application.ReviewService;
import com.redwerk.likelabs.domain.model.Comment;
import com.redwerk.likelabs.domain.model.Review;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Override
    public Review getReview(long reviewId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Review> getReviewsForPoint(long pointId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Review> getReviewsByUser(long userId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Review createReview(long companyId, long userId, String text, byte[] imageData) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteReview(long pointId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Review updateReview(long reviewId, long userId, String text, byte[] imageData) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Review moderateReview(long reviewId, long userId, boolean isApproved) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Comment getComment(long commentId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Comment> getComments(long reviewId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Comment addComment(long reviewId, long userId, String text) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteComment(long commentId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Review updateComment(long commentId, long userId, String text) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Review moderateComment(long commentId, long userId, boolean isApproved) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
