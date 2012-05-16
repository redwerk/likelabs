package com.redwerk.likelabs.application;

import com.redwerk.likelabs.application.dto.*;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.review.ReviewStatus;

import java.util.List;

public interface ReviewService {

    Report<Review> getPublicReviews(long companyId, ReviewQueryData query);

    Report<Review> getCompanyReviews(long companyId, ReviewStatus status, ReviewQueryData query);

    Report<Review> getModeratorReviews(long moderatorId, ReviewStatus status, ReviewQueryData query);

    Report<Review> getUserReviews(long userId, List<Long> companyIds, ReviewQueryData query);
    
/*
    List<Review> getReviews(long authorId, long moderatorId, 
            List<Long> companyIds, List<Long> pointIds,
            DateFilter dates, ReviewContentType type, ReviewStatus status,
            PagerFilter pager, SortingRule sortingRule);

*/

    Review getReview(long reviewId);
    
    
    Review createReview(long userId, long tabletId, String text,
            List<PhotoData> photos, List<RecipientData> recipients);

    
    void updateReview(long userId, long reviewId, String text);

    void updateStatus(long userId, long reviewId, ReviewStatus status,
            boolean useAsPromo, boolean publishOnCompanyPage);
    
    void updateStatus(long userId, long reviewId, ReviewStatus status);
    
    void updatePromoStatus(long userId, long reviewId, boolean useAsPromo);
    
    void publishOnCompanyPage(long userId, long reviewId);

    
    void deleteReview(long reviewId);
    
}

