package com.redwerk.likelabs.application.dto;

import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.review.SortingRule;
import com.redwerk.likelabs.domain.model.review.ContentTypeFilter;

import java.util.Date;
import java.util.List;

public class ReviewQueryData {
    
    private final List<Long> pointIds;
    
    private final Date fromDate;
    
    private final Date toDate;
    
    private final ContentTypeFilter type;

    private final Boolean sampleStatus;

    private final Boolean publishingStatus;

    private final Pager pager;
    
    private final SortingRule sortingRule;


    public ReviewQueryData(List<Long> pointIds, Date fromDate, Date toDate, ContentTypeFilter type,
                           Boolean sampleStatus, Boolean publishingStatus, Pager pager, SortingRule sortingRule) {
        this.pointIds = pointIds;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.type = type;
        this.sampleStatus = sampleStatus;
        this.publishingStatus = publishingStatus;
        this.pager = pager;
        this.sortingRule = sortingRule;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Pager getPager() {
        return pager;
    }

    public List<Long> getPointIds() {
        return pointIds;
    }

    public SortingRule getSortingRule() {
        return sortingRule;
    }

    public Date getToDate() {
        return toDate;
    }

    public ContentTypeFilter getType() {
        return type;
    }

    public Boolean getSampleStatus() {
        return sampleStatus;
    }

    public Boolean getPublishingStatus() {
        return publishingStatus;
    }

}
