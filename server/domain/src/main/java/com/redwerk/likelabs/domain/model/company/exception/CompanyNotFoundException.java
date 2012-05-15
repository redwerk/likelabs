package com.redwerk.likelabs.domain.model.company.exception;

public class CompanyNotFoundException extends RuntimeException {

    private final long companyId;

    public CompanyNotFoundException(long companyId) {
        this.companyId = companyId;
    }

    public long getCompanyId() {
        return companyId;
    }

}
