package com.redwerk.likelabs.application.impl;

import com.redwerk.likelabs.application.ReviewService;
import com.redwerk.likelabs.application.dto.PhotoData;
import com.redwerk.likelabs.application.dto.RecipientData;
import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.dto.ReviewQueryData;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.review.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;


    @Override
    @Transactional(readOnly = true)
    public Report<Review> getPublicReviews(long companyId, ReviewQueryData queryData) {
        ReviewQuery query = getQuery(queryData).setCompanyIds(Arrays.asList(companyId));
        return new Report<Review>(query.findReviews(), query.getCount());
    }

    private ReviewQuery getQuery(ReviewQueryData queryData) {
        ReviewQuery query = reviewRepository.getQuery();
        query.setPointIds(queryData.getPointIds());
        query.setDateRange(queryData.getFromDate(), queryData.getToDate());
        query.setContentType(queryData.getType());
        query.setPager(queryData.getPager());
        query.setSortingRule(queryData.getSortingRule());
        return query;
    }

    @Override
    @Transactional(readOnly = true)
    public Report<Review> getCompanyReviews(long companyId, ReviewStatus status, ReviewQueryData queryData) {
        ReviewQuery query = getQuery(queryData)
                .setCompanyIds(Arrays.asList(companyId))
                .setStatus(status);
        return new Report<Review>(query.findReviews(), query.getCount());
    }

    @Override
    @Transactional(readOnly = true)
    public Report<Review> getModeratorReviews(long moderatorId, ReviewStatus status, ReviewQueryData queryData) {
        ReviewQuery query = getQuery(queryData)
                .setModeratorId(moderatorId)
                .setStatus(status);
        return new Report<Review>(query.findReviews(), query.getCount());
    }

    @Override
    @Transactional(readOnly = true)
    public Report<Review> getUserReviews(long authorId, List<Long> companyIds, ReviewQueryData queryData) {
        ReviewQuery query = getQuery(queryData)
                .setAuthorId(authorId)
                .setCompanyIds(companyIds);
        return new Report<Review>(query.findReviews(), query.getCount());
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
