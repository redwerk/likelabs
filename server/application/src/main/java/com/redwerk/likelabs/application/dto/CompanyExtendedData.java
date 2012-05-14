package com.redwerk.likelabs.application.dto;

import com.redwerk.likelabs.domain.model.company.Company;

public class CompanyExtendedData {

    private final Company company;
    
    private final int pointsNumber;
    
    private final int reviewsNumber;

    public CompanyExtendedData(Company company, int pointsNumber, int reviewsNumber) {
        this.company = company;
        this.pointsNumber = pointsNumber;
        this.reviewsNumber = reviewsNumber;
    }

    public Company getCompany() {
        return company;
    }

    public int getPointsNumber() {
        return pointsNumber;
    }

    public int getReviewsNumber() {
        return reviewsNumber;
    }
    
}
