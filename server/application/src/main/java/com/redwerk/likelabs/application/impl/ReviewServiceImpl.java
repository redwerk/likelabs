package com.redwerk.likelabs.application.impl;

import com.redwerk.likelabs.application.ReviewService;
import com.redwerk.likelabs.application.dto.PhotoData;
import com.redwerk.likelabs.application.dto.RecipientData;
import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.dto.ReviewQuery;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.review.ReviewRepository;
import com.redwerk.likelabs.domain.model.review.ReviewStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;


    @Override
    @Transactional(readOnly = true)
    public Report<Review> getPublicReviews(long companyId, ReviewQuery query) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transactional(readOnly = true)
    public Report<Review> getCompanyReviews(long companyId, ReviewStatus status, ReviewQuery query) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transactional(readOnly = true)
    public Report<Review> getModeratorReviews(long moderatorId, ReviewStatus status, ReviewQuery query) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transactional(readOnly = true)
    public Report<Review> getUserReviews(long userId, List<Long> companyIds, ReviewQuery query) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transactional(readOnly = true)
    public Review getReview(long reviewId) {
        return reviewRepository.get(reviewId);
    }

    @Override
    @Transactional
    public Review createReview(long userId, long tabletId, String text, List<PhotoData> photos, List<RecipientData> recipients) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transactional
    public void updateReview(long userId, long reviewId, String text) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transactional
    public void updateStatus(long userId, long reviewId, ReviewStatus status, boolean useAsPromo, boolean publishOnCompanyPage) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transactional
    public void updateStatus(long userId, long reviewId, ReviewStatus status) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transactional
    public void updatePromoStatus(long userId, long reviewId, boolean useAsPromo) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transactional
    public void publishOnCompanyPage(long userId, long reviewId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transactional
    public void deleteReview(long reviewId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
