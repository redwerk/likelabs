package com.redwerk.likelabs.web.ui.dto;

public class IntervalsSettingsDto {

    private String userCreatedReview;
    private String pointCreatedReview;
    private String userApprovedReview;

    public IntervalsSettingsDto() {
    }

    public IntervalsSettingsDto(String userCreatedReview, String pointCreatedReview, String userApprovedReview) {
        this.userCreatedReview = userCreatedReview;
        this.pointCreatedReview = pointCreatedReview;
        this.userApprovedReview = userApprovedReview;
    }

    public String getPointCreatedReview() {
        return pointCreatedReview;
    }

    public void setPointCreatedReview(String pointCreatedReview) {
        this.pointCreatedReview = pointCreatedReview;
    }

    public String getUserApprovedReview() {
        return userApprovedReview;
    }

    public void setUserApprovedReview(String userApprovedReview) {
        this.userApprovedReview = userApprovedReview;
    }

    public String getUserCreatedReview() {
        return userCreatedReview;
    }

    public void setUserCreatedReview(String userCreatedReview) {
        this.userCreatedReview = userCreatedReview;
    }
}
