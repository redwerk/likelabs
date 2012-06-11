package com.redwerk.likelabs.web.ui.utils;

import com.redwerk.likelabs.application.dto.ReviewQueryData;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.review.ContentTypeFilter;
import com.redwerk.likelabs.domain.model.review.SortingCriteria;
import com.redwerk.likelabs.domain.model.review.SortingOrder;
import com.redwerk.likelabs.domain.model.review.SortingRule;
import com.redwerk.likelabs.web.ui.dto.ReviewFilterDto;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;

public class QueryFilterBuilder {

    public static final int ITEMS_PER_PAGE_REVIEW = 8;
    public static final int ITEMS_PER_PAGE_PHOTO = 8;
    public static final int ITEMS_PER_PAGE_COMPANY = 10;
    public static final int ITEMS_PER_PAGE_USER = 10;

    
    public static ReviewQueryData buildReviewQuery(ReviewFilterDto filter) throws ParseException {

        List<Long> pointIds = null;
        if (filter.getPoint() != null) {
            pointIds = new ArrayList<Long>();
            pointIds.add(filter.getPoint());
        }
        ContentTypeFilter contentType = null;
        if (StringUtils.isNotBlank(filter.getFeedType())) {
            contentType = ContentTypeFilter.valueOf(filter.getFeedType().toUpperCase(Locale.ENGLISH));
        }
        SortingRule sort = null;
        if (StringUtils.isNotBlank(filter.getSortBy())) {
            SortingCriteria sortingCriteria = SortingCriteria.valueOf(filter.getSortBy().toUpperCase(Locale.ENGLISH));
            SortingOrder sortingOrder = null;
            switch (sortingCriteria) {
                case DATE:
                    sortingOrder = SortingOrder.DESCENDING;
                    break;
                case REVIEW_TYPE:
                    sortingOrder = SortingOrder.DESCENDING;
                    break;
                default:
                    sortingOrder = SortingOrder.ASCENDING;
            }
            sort = new SortingRule(sortingCriteria, sortingOrder);
        }
        return new ReviewQueryData(pointIds, filter.getFromDate(), filter.getToDate(),
                contentType, filter.getSampleStatus(), filter.getPublishingStatus(), buildPagerFeed(filter.getPage()), sort);
    }

    public static Pager buildPagerPhoto(Integer page) {

        return new Pager(page * ITEMS_PER_PAGE_PHOTO, ITEMS_PER_PAGE_PHOTO);
    }

    public static Pager buildPagerFeed(Integer page) {

        return new Pager(page * ITEMS_PER_PAGE_REVIEW, ITEMS_PER_PAGE_REVIEW);
    }

    public static Pager buildPagerUsers(Integer page) {

        return new Pager(page * ITEMS_PER_PAGE_USER, ITEMS_PER_PAGE_USER);
    }

    public static Pager buildPagerCompanies(Integer page) {

        return new Pager(page * ITEMS_PER_PAGE_COMPANY, ITEMS_PER_PAGE_COMPANY);
    }
}
