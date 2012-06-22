package com.redwerk.likelabs.domain.service.sn.exception;

import com.redwerk.likelabs.domain.model.company.CompanySocialPage;

public class WrongCompanyPageException extends SNException {

    private final CompanySocialPage companyPage;

    public WrongCompanyPageException(CompanySocialPage companyPage) {
        this.companyPage = companyPage;
    }

    public CompanySocialPage getCompanyPage() {
        return companyPage;
    }

}
