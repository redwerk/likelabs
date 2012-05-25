package com.redwerk.likelabs.application.impl;

import com.redwerk.likelabs.application.ReviewService;
import com.redwerk.likelabs.application.dto.PhotoData;
import com.redwerk.likelabs.application.dto.RecipientData;
import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.dto.ReviewQueryData;
import com.redwerk.likelabs.domain.model.photo.Photo;
import com.redwerk.likelabs.domain.model.photo.PhotoRepository;
import com.redwerk.likelabs.domain.model.photo.PhotoStatus;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.review.*;
import com.redwerk.likelabs.domain.model.tablet.Tablet;
import com.redwerk.likelabs.domain.model.tablet.TabletRepository;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserRepository;
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
        Tablet tablet = tabletRepository.get(tabletId);
        // get or create author
        User author = getOrCreateReviewAuthor(phone);
        // creates photos
        Photo reviewPhoto = null;
        for (PhotoData photoData: photos) {
            Photo photo = new Photo(author, photoData.getStatus(), photoData.getImage());
            if (photo.getStatus() == PhotoStatus.SELECTED) {
                if (reviewPhoto != null) {
                    throw new IllegalArgumentException("only one photo can be selected");
                }
                reviewPhoto = photo;
            }
            photoRepository.add(photo);
        }
        if (reviewPhoto == null) {
            throw new IllegalArgumentException("review photo is not selected");
        }
        // create recipients
        Set<Recipient> reviewRecipients = new HashSet<Recipient>();
        for (RecipientData recipientData: recipients) {
            reviewRecipients.add(new Recipient(recipientData.getType(), recipientData.getAddress()));
        }
        // create review with proper status
        Point point = tablet.getPoint();
        ReviewStatus status = point.getCompany().isModerateReviews() ? ReviewStatus.PENDING : ReviewStatus.APPROVED;
        Review review = Review.createReview(author, point, text, reviewPhoto, reviewRecipients);
        reviewRepository.add(review);
        // publish in user sn ?
        if (author.isPublishInSN()) {
        }
        // create events
        // send notifications for recipients
        return review;
    }
    
    private User getOrCreateReviewAuthor(String phone) {
        User author = userRepository.find(phone); // or create
        if (author == null) {
        }
        return author;
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
        review.setStatus(status, user);
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
