package com.redwerk.likelabs.application.dto.company;

import com.redwerk.likelabs.domain.model.company.Company;

public class CompanyReportItem {

    private final Company company;
    
    private final int pointsNumber;
    
    private final int reviewsNumber;

    public CompanyReportItem(Company company, int pointsNumber, int reviewsNumber) {
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
