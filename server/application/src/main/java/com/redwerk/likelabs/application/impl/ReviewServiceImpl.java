package com.redwerk.likelabs.application.impl;

import com.redwerk.likelabs.application.RegistrationService;
import com.redwerk.likelabs.application.ReviewService;
import com.redwerk.likelabs.domain.service.RecipientNotifier;
import com.redwerk.likelabs.domain.service.ReviewRegistrator;
import com.redwerk.likelabs.domain.service.dto.PhotoData;
import com.redwerk.likelabs.domain.service.dto.RecipientData;
import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.dto.ReviewQueryData;
import com.redwerk.likelabs.domain.model.event.EventRepository;
import com.redwerk.likelabs.domain.model.photo.Photo;
import com.redwerk.likelabs.domain.model.photo.PhotoRepository;
import com.redwerk.likelabs.domain.model.photo.PhotoStatus;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.review.*;
import com.redwerk.likelabs.domain.model.tablet.Tablet;
import com.redwerk.likelabs.domain.model.tablet.TabletRepository;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserRepository;
import com.redwerk.likelabs.domain.service.impl.BasicReviewRegistrator;
import com.redwerk.likelabs.domain.service.sn.GatewayFactory;
import com.redwerk.likelabs.domain.service.sn.ImageSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import org.apache.commons.lang.time.DateUtils;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TabletRepository tabletRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private GatewayFactory gatewayFactory;

    @Autowired
    private ImageSourceFactory imageSourceFactory;

    @Autowired
    private RecipientNotifier recipientNotifier;

    @Autowired
    private RegistrationService registrationService;

    private ReviewRegistrator reviewRegistrator;

    private ReviewRegistrator getReviewRegistrator() {
        if (reviewRegistrator == null) {
            reviewRegistrator = new BasicReviewRegistrator(userRepository, registrationService, reviewRepository,
                    photoRepository, eventRepository, gatewayFactory, imageSourceFactory, recipientNotifier);
        }
        return reviewRegistrator;
    }

    // implementation of ReviewService

    @Override
    @Transactional(readOnly = true)
    public Report<Review> getPublicReviews(long companyId, ReviewQueryData queryData) {
        ReviewQuery query = getQuery(queryData).setCompanyIds(Arrays.asList(companyId));
        return new Report<Review>(getLoadedReviews(query.findReviews()), query.getCount());
    }

    private ReviewQuery getQuery(ReviewQueryData queryData) {
        ReviewQuery query = reviewRepository.getQuery();
        query.setPointIds(queryData.getPointIds());
        Date fromDate = (queryData.getFromDate() != null) ?
            DateUtils.truncate(queryData.getFromDate(), Calendar.DATE) : null;
        Date toDate = (queryData.getToDate() != null) ?
            DateUtils.truncate(queryData.getToDate(), Calendar.DATE) : null;
        if (toDate != null) {
            toDate = getIncrementedDate(toDate);
        }
        query.setDateRange(fromDate, toDate);
        query.setContentType(queryData.getType());
        query.setPager(queryData.getPager());
        query.setSortingRule(queryData.getSortingRule());
        query.setSampleStatus(queryData.getSampleStatus());
        query.setPublishingStatus(queryData.getPublishingStatus());
        return query;
    }

    private Date getIncrementedDate(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        date = cal.getTime();
        return date;
    }

    @Override
    @Transactional(readOnly = true)
    public Report<Review> getCompanyReviews(long companyId, ReviewStatus status, ReviewQueryData queryData) {
        ReviewQuery query = getQuery(queryData)
                .setCompanyIds(Arrays.asList(companyId))
                .setStatus(status);
        return new Report<Review>(getLoadedReviews(query.findReviews()), query.getCount());
    }

    @Override
    @Transactional(readOnly = true)
    public Report<Review> getModeratorReviews(long moderatorId, ReviewStatus status, ReviewQueryData queryData) {
        ReviewQuery query = getQuery(queryData)
                .setModeratorId(moderatorId)
                .setStatus(status);
        return new Report<Review>(getLoadedReviews(query.findReviews()), query.getCount());
    }

    @Override
    @Transactional(readOnly = true)
    public Report<Review> getUserReviews(long authorId, List<Long> companyIds, ReviewQueryData queryData) {
        ReviewQuery query = getQuery(queryData)
                .setAuthorId(authorId)
                .setCompanyIds(companyIds);
        return new Report<Review>(getLoadedReviews(query.findReviews()), query.getCount());
    }

    private List<Review> getLoadedReviews(List<Review> reviews) {
        for (Review r: reviews) {
            r.getAuthor().getAccounts();
            r.getPoint().getAddress();
        }
        return reviews;
    }

    @Override
    @Transactional(readOnly = true)
    public Review getReview(long reviewId) {
        return reviewRepository.get(reviewId);
    }

    @Override
    @Transactional
    public Review createReview(long tabletId, String phone, String text, List<PhotoData> photos, List<RecipientData> recipients) {
        return getReviewRegistrator().createAndRegisterReview(tabletRepository.get(tabletId), phone, text, photos, recipients);
    }
    
    @Override
    @Transactional
    public void updateReview(long userId, long reviewId, String text) {
        User user = userRepository.get(userId);
        Review review = reviewRepository.get(reviewId);
        review.setMessage(text, user);
    }

    @Override
    @Transactional
    public void updateStatus(long userId, long reviewId, ReviewStatus status, boolean useAsSample, boolean publishOnCompanyPage) {
        User user = userRepository.get(userId);
        Review review = reviewRepository.get(reviewId);
        review.setStatus(status, user, eventRepository);
        if (publishOnCompanyPage) {
            review.publishInCompanySN(user);
        }
        review.setSampleStatus(useAsSample, user);
    }

    @Override
    @Transactional
    public void deleteReview(long reviewId) {
        reviewRepository.remove(reviewRepository.get(reviewId));
    }

}
