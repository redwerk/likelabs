package com.redwerk.likelabs.web.ui.dto;

import com.redwerk.likelabs.domain.model.review.ReviewStatus;
import java.util.Date;
import org.apache.commons.lang.StringUtils;

public class ReviewFilterDto {

    //params reguest
    private Long company = null;
    private Long point = null;
    private String feedType = null;
    private String sortBy = null;
    private Date toDate = null;
    private Date fromDate = null;
    private Integer page = 0;
    private String status = null;

    //fields for filter quey bilder
    private ReviewStatus reviewStatus = null;
    private Boolean publishingStatus = null;
    private Boolean sampleStatus = null;

    public void setStatus(String status) {
        if (StringUtils.isBlank(status)) {
            return;
        }
        if (status.equals("published")) {
            this.publishingStatus = true;
        }
        if (status.equals("promo")) {
            this.sampleStatus = true;
        }
        if (status.equals("pending")) {
            this.reviewStatus = ReviewStatus.PENDING;
        }
        if (status.equals("approved")) {
            this.reviewStatus = ReviewStatus.APPROVED;
        }
        if (status.equals("archived")) {
            this.reviewStatus = ReviewStatus.ARCHIVED;
        }
        if (status.equals("flagged")) {
            this.reviewStatus = ReviewStatus.FLAGGED;
        }
        this.status = status;
    }

    public Long getCompany() {
        return company;
    }

    public void setCompany(Long company) {
        this.company = company;
    }

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    public Boolean getPublishingStatus() {
        return publishingStatus;
    }

    public void setPublishingStatus(Boolean publishingStatus) {
        this.publishingStatus = publishingStatus;
    }

    public ReviewStatus getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(ReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public Boolean getSampleStatus() {
        return sampleStatus;
    }

    public void setSampleStatus(Boolean sampleStatus) {
        this.sampleStatus = sampleStatus;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getStatus() {
        return status;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}