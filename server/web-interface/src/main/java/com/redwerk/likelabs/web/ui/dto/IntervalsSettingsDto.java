package com.redwerk.likelabs.web.ui.dto;

import com.redwerk.likelabs.domain.model.notification.Period;

public class IntervalsSettingsDto {

    private Period userCreatedReview;
    private Period pointCreatedReview;
    private Period userApprovedReview;

    public IntervalsSettingsDto() {
    }

    public IntervalsSettingsDto(Period userCreatedReview, Period pointCreatedReview, Period userApprovedReview) {
        this.userCreatedReview = userCreatedReview;
        this.pointCreatedReview = pointCreatedReview;
        this.userApprovedReview = userApprovedReview;
    }

    public Period getPointCreatedReview() {
        return pointCreatedReview;
    }

    public void setPointCreatedReview(Period pointCreatedReview) {
        this.pointCreatedReview = pointCreatedReview;
    }

    public Period getUserApprovedReview() {
        return userApprovedReview;
    }

    public void setUserApprovedReview(Period userApprovedReview) {
        this.userApprovedReview = userApprovedReview;
    }

    public Period getUserCreatedReview() {
        return userCreatedReview;
    }

    public void setUserCreatedReview(Period userCreatedReview) {
        this.userCreatedReview = userCreatedReview;
    }
}
