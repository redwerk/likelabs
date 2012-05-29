package com.redwerk.likelabs.domain.model.review;

import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.user.User;

import java.util.Date;
import java.util.List;

public interface ReviewQuery {

    ReviewQuery setAuthorId(Long authorId);
    
    ReviewQuery setAdmin(User admin);

    ReviewQuery setCompanyIds(List<Long> companyIds);

    ReviewQuery setPointIds(List<Long> pointIds);

    ReviewQuery setDateRange(Date fromDate, Date toDate);

    ReviewQuery setContentType(ContentTypeFilter contentType);

    ReviewQuery setStatus(ReviewStatus status);

    ReviewQuery setSampleStatus(Boolean promoStatus);

    ReviewQuery setPublishingStatus(Boolean publishingStatus);

    ReviewQuery setPager(Pager pager);

    ReviewQuery setSortingRule(SortingRule sortingRule);


    List<Review> findReviews();

    int getCount();

}
