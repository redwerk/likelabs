package com.redwerk.likelabs.domain.model.review;

import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.query.SortingRule;

import java.util.Date;
import java.util.List;

public interface ReviewQuery {

    ReviewQuery setAuthorId(long authorId);
    
    ReviewQuery setModeratorId(long moderatorId);

    ReviewQuery setCompanyIds(List<Long> companyIds);

    ReviewQuery setPointIds(List<Long> pointIds);

    ReviewQuery setDateRange(Date fromDate, Date toDate);

    ReviewQuery setContentType(ReviewContentType contentType);

    ReviewQuery setStatus(ReviewStatus status);

    ReviewQuery setPager(Pager pager);

    ReviewQuery setSortingRule(SortingRule sortingRule);


    List<Review> findReviews();

    int getCount();

}
