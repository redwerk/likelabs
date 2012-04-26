package com.redwerk.likelabs.application;

import com.redwerk.likelabs.application.dto.CompanyData;
import com.redwerk.likelabs.domain.model.company.Company;

import java.util.List;

public interface CompanyService {

    Company getCompany(long companyId);

    Company getCompany(String companyName);

    List<Company> getAllCompanies();

    Company createCompany(CompanyData companyData);

    void deleteCompany(long companyId);

    void updateCompany(long companyId, CompanyData companyData);

}
