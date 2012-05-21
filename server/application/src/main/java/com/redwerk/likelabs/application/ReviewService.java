package com.redwerk.likelabs.application;

import com.redwerk.likelabs.application.dto.*;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.review.ReviewStatus;

import java.util.List;

public interface ReviewService {

    Report<Review> getPublicReviews(long companyId, ReviewQueryData queryData);

    Report<Review> getCompanyReviews(long companyId, ReviewStatus status, ReviewQueryData queryData);

    Report<Review> getModeratorReviews(long moderatorId, ReviewStatus status, ReviewQueryData queryData);

    Report<Review> getUserReviews(long authorId, List<Long> companyIds, ReviewQueryData queryData);
    
    Review getReview(long reviewId);
    
    
    Review createReview(long tabletId, String userPhone, String text, List<PhotoData> photos, List<RecipientData> recipients);


    void updateReview(long userId, long reviewId, String text);

    void updateStatus(long userId, long reviewId, ReviewStatus status,
            boolean useAsSample, boolean publishOnCompanyPage);
    
    void deleteReview(long reviewId);
    
}

